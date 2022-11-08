package com.tokopedia.mvc.domain.entity

data class Product(
    val id: Long,
    val isVariant: Boolean,
    val name: String,
    val pictures: List<Picture>,
    val preorder: Preorder,
    val price: Price,
    val sku: String,
    val stats: Stats,
    val status: String,
    val stock: Int,
    val txStats: TxStats,
    val warehouse: List<Warehouse>,
    val warehouseCount: Int
) {
    data class Picture(val urlThumbnail: String)

    data class Preorder(val durationDays: Int)

    data class Price(val min: Int, val max: Int)

    data class Stats(
        val countReview: Int,
        val countTalk: Int,
        val countView: Int
    )

    data class TxStats(val sold: Int)

    data class Warehouse(val id: Long)
}
