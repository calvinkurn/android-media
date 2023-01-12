package com.tokopedia.mvc.domain.entity

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherSort
import com.tokopedia.mvc.domain.entity.enums.VoucherSource
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherSubsidy
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.entity.enums.VoucherVps

data class VoucherListParam (
    @SerializedName("voucher_type")
    val voucherType: Int?,
    @SerializedName("voucher_status")
    val voucherStatus: String,
    @SerializedName("is_public")
    val isPublic: String?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("per_page")
    val perPage: Int? = 0,
    @SerializedName("sort_by")
    val sortBy: String? = null,
    @SerializedName("is_inverted")
    val isInverted: Boolean,
    @SerializedName("include_subsidy")
    val includeSubsidy: Int,
    @SerializedName("is_vps")
    val isVps: String,
    @SerializedName("voucher_name")
    val voucherName: String?,
    @SerializedName("target_buyer")
    val targetBuyer: String?,
    @SerializedName("is_lock_to_product")
    val isLockToProduct: String = "0"
) {
    companion object {
        private const val VALUE_DELIMITER = ","

        @JvmStatic
        fun createParam(
            type: PromoType? = null,
            status: List<VoucherStatus> = emptyList(),
            sort: VoucherSort? = null,
            target: List<VoucherTarget> = emptyList(),
            page: Int? = null,
            perPage: Int? = Int.ZERO,
            voucherName: String? = null,
            voucherType: List<VoucherServiceType> = emptyList(),
            targetBuyer: List<VoucherTargetBuyer> = emptyList(),
            source: List<VoucherSource> = emptyList()
        ): VoucherListParam {
            return VoucherListParam(
                voucherType = type?.id,
                voucherStatus = status.map { it.id }.joinToString(VALUE_DELIMITER),
                isPublic = target.map { it.id }.joinToString(VALUE_DELIMITER),
                page = page,
                perPage = perPage,
                sortBy = sort?.type,
                isInverted = true,
                includeSubsidy = source.mapToSubsidy(),
                isVps = if (source.any { it == VoucherSource.VPS_PROMOTION }) VoucherVps.VPS.id.toString() else "",
                voucherName = voucherName,
                targetBuyer = targetBuyer.map { it.id }.joinToString(VALUE_DELIMITER),
                isLockToProduct = voucherType.map { it.id }.joinToString(VALUE_DELIMITER)
            )
        }

        private fun List<VoucherSource>.mapToSubsidy(): Int {
            return if (any { it == VoucherSource.SELLER_BUDGET } && any { it == VoucherSource.SELLER_BUDGET }) {
                VoucherSubsidy.SELLER_AND_TOKOPEDIA.id
            } else if (any { it == VoucherSource.TOKOPEDIA_BUDGET }) {
                VoucherSubsidy.SELLER.id
            } else if (any { it == VoucherSource.TOKOPEDIA_BUDGET }) {
                VoucherSubsidy.TOKOPEDIA.id
            } else {
                VoucherSubsidy.SELLER_AND_TOKOPEDIA.id
            }
        }
    }
}
