package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.model.PinnedProductException
import com.tokopedia.play.broadcaster.domain.model.addproduct.AddProductTagChannelRequest
import com.tokopedia.play.broadcaster.domain.repository.PlayBroProductRepository
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetPinnedProductUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetProductTagSummarySectionUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetProductsInCampaignUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val getProductsInCampaignUseCase: GetProductsInCampaignUseCase,
    private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
    private val getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
    private val addProductTagUseCase: AddProductTagUseCase,
    private val getProductTagSummarySectionUseCase: GetProductTagSummarySectionUseCase,
    private val setPinnedProductUseCase: SetPinnedProductUseCase,
    private val productMapper: PlayBroProductUiMapper,
    private val userSession: UserSessionInterface,
) : PlayBroProductRepository {

    override suspend fun getCampaignList(): List<CampaignUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not has shop")

        val response = getCampaignListUseCase.apply {
            setRequestParams(GetCampaignListUseCase.createParams(shopId = userSession.shopId))
        }.executeOnBackground()

        return@withContext productMapper.mapCampaignList(response)
    }

    override suspend fun getEtalaseList(): List<EtalaseUiModel> = withContext(dispatchers.io) {
        val response = getSelfEtalaseListUseCase.executeOnBackground()

        return@withContext productMapper.mapEtalaseList(response)
    }

    override suspend fun getProductsInEtalase(
        etalaseId: String,
        cursor: String,
        keyword: String,
        sort: SortUiModel,
    ): PagedDataUiModel<ProductUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not has shop")

        val param = GetProductsInEtalaseUseCase.Param(
            authorId = userSession.shopId,
            authorType = AUTHOR_TYPE_SELLER,
            cursor = cursor,
            limit = PRODUCTS_IN_ETALASE_PER_PAGE,
            keyword = keyword,
            sort = sort,
            etalaseId = etalaseId,
        )

        val response = getProductsInEtalaseUseCase.executeWithParam(param)

        return@withContext productMapper.mapProductsInEtalase(response)
    }

    override suspend fun getProductsInCampaign(
        campaignId: String,
        page: Int,
    ): PagedDataUiModel<ProductUiModel> = withContext(dispatchers.io) {
        if (userSession.userId.isBlank()) error("User does not exist")

        val response = getProductsInCampaignUseCase.apply {
            setRequestParams(
                GetProductsInCampaignUseCase.createParams(
                    userId = userSession.userId,
                    campaignId = campaignId,
                    page = page,
                    perPage = PRODUCTS_IN_CAMPAIGN_PER_PAGE,
                )
            )
        }.executeOnBackground()

        return@withContext productMapper.mapProductsInCampaign(response)
    }

    override suspend fun setProductTags(channelId: String, productIds: List<String>) {
        withContext(dispatchers.io) {
            addProductTagUseCase(AddProductTagChannelRequest(channelId, productIds))
        }
    }

    override suspend fun getProductTagSummarySection(channelID: String, fetchCommission: Boolean) =
        withContext(dispatchers.io) {
            val response = getProductTagSummarySectionUseCase.apply {
                setRequestParams(GetProductTagSummarySectionUseCase.createParams(channelID, fetchCommission))
            }.executeOnBackground()

            return@withContext productMapper.mapProductTagSection(response)
        }

    private var lastRequestTime: Long = 0L
    private val isEligibleForPin: Boolean
        get() {
            val diff = System.currentTimeMillis() - lastRequestTime
            return diff >= DELAY_MS
        }

    override suspend fun setPinProduct(channelId: String, product: ProductUiModel): Boolean =
        withContext(dispatchers.io) {
            return@withContext if (isEligibleForPin || product.pinStatus.isPinned) {
                setPinnedProductUseCase.apply {
                    setRequestParams(createParam(channelId, product))
                }.executeOnBackground().data.success.apply {
                    if (this && !product.pinStatus.isPinned) lastRequestTime = System.currentTimeMillis()
                    else if (!this) throw PinnedProductException(
                        String.format(
                            "Gagal %1s pin di produk. Coba lagi, ya.",
                            if (product.pinStatus.isPinned) "lepas" else "pasang"
                        )
                    )
                    else return@apply
                }
            } else {
                throw PinnedProductException()
            }
        }

    companion object {
        private const val PRODUCTS_IN_ETALASE_PER_PAGE = 25
        private const val PRODUCTS_IN_CAMPAIGN_PER_PAGE = 25

        private const val DELAY_MS = 5000L

        private const val AUTHOR_TYPE_SELLER = 2
    }
}
