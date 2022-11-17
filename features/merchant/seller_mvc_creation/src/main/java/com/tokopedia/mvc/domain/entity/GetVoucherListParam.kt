package com.tokopedia.mvc.domain.entity

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
    val perPage: Int? = 0,
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
        private const val VALUE_DELIMITER = ","

        @JvmStatic
        fun createParam(
            type: VoucherType? = null,
            status: List<VoucherStatus> = emptyList(),
            sort: VoucherSort? = null,
            target: VoucherTarget? = null,
            page: Int? = null,
            perPage: Int? = Int.ZERO,
            voucherName: String? = null
        ): VoucherListParam {
            return VoucherListParam(
                voucherType = type?.type,
                voucherStatus = status.map { it.type }.joinToString(VALUE_DELIMITER),
                isPublic = target?.type?.toString(),
                page = page,
                perPage = perPage,
                sortBy = sort?.type,
                isInverted = false,
                includeSubsidy = VoucherSubsidy.SELLER_AND_TOKOPEDIA.type,
                isVps = listOf(VoucherVps.VPS, VoucherVps.NON_VPS).joinToString(VALUE_DELIMITER),
                voucherName = voucherName,
                targetBuyer = null,
                isLockToProduct = listOf(VoucherServiceType.SHOP_VOUCHER.type,
                    VoucherServiceType.PRODUCT_VOUCHER.type).joinToString(VALUE_DELIMITER)
            )
        }
    }
}

enum class VoucherServiceType(val type: Int) {
    SHOP_VOUCHER(0),
    PRODUCT_VOUCHER(1)
}

enum class VoucherType(val type: Int) {
    FREE_ONGKIR(1),
    DISCOUNT(2),
    CASHBACK(3)
}

enum class VoucherStatus(val type: Int) {
    DELETED(-1),
    PROCESSING(0),
    NOT_STARTED(1),
    ONGOING(2),
    ENDED(3),
    STOPPED(4)
}

enum class VoucherTarget(val type: Int) {
    PUBLIC(1),
    PRIVATE(0)
}

enum class VoucherTargetBuyer(val type: Int) {
    ALL_BUYER(0),
    NEW_FOLLOWER(1),
    NEW_BUYER(2),
    MEMBER(3)
}

enum class VoucherSort(val type: String) {
    CREATE_TIME("create_time"),
    START_TIME("voucher_start_time"),
    FINISH_TIME("voucher_finish_time"),
    VOUCHER_STATUS("voucher_status")
}

enum class VoucherSubsidy(val type: Int) {
    SELLER(0),
    TOKOPEDIA(1),
    SELLER_AND_TOKOPEDIA(2)
}

enum class VoucherVps(val type: Int) {
    NON_VPS(0),
    VPS(1)
}
