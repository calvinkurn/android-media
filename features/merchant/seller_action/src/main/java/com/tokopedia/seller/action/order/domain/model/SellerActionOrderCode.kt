package com.tokopedia.seller.action.order.domain.model

object SellerActionOrderCode {
    // Pesanan baru
    internal const val STATUS_CODE_ORDER_CREATED = 220
    // Siap dikirim
    internal const val STATUS_CODE_ORDER_CONFIRMED = 400
    // Menunggu Pick-Up
    internal const val STATUS_CODE_ORDER_PICKUP_AVAILABLE = 450
    // Pesanan dikembalikan
    internal const val STATUS_CODE_ORDER_RETURNED = 550
    // Resi tidak valid, Pesanan dikomplain
    internal val STATUS_CODE_ORDER_PROBLEM = listOf(520, 601)
    internal val STATUS_CODE_DEFAULT = listOf(STATUS_CODE_ORDER_CREATED, STATUS_CODE_ORDER_CONFIRMED)
    internal val STATUS_CODE_ORDER_IN_DELIVERY = listOf(450, 500, 501, 520, 530, 540, 550, 600, 601)
    internal val STATUS_CODE_ORDER_FINISHED: List<Int> = listOf(690, 691, 695, 698, 699, 700, 701)
    internal val STATUS_CODE_ORDER_CANCELLED: List<Int> = listOf(0, 4, 6, 10, 11, 15)
}