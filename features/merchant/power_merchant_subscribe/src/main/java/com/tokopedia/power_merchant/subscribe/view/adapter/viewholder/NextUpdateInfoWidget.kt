package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetNextUpdateUiModel
import kotlinx.android.synthetic.main.widget_next_update_info.view.*

/**
 * Created By @ilhamsuaib on 19/03/21
 */

class NextUpdateInfoWidget(itemView: View) : BaseViewHolder<WidgetNextUpdateUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_next_update_info
    }

    override fun bind(element: WidgetNextUpdateUiModel) {
        with(itemView) {
            tvPmNextUpdateDescription.text = context.getString(R.string.pm_next_update_pm_pro_info, element.pmProThreshold).parseAsHtml()
            tvPmNextUpdateDateInfo.text = context.getString(R.string.pm_label_next_three_months_pm_grade_update, element.nextQuarterlyRefreshDate)
        }
    }
}