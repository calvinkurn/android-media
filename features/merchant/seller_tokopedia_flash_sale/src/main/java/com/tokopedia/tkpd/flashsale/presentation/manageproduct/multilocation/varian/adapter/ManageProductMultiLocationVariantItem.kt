package com.tokopedia.tkpd.flashsale.presentation.manageproduct.multilocation.varian.adapter

import android.os.Parcelable
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import kotlinx.parcelize.Parcelize

data class ManageProductMultiLocationVariantItem(
    var isToggleOn: Boolean,
    val name: String,
    val priceInWarehouse : String,
    val priceInStore: Price,
    val warehouseId: Long,
    val stock: Long,
    val productCriteria : ProductCriteria,
): DelegateAdapterItem {
    override fun id() = warehouseId

    @Parcelize
    data class Price(
        val lowerPrice: String,
        var price: String,
        val upperPrice: String
    ) : Parcelable

    @Parcelize
    data class ProductCriteria(
        val maxCustomStock: Int,
        val minCustomStock: Int,
        val maxDiscount: Long,
        val minDiscount: Long,
        val minFinalPrice: Long,
        val maxFinalPrice: Long
        ) : Parcelable

    object BundleConstant {
        fun ReservedProduct.Product.ProductCriteria.toProductCriteriaInWarehouse() : ProductCriteria{
            return ProductCriteria(
                maxCustomStock = this.maxCustomStock,
                minCustomStock = this.minCustomStock,
                maxDiscount = this.maxDiscount,
                minDiscount = this.minDiscount,
                minFinalPrice = this.minFinalPrice,
                maxFinalPrice = this.maxFinalPrice
            )
        }
    }
}