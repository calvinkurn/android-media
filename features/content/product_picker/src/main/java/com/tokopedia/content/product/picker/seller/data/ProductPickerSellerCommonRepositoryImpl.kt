package com.tokopedia.content.product.picker.seller.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetProductsInCampaignUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.etalase.GetSelfEtalaseListUseCase
import com.tokopedia.content.product.picker.seller.mapper.ProductPickerSellerCommonMapper
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 14, 2023
 */
class ProductPickerSellerCommonRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val getProductsInCampaignUseCase: GetProductsInCampaignUseCase,
    private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
    private val commonMapper: ProductPickerSellerCommonMapper,
    private val userSession: UserSessionInterface,
) : ProductPickerSellerCommonRepository {

    override suspend fun getCampaignList(): List<CampaignUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not have shop")

        val response = getCampaignListUseCase.apply {
            setRequestParams(GetCampaignListUseCase.createParams(shopId = userSession.shopId))
        }.executeOnBackground()

        return@withContext commonMapper.mapCampaignList(response)
    }

    override suspend fun getEtalaseList(): List<EtalaseUiModel> = withContext(dispatchers.io) {
        val response = getSelfEtalaseListUseCase.executeOnBackground()

        return@withContext commonMapper.mapEtalaseList(response)
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

        return@withContext commonMapper.mapProductsInCampaign(response)
    }

    companion object {
        private const val PRODUCTS_IN_CAMPAIGN_PER_PAGE = 25
    }
}
