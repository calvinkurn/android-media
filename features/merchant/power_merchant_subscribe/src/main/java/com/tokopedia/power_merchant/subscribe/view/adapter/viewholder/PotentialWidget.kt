package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PotentialUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PotentialWidget(itemView: View) : AbstractViewHolder<PotentialUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_potential
    }

    override fun bind(element: PotentialUiModel) {

    }
}