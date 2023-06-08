package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.play.domain.CheckUpcomingCampaignReminderUseCase
import com.tokopedia.play.domain.GetProductTagItemSectionUseCase
import com.tokopedia.play.domain.PostUpcomingCampaignReminderUseCase
import com.tokopedia.play.domain.repository.PlayViewerTagItemRepository
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.tagitem.*
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayViewerTagItemRepositoryImpl @Inject constructor(
    private val getProductTagItemsUseCase: GetProductTagItemSectionUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val checkUpcomingCampaignReminderUseCase: CheckUpcomingCampaignReminderUseCase,
    private val postUpcomingCampaignReminderUseCase: PostUpcomingCampaignReminderUseCase,
    private val atcOccUseCase: AddToCartOccMultiUseCase,
    private val mapper: PlayUiModelMapper,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
) : PlayViewerTagItemRepository {

    override suspend fun getTagItem(
        channelId: String,
        warehouseId: String,
        partnerName : String,
        channelType: PlayChannelType,
    ): TagItemUiModel = withContext(dispatchers.io) {
        val response = getProductTagItemsUseCase.apply {
            setRequestParams(
                GetProductTagItemSectionUseCase.createParam(
                    channelId,
                    warehouseId
                ).parameters
            )
        }.executeOnBackground()

        val productList = mapper.mapProductSection(response.playGetTagsItem.sectionList, channelType)

        val voucherList = mapper.mapMerchantVouchers(
            response.playGetTagsItem.voucherList,
            partnerName
        )

        return@withContext TagItemUiModel(
            product = ProductUiModel(
                productSectionList = updateCampaignReminderStatus(productList),
                canShow = true
            ),
            voucher = voucherList,
            maxFeatured = response.playGetTagsItem.config.peekProductCount,
            resultState = ResultState.Success,
            bottomSheetTitle = response.playGetTagsItem.config.bottomSheetTitle
        )
    }

    override suspend fun updateCampaignReminderStatus(
        productSections: List<ProductSectionUiModel.Section>
    ): List<ProductSectionUiModel.Section> = withContext(dispatchers.io) {
        if (userSession.isLoggedIn) {
            val reminderMap = productSections.filter {
                it.config.type == ProductSectionType.Upcoming
            }.associate {
                it.id to async {
                    checkUpcomingCampaign(it.id)
                }
            }

            productSections.map {
                it.copy(
                    config = it.config.copy(
                        reminder = if (reminderMap[it.id]?.await() == true) PlayUpcomingBellStatus.On
                        else it.config.reminder
                    )
                )
            }
        } else productSections
    }

    override suspend fun addProductToCart(
        id: String,
        name: String,
        shopId: String,
        minQty: Int,
        price: Double
    ): String = withContext(dispatchers.io) {
        try {
            val response = addToCartUseCase.apply {
                setParams(
                    AddToCartUseCase.getMinimumParams(
                        productId = id,
                        shopId = shopId,
                        quantity = minQty,
                        atcExternalSource = AtcFromExternalSource.ATC_FROM_PLAY,
                        productName = name,
                        price = price.toString(),
                        userId = userSession.userId,
                    )
                )
            }.executeOnBackground()
            if (response.isDataError()) throw MessageErrorException(response.getAtcErrorMessage())
            return@withContext response.data.cartId
        } catch (e: Throwable) {
            if (e is ResponseErrorException) throw MessageErrorException(e.localizedMessage)
            else throw e
        }
    }

    override suspend fun checkUpcomingCampaign(campaignId: String): Boolean =
        withContext(dispatchers.io) {
            val response = checkUpcomingCampaignReminderUseCase.apply {
                setRequestParams(CheckUpcomingCampaignReminderUseCase.createParam(campaignId.toLongOrZero()).parameters)
            }.executeOnBackground()
            return@withContext response.response.isAvailable
        }

    override suspend fun subscribeUpcomingCampaign(
        campaignId: String,
        shouldRemind: Boolean,
    ): PlayViewerTagItemRepository.CampaignReminder = withContext(dispatchers.io) {
        val response = postUpcomingCampaignReminderUseCase.apply {
            setRequestParams(
                PostUpcomingCampaignReminderUseCase.createParam(
                    campaignId.toLongOrZero(),
                    shouldRemind,
                ).parameters
            )
        }.executeOnBackground()

        val currentStatus = checkUpcomingCampaign(campaignId)

        PlayViewerTagItemRepository.CampaignReminder(
            isReminded = currentStatus,
            message = if (!response.response.success) response.response.errorMessage
            else if (shouldRemind != currentStatus) ""
            else response.response.message
        )
    }

    override suspend fun addProductToCartOcc(
        id: String,
        name: String,
        shopId: String,
        minQty: Int,
        price: Double
    ): String = withContext(dispatchers.io) {
        try {
            val response = atcOccUseCase.apply {
                setParams(
                    AddToCartOccMultiRequestParams(
                        carts = listOf(
                            AddToCartOccMultiCartParam(
                                productId = id,
                                shopId = shopId,
                                quantity = minQty.toString(),
                                productName = name,
                                price = price.toString()
                            ),
                        ),
                        userId = userSession.userId,
                        atcFromExternalSource = AtcFromExternalSource.ATC_FROM_PLAY
                    )
                )
            }.executeOnBackground()
            if (response.isStatusError()) throw MessageErrorException(response.getAtcErrorMessage())
            return@withContext response.data.cart.firstOrNull()?.cartId ?: ""
        } catch (e: Throwable) {
            if (e is ResponseErrorException) throw MessageErrorException(e.localizedMessage)
            else throw e
        }
    }
}
