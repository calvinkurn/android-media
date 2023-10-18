package com.tokopedia.mvc.domain.entity.enums

enum class VoucherSort(val type: String) {
    CREATE_TIME("create_time"),
    START_TIME("voucher_start_time"),
    FINISH_TIME("voucher_finish_time"),
    VOUCHER_STATUS("voucher_status")
}
