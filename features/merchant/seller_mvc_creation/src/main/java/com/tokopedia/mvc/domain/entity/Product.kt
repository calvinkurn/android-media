package com.tokopedia.mvc.domain.entity

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem

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
    val warehouseCount: Int
) : DelegateAdapterItem {

    data class Preorder(val durationDays: Int)

    data class Price(val min: Int, val max: Int)

    data class Stats(
        val countReview: Int,
        val countTalk: Int,
        val countView: Int
    )

    data class TxStats(val sold: Int)

    data class Warehouse(val id: Long)

    override fun id() = id
}
