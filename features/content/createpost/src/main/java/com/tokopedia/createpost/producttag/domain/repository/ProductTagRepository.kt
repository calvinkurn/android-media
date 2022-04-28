package com.tokopedia.createpost.producttag.domain.repository

import com.tokopedia.createpost.producttag.view.uimodel.LastTaggedProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

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
    ) : PagedDataUiModel<ProductUiModel>
}