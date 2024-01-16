package com.tokopedia.stories.creation.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository.Companion.AUTHOR_TYPE_SELLER
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository.Companion.PRODUCTS_IN_ETALASE_PER_PAGE
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
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
    private val getStoryProductEtalaseUseCase: GetStoryProductEtalaseUseCase,
    private val setActiveProductTagUseCase: SetActiveProductTagUseCase,
    private val getStoryProductDetailsUseCase: GetStoryProductDetailsUseCase,
    private val productMapper: StoriesCreationProductMapper,
    private val userSession: UserSessionInterface,
) : ContentProductPickerSellerRepository {

    override suspend fun getProductsInEtalase(
        etalaseId: String,
        cursor: String,
        keyword: String,
        sort: SortUiModel
    ): PagedDataUiModel<ProductUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not have shop")

        val response = getStoryProductEtalaseUseCase(
            GetStoryProductEtalaseRequest.create(
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

    override suspend fun setProductTags(creationId: String, productIds: List<String>) {
        withContext(dispatchers.io) {
            setActiveProductTagUseCase(
                SetActiveProductTagRequest.create(
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
            GetStoryProductDetailsRequest.create(creationId)
        )

        return@withContext productMapper.mapProductDetails(response)
    }

    override suspend fun setPinProduct(creationId: String, product: ProductUiModel): Boolean {
        throw Exception("No Pin Product for Stories Creation")
    }
}
