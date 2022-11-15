package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
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
    val stats: Stats,
    val status: String,
    val stock: Int,
    val txStats: TxStats,
    val warehouse: List<Warehouse>,
    val warehouseCount: Int,
    val isEligible: Boolean,
    val ineligibleReason: String,
    val originalVariants: List<Variant>,
    val modifiedVariants: List<Variant>,
    val isSelected: Boolean,
    val enableCheckbox: Boolean
) : DelegateAdapterItem, Parcelable {

    @Parcelize
    data class Preorder(val durationDays: Int) : Parcelable

    @Parcelize
    data class Price(val min: Int, val max: Int) : Parcelable

    @Parcelize
    data class Stats(
        val countReview: Int,
        val countTalk: Int,
        val countView: Int
    ) : Parcelable

    @Parcelize
    data class TxStats(val sold: Int) : Parcelable

    @Parcelize
    data class Warehouse(val id: Long) : Parcelable

    @Parcelize
    data class Variant(
        val variantProductId: Long,
        val productName: String,
        val price: Long,
        val stock: Int,
        val isEligible: Boolean,
        val reason: String,
        val isSelected: Boolean,
        val imageUrl: String
    ) : Parcelable

    override fun id() = id
}
