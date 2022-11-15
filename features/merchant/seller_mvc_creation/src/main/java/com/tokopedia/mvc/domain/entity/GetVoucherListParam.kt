package com.tokopedia.mvc.domain.entity

import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

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
    val isInverted: Boolean,
    @SerializedName("include_subsidy")
    @Expose
    val includeSubsidy: Int,
    @SerializedName("is_vps")
    @Expose
    val isVps: String,
    @SerializedName("voucher_name")
    @Expose
    val voucherName: String?,
    @SerializedName("target_buyer")
    @Expose
    val targetBuyer: String?,
    @SerializedName("is_lock_to_product")
    @Expose
    val isLockToProduct: String = "0"
) {

    companion object {
        @JvmStatic
        fun createParam(
            @VoucherTypeConst type: Int? = null,
            @VoucherStatus status: String,
            @VoucherSort sort: String? = null,
            @VoucherTarget target: String? = null,
            page: Int? = null,
            perPage: Int? = Int.ZERO,
            voucherName: String? = null
        ) : VoucherListParam {
            return VoucherListParam(
                voucherType = type,
                voucherStatus = status,
                isPublic = target,
                page = page,
                perPage = perPage,
                sortBy = sort,
                isInverted = false,
                includeSubsidy = VoucherSubsidy.SELLER_AND_TOKOPEDIA,
                isVps = VoucherVps.ALL,
                voucherName = voucherName,
                targetBuyer = null,
                isLockToProduct = "1,0"
            )
        }
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherTypeConst.FREE_ONGKIR, VoucherTypeConst.DISCOUNT, VoucherTypeConst.CASHBACK)
annotation class VoucherTypeConst {
    companion object {
        const val FREE_ONGKIR = 1
        const val DISCOUNT = 2
        const val CASHBACK = 3
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherStatusConst.DELETED, VoucherStatusConst.PROCESSING,
    VoucherStatusConst.NOT_STARTED, VoucherStatusConst.ONGOING,
    VoucherStatusConst.ENDED, VoucherStatusConst.STOPPED)
annotation class VoucherStatusConst {
    companion object {
        const val DELETED = -1
        const val PROCESSING = 0
        const val NOT_STARTED = 1
        const val ONGOING = 2
        const val ENDED = 3
        const val STOPPED = 4
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING, VoucherStatus.HISTORY, VoucherStatus.NOT_STARTED_AND_ONGOING)
annotation class VoucherStatus {
    companion object {
        const val NOT_STARTED = "1"
        const val ONGOING = "2"
        const val HISTORY = "3,4"
        const val NOT_STARTED_AND_ONGOING = "1,2"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherTarget.PUBLIC, VoucherTarget.PRIVATE)
annotation class VoucherTarget {
    companion object {
        const val PUBLIC = "1"
        const val PRIVATE = "0"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER, VoucherTargetBuyer.NEW_BUYER, VoucherTargetBuyer.MEMBER)
annotation class VoucherTargetBuyer {
    companion object {
        const val ALL_BUYER = "0"
        const val NEW_FOLLOWER = "1"
        const val NEW_BUYER = "2"
        const val MEMBER = "3"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherSort.CREATE_TIME, VoucherSort.START_TIME, VoucherSort.FINISH_TIME, VoucherSort.VOUCHER_STATUS)
annotation class VoucherSort {
    companion object {
        const val CREATE_TIME = "create_time"
        const val START_TIME = "voucher_start_time"
        const val FINISH_TIME = "voucher_finish_time"
        const val VOUCHER_STATUS = "voucher_status"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherSubsidy.SELLER, VoucherSubsidy.TOKOPEDIA, VoucherSubsidy.SELLER_AND_TOKOPEDIA)
annotation class VoucherSubsidy {
    companion object {
        const val SELLER = 0
        const val TOKOPEDIA = 1
        const val SELLER_AND_TOKOPEDIA = 2
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(VoucherVps.ALL, VoucherVps.NON_VPS, VoucherVps.VPS)
annotation class VoucherVps {
    companion object {
        const val NON_VPS = "0"
        const val VPS = "1"
        const val ALL = "0,1"
    }
}
