package com.tokopedia.product.manage.feature.filter.domain

import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase
import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase.Companion.PARAM_FILTER
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaResponse
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase.Companion.HIDE_NO_COUNT
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase.Companion.HIDE_SHOWCASE_GROUP
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase.Companion.IS_OWNER
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductManageFilterOptionsUseCase @Inject constructor(
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase
        ) : UseCase<FilterOptionsResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object {
        const val DEFAULT_HIDE_NO_COUNT = false
        const val DEFAULT_HIDE_SHOWCASE_GROUP = true
        private const val DEFAULT_CATEGORIES_FILTER = "seller"
        private const val RELOAD_ETALASE = true
        private const val PARAM_SHOP_ID = "shopID"

        fun createRequestParams(shopId: String, isOwner: Boolean): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_SHOP_ID, shopId)
                putBoolean(HIDE_NO_COUNT, DEFAULT_HIDE_NO_COUNT)
                putBoolean(HIDE_SHOWCASE_GROUP, DEFAULT_HIDE_SHOWCASE_GROUP)
                putBoolean(IS_OWNER, isOwner)
                putString(PARAM_FILTER, DEFAULT_CATEGORIES_FILTER)
            }
        }
    }

    override suspend fun executeOnBackground(): FilterOptionsResponse = withContext(Dispatchers.IO) {
        val productListMetaData = executeProductListMetaDataUseCaseAsync()
        val shopEtalase = executeEtalaseUseCaseAsync()
        val categories = executeCategoriesUseCaseAsync()
        return@withContext FilterOptionsResponse(productListMetaData.await(), shopEtalase.await(), categories.await())
    }

    private suspend fun executeProductListMetaDataUseCaseAsync(): Deferred<ProductListMetaResponse> {
        return withContext(Dispatchers.IO) {
            async {
                getProductListMetaUseCase.setParams(params.getString(PARAM_SHOP_ID,""))
                getProductListMetaUseCase.executeOnBackground()
            }
        }
    }

    private suspend fun executeEtalaseUseCaseAsync(): Deferred<ArrayList<ShopEtalaseModel>> {
        return withContext(Dispatchers.IO) {
            async {
                val params = GetShopEtalaseByShopUseCase.createRequestParams(
                        params.getString(PARAM_SHOP_ID,""),
                        params.getBoolean(HIDE_NO_COUNT, DEFAULT_HIDE_NO_COUNT),
                        params.getBoolean(HIDE_SHOWCASE_GROUP, DEFAULT_HIDE_SHOWCASE_GROUP),
                        params.getBoolean(IS_OWNER, false))
                getShopEtalaseByShopUseCase.run {
                    isFromCacheFirst = !RELOAD_ETALASE
                    createObservable(params).toBlocking().first()
                }
            }
        }
    }

    private suspend fun executeCategoriesUseCaseAsync(): Deferred<CategoriesResponse> {
        return withContext(Dispatchers.IO) {
            async {
                getCategoryListUseCase.setParams(params.getString(PARAM_FILTER,""))
                getCategoryListUseCase.executeOnBackground()
            }
        }
    }

}