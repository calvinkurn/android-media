package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Voucher(
    val id: Int,
    val name: String,
    val type: Int,
    val typeFormatted: String,
    val image: String,
    val imageSquare: String,
    val imagePortrait: String,
    val status: VoucherStatus,
    val discountUsingPercent: Boolean,
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
    val isLockToProduct: Boolean,
    val showNewBc: Boolean = false,
    val isFreeIconVisible: Boolean = false,
    val isVps: Boolean = false,
    val totalChild: Int,
    val packageName: String = "",
    val isSubsidy: Boolean = false,
    val tnc: String = "",
    val targetBuyer: VoucherTargetBuyer
): Parcelable
