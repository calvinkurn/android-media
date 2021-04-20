package com.tokopedia.vouchercreation.detail.model

import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 30/04/20
 */

data class UsageProgressUiModel(
        @VoucherTargetType val targetType: Int,
        val quota: Int,
        val confirmedQuota: Int,
        val bookedQuota: Int
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}