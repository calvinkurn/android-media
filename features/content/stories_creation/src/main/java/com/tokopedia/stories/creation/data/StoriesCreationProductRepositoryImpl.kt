package com.tokopedia.stories.creation.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetProductsInCampaignUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.etalase.GetSelfEtalaseListUseCase
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel
import com.tokopedia.stories.creation.domain.usecase.GetStoryProductDetailsUseCase
import com.tokopedia.stories.creation.domain.usecase.GetStoryProductEtalaseUseCase
import com.tokopedia.stories.creation.domain.usecase.SetActiveProductTagUseCase
import com.tokopedia.stories.creation.model.GetStoryProductDetailsRequest
import com.tokopedia.stories.creation.model.GetStoryProductEtalaseRequest
import com.tokopedia.stories.creation.model.SetActiveProductTagRequest
import com.tokopedia.stories.creation.view.model.mapper.StoriesCreationProductMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class StoriesCreationProductRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val getProductsInCampaignUseCase: GetProductsInCampaignUseCase,
    private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
    private val getStoryProductEtalaseUseCase: GetStoryProductEtalaseUseCase,
    private val setActiveProductTagUseCase: SetActiveProductTagUseCase,
    private val getStoryProductDetailsUseCase: GetStoryProductDetailsUseCase,
    private val productMapper: StoriesCreationProductMapper,
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

    override suspend fun getProductsInEtalase(
        etalaseId: String,
        cursor: String,
        keyword: String,
        sort: SortUiModel
    ): PagedDataUiModel<ProductUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not has shop")

        val response = getStoryProductEtalaseUseCase(
            GetStoryProductEtalaseRequest(
                authorId = userSession.shopId,
                authorType = AUTHOR_TYPE_SELLER,
                cursor = cursor,
                limit = PRODUCTS_IN_ETALASE_PER_PAGE,
                keyword = keyword,
                sort = sort,
                etalaseId = etalaseId,
            )
        )

        return@withContext productMapper.mapProductEtalase(response)
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

    override suspend fun setProductTags(creationId: String, productIds: List<String>) {
        withContext(dispatchers.io) {
            setActiveProductTagUseCase(
                SetActiveProductTagRequest(
                    storyId = creationId,
                    productIds = productIds
                )
            )
        }
    }

    override suspend fun getProductTagSummarySection(
        creationId: String,
        fetchCommission: Boolean
    ): List<ProductTagSectionUiModel> = withContext(dispatchers.io) {
        val response = getStoryProductDetailsUseCase(
            GetStoryProductDetailsRequest(creationId)
        )

        return@withContext productMapper.mapProductDetails(response)
    }

    override suspend fun setPinProduct(creationId: String, product: ProductUiModel): Boolean {
        throw Exception("No Pin Product for Stories Creation")
    }

    companion object {
        private const val PRODUCTS_IN_ETALASE_PER_PAGE = 25
        private const val PRODUCTS_IN_CAMPAIGN_PER_PAGE = 25

        private const val AUTHOR_TYPE_SELLER = 2
    }
}
