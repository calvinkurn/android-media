package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.ItemCurrentStatusPMUiModel
import kotlinx.android.synthetic.main.item_current_status_power_merchant.view.*

class ItemCurrentStatusPMViewHolder(view: View) : AbstractViewHolder<ItemCurrentStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_current_status_power_merchant
    }

    override fun bind(element: ItemCurrentStatusPMUiModel?) {
        with(itemView) {
            tv_pm_reputation_value?.text = element?.statusPowerMerchant.orEmpty()
            tv_update_date_potential_pm?.text = getString(R.string.next_update_date_pm_status).orEmpty()
            tv_desc_potential_pm?.text = MethodChecker.fromHtml(String.format(
                    getString(R.string.desc_pm_status),
                    element?.statusPotentialPM.orEmpty(),
                    element?.gradePotentialPM.orEmpty()))
        }
    }
}