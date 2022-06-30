package com.tokopedia.createpost.producttag.domain.repository

import com.tokopedia.createpost.producttag.model.PagedGlobalSearchProductResponse
import com.tokopedia.createpost.producttag.model.PagedGlobalSearchShopResponse
import com.tokopedia.createpost.producttag.view.uimodel.*
import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
interface ProductTagRepository {

    suspend fun getLastTaggedProducts(
        authorId: String,
        authorType: String,
        cursor: String,
        limit: Int,
    ): PagedDataUiModel<ProductUiModel>

    suspend fun getLastPurchasedProducts(
        cursor: String,
        limit: Int,
    ): LastPurchasedProductUiModel

    suspend fun searchAceProducts(
        param: SearchParamUiModel,
    ): PagedGlobalSearchProductResponse

    suspend fun searchAceShops(
        param: SearchParamUiModel,
    ): PagedGlobalSearchShopResponse

    suspend fun getQuickFilter(
        query: String,
        extraParams: String,
    ): List<QuickFilterUiModel>

    suspend fun getSortFilter(
        param: SearchParamUiModel,
    ): DynamicFilterModel

    suspend fun getSortFilterProductCount(
        param: SearchParamUiModel,
    ): String

    suspend fun getShopInfoByID(
        shopIds: List<Int>,
    ): ShopUiModel
}