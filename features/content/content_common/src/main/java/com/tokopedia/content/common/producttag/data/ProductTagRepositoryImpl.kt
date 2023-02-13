package com.tokopedia.content.common.producttag.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.domain.usecase.*
import com.tokopedia.content.common.producttag.model.PagedGlobalSearchProductResponse
import com.tokopedia.content.common.producttag.model.PagedGlobalSearchShopResponse
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.content.common.producttag.view.uimodel.mapper.ProductTagUiModelMapper
import com.tokopedia.filter.common.data.DynamicFilterModel
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
    private val getSortFilterUseCase: GetSortFilterUseCase,
    private val getSortFilterProductCountUseCase: GetSortFilterProductCountUseCase,
    private val getShopInfoByIDUseCase: GetShopInfoByIDUseCase,
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
        param: SearchParamUiModel,
    ): PagedGlobalSearchShopResponse {
        return withContext(dispatchers.io) {
            val response = feedAceSearchShopUseCase.apply {
                setRequestParams(FeedAceSearchShopUseCase.createParams(
                    param = param,
                ))
            }.executeOnBackground()

            mapper.mapSearchAceShops(response, param.start + param.rows, param)
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

    override suspend fun getSortFilter(param: SearchParamUiModel): DynamicFilterModel {
        return withContext(dispatchers.io) {
            val response = getSortFilterUseCase.apply {
                setRequestParams(GetSortFilterUseCase.createParams(
                    param = param,
                ))
            }.executeOnBackground()

            mapper.mapSortFilter(response)
        }
    }

    override suspend fun getSortFilterProductCount(param: SearchParamUiModel): String {
        return withContext(dispatchers.io) {
            val response = getSortFilterProductCountUseCase.apply {
                setRequestParams(GetSortFilterProductCountUseCase.createParams(
                    param = param,
                ))
            }.executeOnBackground()

            mapper.mapSortFilterProductCount(response)
        }
    }

    override suspend fun getShopInfoByID(shopIds: List<Long>): ShopUiModel {
        return withContext(dispatchers.io) {
            val response = getShopInfoByIDUseCase.apply {
                setRequestParams(GetShopInfoByIDUseCase.createParams(
                    shopIds = shopIds,
                ))
            }.executeOnBackground()

            mapper.mapShopInfo(response)
        }
    }
}