package com.tokopedia.content.product.picker.seller.domain.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetProductsInCampaignUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.etalase.GetSelfEtalaseListUseCase
import com.tokopedia.content.product.picker.seller.mapper.ContentProductPickerSellerMapper
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext

/**
 * Created By : Jonathan Darwin on October 16, 2023
 */
abstract class AbstractProductPickerSellerRepository(
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val getProductsInCampaignUseCase: GetProductsInCampaignUseCase,
    private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
    private val productMapper: ContentProductPickerSellerMapper,
    private val userSession: UserSessionInterface,
) : ContentProductPickerSellerRepository {

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

    companion object {
        private const val PRODUCTS_IN_CAMPAIGN_PER_PAGE = 25

        const val PRODUCTS_IN_ETALASE_PER_PAGE = 25
        const val AUTHOR_TYPE_SELLER = 2
    }
}
