package com.tokopedia.paylater

import android.content.Context
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.data.mapper.*
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.unifycomponents.Label

object PayLaterHelper {

    fun setLabelData(context: Context, partnerLabelStatus: Label, payLaterApplicationDetail: PayLaterApplicationDetail) {
        partnerLabelStatus.visible()
        when(PayLaterApplicationStatusMapper.getApplicationStatusType(payLaterApplicationDetail)) {
            is PayLaterStatusWaiting -> {
                partnerLabelStatus.text = context.getString(R.string.payLater_status_waiting)
                partnerLabelStatus.setLabelType(Label.GENERAL_LIGHT_ORANGE)
            }
            is PayLaterStatusActive -> {
                partnerLabelStatus.text = context.getString(R.string.payLater_status_active)
                partnerLabelStatus.setLabelType(Label.GENERAL_LIGHT_BLUE)
            }
            is PayLaterStatusCancelled -> {
                partnerLabelStatus.text = context.getString(R.string.payLater_status_cancelled)
                partnerLabelStatus.setLabelType(Label.GENERAL_LIGHT_RED)
            }
            is PayLaterStatusRejected -> {
                partnerLabelStatus.text = context.getString(R.string.payLater_status_rejected)
                partnerLabelStatus.setLabelType(Label.GENERAL_LIGHT_RED)
            }
            else -> partnerLabelStatus.gone()
        }
    }
}