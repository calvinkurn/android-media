package com.tokopedia.content.product.picker.ugc.domain.repository

import com.tokopedia.content.product.picker.ugc.model.PagedGlobalSearchProductResponse
import com.tokopedia.content.product.picker.ugc.model.PagedGlobalSearchShopResponse
import com.tokopedia.content.product.picker.ugc.view.uimodel.LastPurchasedProductUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.PagedDataUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.ProductUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.QuickFilterUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.SearchParamUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.ShopUiModel
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
        shopIds: List<Long>,
    ): ShopUiModel
}
