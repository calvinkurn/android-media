package com.tokopedia.content.product.picker.seller.domain.repository

import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
interface ContentProductPickerSellerRepository {

    suspend fun getProductsInEtalase(
        etalaseId: String,
        cursor: String,
        keyword: String,
        sort: SortUiModel,
    ): PagedDataUiModel<ProductUiModel>

    suspend fun setProductTags(creationId: String, productIds: List<String>)

    suspend fun getProductTagSummarySection(
        creationId: String,
        fetchCommission: Boolean = false,
    ): List<ProductTagSectionUiModel>

    suspend fun setPinProduct(creationId: String, product: ProductUiModel): Boolean

    companion object {
        const val PRODUCTS_IN_ETALASE_PER_PAGE = 25
        const val AUTHOR_TYPE_SELLER = 2
    }
}
