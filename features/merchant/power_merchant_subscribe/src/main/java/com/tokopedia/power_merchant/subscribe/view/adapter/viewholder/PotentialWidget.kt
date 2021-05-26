package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.PotentialAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPotentialUiModel
import kotlinx.android.synthetic.main.widget_pm_potential.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PotentialWidget(itemView: View) : AbstractViewHolder<WidgetPotentialUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_potential
    }

    override fun bind(element: WidgetPotentialUiModel) {
        with(itemView) {
            rvPmPotential.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            rvPmPotential.adapter = PotentialAdapter(element.potentialItems)
        }
    }
}