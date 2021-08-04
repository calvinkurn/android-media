package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetDividerUiModel

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class DividerWidget(itemView: View) : AbstractViewHolder<WidgetDividerUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_divider
    }

    override fun bind(element: WidgetDividerUiModel?) {

    }
}