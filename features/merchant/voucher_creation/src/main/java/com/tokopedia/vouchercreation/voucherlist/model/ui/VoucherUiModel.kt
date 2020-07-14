package com.tokopedia.vouchercreation.voucherlist.model.ui

import android.os.Parcelable
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.create.view.enums.BenefitType
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 17/04/20
 */

@Parcelize
data class VoucherUiModel(
        val id: Int,
        val shopId: Int,
        val name: String,
        @VoucherTypeConst val type: Int,
        val typeFormatted: String,
        val image: String,
        val imageSquare: String,
        @VoucherStatusConst val status: Int,
        val statusFormatted: String,
        @BenefitType val discountType: Int,
        val discountTypeFormatted: String,
        val discountAmt: Int,
        val discountAmtFormatted: String,
        val discountAmtMax: Int,
        val discountAmtMaxFormatted: String,
        val minimumAmt: Int,
        val minimumAmtFormatted: String,
        val quota: Int,
        val remainingQuota: Int,
        val bookedQuota: Int,
        val startTime: String,
        val finishTime: String,
        val code: String,
        val galadrielVoucherId: Int,
        val galadrielCatalogId: Int,
        val createdTime: String,
        val createdBy: Int,
        val updatedTime: String,
        val updatedBy: Int,
        val isPublic: Boolean,
        val isQuotaAvailable: Boolean,
        val tnc: String
) : BaseVoucherListUiModel, Parcelable {

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun isOngoingPromo() = status == VoucherStatusConst.ONGOING
}