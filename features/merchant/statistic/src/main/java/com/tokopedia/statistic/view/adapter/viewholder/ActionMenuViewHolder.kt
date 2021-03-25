package com.tokopedia.statistic.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.view.model.ActionMenuUiModel
import kotlinx.android.synthetic.main.item_stc_action_menu.view.*

/**
 * Created By @ilhamsuaib on 14/02/21
 */

class ActionMenuViewHolder(
        itemView: View,
        private val pageName: String,
        private val userId: String,
        private val onClickCallback: (menu: ActionMenuUiModel) -> Unit
) : AbstractViewHolder<ActionMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_action_menu
    }

    override fun bind(element: ActionMenuUiModel) {
        with(itemView) {
            element.iconUnify?.let {
                iconStcActionMenu.setImage(it)
            }
            tvStcActionMenu.text = element.title
            setOnClickListener {
                onClickCallback(element)
                StatisticTracker.sendActionMenuBottomSheetClickEvent(userId, pageName, element.title)
            }

            addOnImpressionListener(element.impressHolder) {
                StatisticTracker.sendActionMenuBottomSheetImpressionEvent(userId, pageName, element.title)
            }
        }
    }
}