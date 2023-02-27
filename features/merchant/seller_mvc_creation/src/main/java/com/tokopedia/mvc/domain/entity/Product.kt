package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: Long,
    val isVariant: Boolean,
    val name: String,
    val picture: String,
    val preorder: Preorder,
    val price: Price,
    val sku: String,
    val status: String,
    val stock: Int,
    val txStats: TxStats,
    val warehouseCount: Int,
    val isEligible: Boolean,
    val ineligibleReason: String,
    val originalVariants: List<Variant>,
    val selectedVariantsIds: Set<Long>,
    val isSelected: Boolean,
    val enableCheckbox: Boolean,
    val isDeletable: Boolean
) : Parcelable {

    @Parcelize
    data class Preorder(val durationDays: Int) : Parcelable

    @Parcelize
    data class Price(val min: Int, val max: Int) : Parcelable

    @Parcelize
    data class TxStats(val sold: Int) : Parcelable

    @Parcelize
    data class Variant(
        val variantProductId: Long,
        val isEligible: Boolean,
        val reason: String,
        val isSelected: Boolean
    ) : Parcelable
}
