package com.tokopedia.seller.action.order.domain.model

object SellerActionOrderCode {
    // Pesanan baru
    internal const val STATUS_CODE_ORDER_CREATED = 220
    // Siap dikirim
    internal const val STATUS_CODE_ORDER_CONFIRMED = 400
    internal const val STATUS_CODE_ORDER_CONFIRMED_2 = 520
    internal val STATUS_CODE_DEFAULT = listOf(STATUS_CODE_ORDER_CREATED, STATUS_CODE_ORDER_CONFIRMED, STATUS_CODE_ORDER_CONFIRMED_2)
}