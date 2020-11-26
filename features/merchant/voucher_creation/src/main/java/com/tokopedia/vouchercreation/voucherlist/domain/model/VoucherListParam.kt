package com.tokopedia.vouchercreation.voucherlist.domain.model

import androidx.annotation.StringDef
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst

data class VoucherListParam (
        @SerializedName("voucher_type")
        @Expose
        val voucherType: Int?,
        @SerializedName("voucher_status")
        @Expose
        val voucherStatus: String,
        @SerializedName("is_public")
        @Expose
        val isPublic: String?,
        @SerializedName("page")
        @Expose
        var page: Int?,
        @SerializedName("per_page")
        @Expose
        val perPage: Int? = 10,
        @SerializedName("sort_by")
        @Expose
        val sortBy: String? = null,
        @SerializedName("is_inverted")
        @Expose
        val isInverted: Boolean) {

    companion object {
        @JvmStatic
        fun createParam(@VoucherTypeConst type: Int? = null,
                        @VoucherStatus status: String,
                        targetList: List<Int>? = null,
                        @VoucherSort sort: String? = null,
                        page: Int? = null,
                        isInverted: Boolean = false) : VoucherListParam {
            return VoucherListParam(
                    voucherType = type,
                    voucherStatus = status,
                    isPublic = targetList?.joinToString(separator = ","),
                    page = page,
                    sortBy = sort,
                    isInverted = isInverted
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