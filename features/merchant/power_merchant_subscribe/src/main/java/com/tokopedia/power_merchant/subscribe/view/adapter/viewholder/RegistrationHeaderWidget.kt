package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationHeaderUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationHeaderWidget(itemView: View) : AbstractViewHolder<RegistrationHeaderUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_registration_header
    }

    override fun bind(element: RegistrationHeaderUiModel) {
        with(itemView) {

        }
    }
}