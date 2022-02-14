package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroProductRepository
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetShopProductsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetProductsInCampaignUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetProductTagSummarySectionUseCase
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
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
    private val getShopProductsUseCase: GetShopProductsUseCase,
    private val addProductTagUseCase: AddProductTagUseCase,
    private val getProductTagSummarySectionUseCase: GetProductTagSummarySectionUseCase,
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
        page: Int,
        keyword: String,
        sort: Int,
    ): PagedDataUiModel<ProductUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not has shop")

        val response = getShopProductsUseCase.apply {
            setRequestParams(
                GetShopProductsUseCase.createParams(
                    shopId = userSession.shopId,
                    page = page,
                    perPage = PRODUCTS_IN_ETALASE_PER_PAGE,
                    etalaseId = etalaseId,
                    keyword = keyword,
                    sort = sort,
                )
            )
        }.executeOnBackground()

        return@withContext productMapper.mapProductsInEtalase(response)
    }

    override suspend fun getProductsInCampaign(
        campaignId: String,
        page: Int,
    ) = withContext(dispatchers.io) {
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
            addProductTagUseCase.apply {
                params = AddProductTagUseCase.createParams(
                    channelId = channelId,
                    productIds = productIds
                )
            }.executeOnBackground()
        }
    }

    override suspend fun getProductTagSummarySection(channelID: Int) = withContext(dispatchers.io) {
        val response = getProductTagSummarySectionUseCase.apply {
            setRequestParams(GetProductTagSummarySectionUseCase.createparams(channelID))
        }.executeOnBackground()

        return@withContext productMapper.mapProductTagSection(response)
    }

    companion object {
        private const val PRODUCTS_IN_ETALASE_PER_PAGE = 25
        private const val PRODUCTS_IN_CAMPAIGN_PER_PAGE = 25
    }
}