package com.tokopedia.vouchercreation.voucherlist.domain.model

import androidx.annotation.StringDef
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst

data class VoucherListParam (
        val voucher_type: Int?,
        val voucher_status: String,
        val is_public: String?,
        var page: Int?,
        val per_page: Int? = 10,
        val sort_by: String? = null,
        val is_inverted: Boolean) {

    companion object {
        @JvmStatic
        fun createParam(@VoucherTypeConst type: Int? = null,
                        @VoucherStatus status: String,
                        targetList: List<Int>? = null,
                        @VoucherSort sort: String? = null,
                        page: Int? = null,
                        isInverted: Boolean = false) : VoucherListParam {
            return VoucherListParam(
                    voucher_type = type,
                    voucher_status = status,
                    is_public = targetList?.joinToString(separator = ","),
                    page = page,
                    sort_by = sort,
                    is_inverted = isInverted
            )
        }
    }

    fun incrementPage() {
        page?.inc()
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING, VoucherStatus.HISTORY)
annotation class VoucherStatus {
    companion object {
        const val NOT_STARTED = "1"
        const val ONGOING = "2"
        const val HISTORY = "3,4"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherTarget.PUBLIC, VoucherTarget.PRIVATE)
annotation class VoucherTarget {
    companion object {
        const val PUBLIC = "0"
        const val PRIVATE = "1"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherSort.CREATE_TIME, VoucherSort.START_TIME, VoucherSort.FINISH_TIME)
annotation class VoucherSort {
    companion object {
        const val CREATE_TIME = "create_time"
        const val START_TIME = "voucher_start_time"
        const val FINISH_TIME = "voucher_finish_time"
    }
}