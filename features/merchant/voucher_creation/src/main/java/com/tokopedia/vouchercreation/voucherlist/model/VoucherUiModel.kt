package com.tokopedia.vouchercreation.voucherlist.model

import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 17/04/20
 */

data class VoucherUiModel(
        val id: Int,
        val shopId: Int,
        val name: String,
        val description: String,
        val type: Int,
        val typeFormatted: String,
        val image: String,
        val imageSquare: String,
        val status: Int,
        val statusFormatted: String,
        val discountType: Int,
        val discountTypeFormatted: String,
        val discountAmt: Int,
        val discountAmtFormatted: String,
        val discountAmtMax: Int,
        val discountAmtMaxFormatted: String,
        val minimumAmt: Int,
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
        val tnc: String,
        val hyperLink: HyperlinkUiModel,
        val isOngoingStatus: Boolean
) : BaseVoucherListUiModel {

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}