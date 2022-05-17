package com.tokopedia.createpost.producttag.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.domain.usecase.*
import com.tokopedia.createpost.producttag.model.PagedGlobalSearchProductResponse
import com.tokopedia.createpost.producttag.view.uimodel.*
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
    private val feedAceSearchShopUseCase: FeedAceSearchShopUseCase,
    private val feedQuickFilterUseCase: FeedQuickFilterUseCase,
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
        param: SearchParamUiModel,
    ): PagedGlobalSearchProductResponse {
        return withContext(dispatchers.io) {
            val response = feedAceSearchProductUseCase.apply {
                setRequestParams(FeedAceSearchProductUseCase.createParams(
                    param = param,
                ))
            }.executeOnBackground()

            mapper.mapSearchAceProducts(response, param.start + param.rows)
        }
    }

    override suspend fun searchAceShops(
        rows: Int,
        start: Int,
        query: String,
        sort: Int
    ): PagedDataUiModel<ShopUiModel> {
        return withContext(dispatchers.io) {
            val response = feedAceSearchShopUseCase.apply {
                setRequestParams(FeedAceSearchShopUseCase.createParams(
                    rows = rows,
                    start = start,
                    query = query,
                    sort = sort,
                ))
            }.executeOnBackground()

            mapper.mapSearchAceShops(response, start + rows)
        }
    }

    override suspend fun getQuickFilter(
        query: String,
        extraParams: String
    ): List<QuickFilterUiModel> {
        return withContext(dispatchers.io) {
            val response = feedQuickFilterUseCase.apply {
                setRequestParams(FeedQuickFilterUseCase.createParams(
                    query = query,
                    extraParams = extraParams,
                ))
            }.executeOnBackground()

            mapper.mapQuickFilter(response)
        }
    }
}