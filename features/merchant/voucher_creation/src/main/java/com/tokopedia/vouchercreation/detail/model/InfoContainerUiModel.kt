package com.tokopedia.vouchercreation.detail.model

import androidx.annotation.StringRes
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 05/05/20
 */

data class InfoContainerUiModel(
        @StringRes val titleRes: Int,
        val informationList: List<SubInfoItemUiModel>,
        val dataKey: String = "",
        val hasCta: Boolean = false,
        val impressHolder: ImpressHolder = ImpressHolder()
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}