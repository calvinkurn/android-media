package com.tokopedia.vouchercreation.detail.model

import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherHeaderUiModel(
        @VoucherStatusConst val status: Int,
        val voucherImageUrl: String,
        val startTime: String,
        val finishTime: String,
        val cancelTime: String? = null,
        val isVps: Boolean = false,
        val isSubsidy: Boolean = false,
        val packageName: String = ""
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
