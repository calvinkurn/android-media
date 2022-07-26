package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import android.content.Context
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.ChooseProductConstant
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignValidatedProductListResponse
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.SelectedProductModel

object ReserveProductMapper {

    private const val ADD_PRODUCT_DEFAULT_VALUE = 0L
    private const val TEASER_POS_DEFAULT_VALUE = 0
    private const val TEASER_ACTIVE_DEFAULT_VALUE = false
    private const val SELECTED_DISABLED_REASON_REMOTE_WORDING = "Produk sudah dipilih sebelumnya."
    private const val ERROR_EXCEED_PRODUCT_LIMIT = "can't reserve product. remaining allowed product to reserve:"

    fun mapFromProduct(product: GetSellerCampaignValidatedProductListResponse.Product) =
        ReserveProductModel (
            productId = product.productId,
            productName = product.productName,
            imageUrl = product.pictures.firstOrNull {
                it.urlThumbnail.isNotBlank()
            }?.urlThumbnail.orEmpty(),
            sku = product.sku,
            price = product.price,
            variant = product.variantChildsIds,
            stock = product.stock,
            disabled = product.disabled,
            disabledReason = product.disabledReason,
            isSelected = product.disabledReason == SELECTED_DISABLED_REASON_REMOTE_WORDING
        )

    fun mapToProductData(product: SelectedProductModel) = DoSellerCampaignProductSubmissionRequest.ProductData(
        productId = product.productId.toLongOrNull().orZero(),
        customStock = ADD_PRODUCT_DEFAULT_VALUE,
        finalPrice = ADD_PRODUCT_DEFAULT_VALUE,
        teaser = DoSellerCampaignProductSubmissionRequest.ProductData.Teaser(
            active = TEASER_ACTIVE_DEFAULT_VALUE, position = TEASER_POS_DEFAULT_VALUE),
        warehouses = emptyList(),
        maxOrder = ADD_PRODUCT_DEFAULT_VALUE.toInt()
    )

    fun mapFromProductList(
        productList: List<GetSellerCampaignValidatedProductListResponse.Product>
    ) = productList.map { mapFromProduct(it) }

    fun mapToProductDataList(reserveProductList: List<SelectedProductModel>?) = reserveProductList
        ?.filter { it.parentProductId == null && !it.isProductPreviouslySubmitted } // filter only parent product and not submitted before
        ?.map { mapToProductData(it) }
        .orEmpty()

    fun canReserveProduct(selectedItem: List<SelectedProductModel>, previousSelectedProductCount: Int): Boolean {
        return selectedItem.filter { !it.hasChild && !it.isProductPreviouslySubmitted }.run {
            val actualSize = size + previousSelectedProductCount
            actualSize.isMoreThanZero() && actualSize < ChooseProductConstant.PRODUCT_SELECTION_MAX
        }
    }

    fun hasVariant(selectedItem: List<SelectedProductModel>): Boolean {
        return selectedItem.any { it.parentProductId != null }
    }

    fun mapErrorMessage(context: Context, errorMessage: String): String {
        return when {
            errorMessage.startsWith(ERROR_EXCEED_PRODUCT_LIMIT) ->
                context.getString(R.string.chooseproduct_error_exceed_limit)
            else -> errorMessage
        }
    }
}
