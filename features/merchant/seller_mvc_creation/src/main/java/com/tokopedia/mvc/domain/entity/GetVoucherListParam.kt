package com.tokopedia.mvc.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherSort
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherSubsidy
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherVps

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
            type: PromoType? = null,
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
                isLockToProduct = listOf(
                    VoucherServiceType.SHOP_VOUCHER.type,
                    VoucherServiceType.PRODUCT_VOUCHER.type).joinToString(VALUE_DELIMITER)
            )
        }
    }
}
