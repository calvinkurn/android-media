package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCatalogVariantDetail
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductUiModel(
        val id: String = "",
        val name: String = "",
        val description: String = "",
        val imageURL: String = "",
        val price: Double = 0.0,
        val priceFmt: String = "",
        val slashPrice: Double = 0.0,
        val slashPriceFmt: String = "",
        val isOutOfStock: Boolean = false,
        val isShopClosed: Boolean = false,
        val isAtc: Boolean = false,
        val orderNote: String = "",
        val orderDetail: OrderDetail = OrderDetail(),
        val variants: List<TokoFoodCatalogVariantDetail> = listOf()
) : Parcelable {
    @IgnoredOnParcel
    val isSlashPriceVisible = slashPrice >= 0.0
    @IgnoredOnParcel
    var isOrderDetailLayoutVisible = orderDetail.qty.isMoreThanZero() && variants.isEmpty()
    @IgnoredOnParcel
    val isCustomizable = variants.isEmpty()
}