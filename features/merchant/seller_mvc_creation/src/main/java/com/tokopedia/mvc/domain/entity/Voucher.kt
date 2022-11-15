package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Voucher(
        val id: Int,
        val name: String,
        @VoucherTypeConst val type: Int,
        val typeFormatted: String,
        val image: String,
        val imageSquare: String,
        val imagePortrait: String,
        @VoucherStatusConst val status: Int,
        val discountTypeFormatted: String,
        val discountAmt: Int,
        val discountAmtFormatted: String,
        val discountAmtMax: Int,
        val minimumAmt: Int,
        val quota: Int,
        val confirmedQuota: Int,
        val bookedQuota: Int,
        val startTime: String,
        val finishTime: String,
        val code: String,
        val createdTime: String,
        val updatedTime: String,
        val isPublic: Boolean,
        var showNewBc: Boolean = false,
        var isFreeIconVisible: Boolean = false,
        var isVps: Boolean = false,
        var packageName: String = "",
        var isSubsidy: Boolean = false,
        var tnc: String = ""
): Parcelable
