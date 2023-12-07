package com.tokopedia.power_merchant.subscribe.view.model

import androidx.annotation.StringRes
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 04/03/21
 */

data class WidgetCancelDeactivationSubmissionUiModel(
        val expiredTime: String,
        val deactivatedStatusName: String,
        @StringRes val messageRes: Int
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
