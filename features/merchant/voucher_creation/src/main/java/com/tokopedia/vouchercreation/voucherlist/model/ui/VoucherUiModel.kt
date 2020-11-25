package com.tokopedia.vouchercreation.voucherlist.model.ui

import android.os.Parcelable
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 17/04/20
 */

@Parcelize
data class VoucherUiModel(
        val id: Int,
        val name: String,
        @VoucherTypeConst val type: Int,
        val typeFormatted: String,
        val image: String,
        val imageSquare: String,
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
        val isPublic: Boolean
) : BaseVoucherListUiModel, Parcelable {

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun isOngoingPromo() = status == VoucherStatusConst.ONGOING
}