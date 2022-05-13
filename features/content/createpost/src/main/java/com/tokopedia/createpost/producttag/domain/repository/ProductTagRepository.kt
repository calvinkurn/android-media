package com.tokopedia.createpost.producttag.domain.repository

import com.tokopedia.createpost.producttag.model.PagedGlobalSearchProductResponse
import com.tokopedia.createpost.producttag.view.uimodel.*

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
interface ProductTagRepository {

    suspend fun getLastTaggedProducts(
        authorId: String,
        authorType: String,
        cursor: String,
        limit: Int,
    ) : PagedDataUiModel<ProductUiModel>

    suspend fun getLastPurchasedProducts(
        cursor: String,
        limit: Int,
    ) : LastPurchasedProductUiModel

    suspend fun searchAceProducts(
        rows: Int,
        start: Int,
        query: String,
        shopId: String,
        userId: String,
        sort: Int,
    ) : PagedGlobalSearchProductResponse

    suspend fun searchAceShops(
        rows: Int,
        start: Int,
        query: String,
        sort: Int,
    ) : PagedDataUiModel<ShopUiModel>
}