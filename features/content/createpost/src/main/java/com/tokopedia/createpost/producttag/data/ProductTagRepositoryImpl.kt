package com.tokopedia.createpost.producttag.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.domain.usecase.FeedAceSearchProductUseCase
import com.tokopedia.createpost.producttag.domain.usecase.GetFeedLastPurchaseProductUseCase
import com.tokopedia.createpost.producttag.domain.usecase.GetFeedLastTaggedProductUseCase
import com.tokopedia.createpost.producttag.view.uimodel.LastPurchasedProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.mapper.ProductTagUiModelMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagRepositoryImpl @Inject constructor(
    private val getFeedLastTaggedProductUseCase: GetFeedLastTaggedProductUseCase,
    private val getFeedLastPurchaseProductUseCase: GetFeedLastPurchaseProductUseCase,
    private val feedAceSearchProductUseCase: FeedAceSearchProductUseCase,
    private val mapper: ProductTagUiModelMapper,
    private val dispatchers: CoroutineDispatchers,
) : ProductTagRepository {

    override suspend fun getLastTaggedProducts(
        authorId: String,
        authorType: String,
        cursor: String,
        limit: Int
    ): PagedDataUiModel<ProductUiModel> {
        return withContext(dispatchers.io) {
            val response = getFeedLastTaggedProductUseCase.apply {
                setRequestParams(GetFeedLastTaggedProductUseCase.createParams(
                    authorId = authorId,
                    authorType = authorType,
                    cursor = cursor,
                    limit = limit,
                ))
            }.executeOnBackground()

            mapper.mapLastTaggedProduct(response)
        }
    }

    override suspend fun getLastPurchasedProducts(
        cursor: String,
        limit: Int
    ): LastPurchasedProductUiModel {
        return withContext(dispatchers.io) {
            val response = getFeedLastPurchaseProductUseCase.apply {
                setRequestParams(GetFeedLastPurchaseProductUseCase.createParams(
                    cursor = cursor,
                    limit = limit,
                ))
            }.executeOnBackground()

            mapper.mapLastPurchasedProduct(response)
        }
    }

    override suspend fun searchAceProducts(
        rows: Int,
        start: Int,
        query: String,
        shopId: String,
        userId: String,
        sort: Int
    ): PagedDataUiModel<ProductUiModel> {
        return withContext(dispatchers.io) {
            val response = feedAceSearchProductUseCase.apply {
                setRequestParams(FeedAceSearchProductUseCase.createParams(
                    rows = rows,
                    start = start,
                    shopId = shopId,
                    userId = userId,
                    query = query,
                    sort = sort,
                ))
            }.executeOnBackground()

            mapper.mapSearchAceProducts(response, start + rows)
        }
    }
}