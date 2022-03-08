package com.tokopedia.vouchercreation.product.create.domain.entity

import android.os.Parcelable
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CouponUiModel(
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
    var tnc: String,
    val productIds : List<Long>,
    val products: List<ProductId>,
    val galadrielVoucherId: Long
): Parcelable