package com.tokopedia.vouchercreation.voucherlist.domain.model

import androidx.annotation.StringDef
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst

data class VoucherListParam (
        val voucher_type: Int? = null,
        val voucher_status: String = VoucherStatus.ACTIVE,
        val is_public: String = VoucherTarget.PUBLIC,
        var page: Int? = null,
        val per_page: Int? = 10,
        val sort_by: String? = null) {

    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(VoucherStatus.ACTIVE, VoucherStatus.HISTORY)
    annotation class VoucherStatus {
        companion object {
            const val ACTIVE = "0,1,2"
            const val HISTORY = "-1,3,4"
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
            const val START_TIME = "start_time"
            const val FINISH_TIME = "finish_time"
        }
    }

    companion object {
        @JvmStatic
        fun createParam(@VoucherTypeConst type: Int?,
                        @VoucherStatus status: String,
                        @VoucherTarget target: String,
                        @VoucherSort sort: String?,
                        page: Int?) : VoucherListParam {
            return VoucherListParam(
                    voucher_type = type,
                    voucher_status = status,
                    is_public = target,
                    page = page,
                    sort_by = sort
            )
        }
    }

    fun incrementPage() {
        page?.inc()
    }

}