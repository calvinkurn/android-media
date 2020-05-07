package com.tokopedia.vouchercreation.create.view.enums

sealed class VoucherImageType {
    data class FreeDelivery(val value: Int)
    data class Rupiah(val value: Int)
    data class Percentage(val value: Int, val percentage: Int)
}