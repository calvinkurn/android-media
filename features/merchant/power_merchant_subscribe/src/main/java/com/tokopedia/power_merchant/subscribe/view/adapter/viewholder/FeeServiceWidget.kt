package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetDividerUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetFeeServiceUiModel

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class FeeServiceWidget(itemView: View) : AbstractViewHolder<WidgetFeeServiceUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_fee_service
    }

    override fun bind(element: WidgetFeeServiceUiModel?) {

    }
}