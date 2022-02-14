package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.play.data.Config
import com.tokopedia.play.data.Product
import com.tokopedia.play.domain.GetProductTagItemSectionUseCase
import com.tokopedia.play.domain.repository.PlayViewerTagItemRepository
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.tagitem.*
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayViewerTagItemRepositoryImpl @Inject constructor(
    private val getProductTagItemsUseCase: GetProductTagItemSectionUseCase,
    private val getProductVariantUseCase: GetProductVariantUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val mapper: PlayUiModelMapper,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
): PlayViewerTagItemRepository {

    override suspend fun getTagItem(
        channelId: String
    ): TagItemUiModel = withContext(dispatchers.io) {
        val response = getProductTagItemsUseCase.apply {
            setRequestParams(GetProductTagItemSectionUseCase.createParam(channelId).parameters)
        }.executeOnBackground()

        try {
            val sectionList = mapper.mapProductSection(
                response.playGetTagsItem.sectionList
            )

            val prodArray = arrayListOf<Product>()

            response.playGetTagsItem.sectionList.forEach {
                it.listOfProducts.forEach {
                    product -> prodArray.add(product)
                }
            }
            val productList = mapper.mapProductTags(prodArray)

            val voucherList = mapper.mapMerchantVouchers(
                response.playGetTagsItem.voucherList
            )

            return@withContext TagItemUiModel(
                product = ProductUiModel(
                    productList = productList as List<PlayProductUiModel.Product>,
                    canShow = true,
                ),
                voucher = VoucherUiModel(
                    voucherList = voucherList,
                ),
                maxFeatured = response.playGetTagsItem.config.peekProductCount,
                resultState = ResultState.Success,
                section = SectionUiModel(sectionList, response.playGetTagsItem.config),
                bottomSheetTitle = response.playGetTagsItem.config.bottomSheetTitle
            )
        }catch (e: Exception){
            return@withContext TagItemUiModel(product = ProductUiModel( productList = emptyList(), canShow = false),
                voucher = VoucherUiModel(voucherList = emptyList()), maxFeatured = 1, resultState = ResultState.Success, section = SectionUiModel(
                    emptyList(), Config()
                ), bottomSheetTitle = ""
            )
        }
    }

    override suspend fun getVariant(
        product: PlayProductUiModel.Product
    ): VariantUiModel = withContext(dispatchers.io) {
        val response = getProductVariantUseCase.apply {
            params = getProductVariantUseCase.createParams(product.id)
        }.executeOnBackground()

        val selectedVariants = VariantCommonMapper.mapVariantIdentifierToHashMap(response.data)
        val categories = VariantCommonMapper.processVariant(
            variantData = response.data,
            mapOfSelectedVariant = selectedVariants
        )
        VariantUiModel(
            variantDetail = product,
            parentVariant = response.data,
            selectedVariants = selectedVariants,
            categories = categories.orEmpty(),
            stockWording = "",
        )
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
}