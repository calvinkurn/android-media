package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategorySwitcherWidgetBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import com.tokopedia.utils.view.binding.viewBinding

class SwitcherWidgetViewHolder(
    itemView: View,
    private val listener: SwitcherWidgetListener? = null
): AbstractViewHolder<SwitcherWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_switcher_widget
    }

    private var binding: ItemTokopedianowSearchCategorySwitcherWidgetBinding? by viewBinding()

    override fun bind(element: SwitcherWidgetDataView) {
        binding?.apply {
            switcherLayout.setBackgroundResource(R.drawable.tokopedianow_bg_search_category_switcher_widget)
            containerCardFifteenMinutes.setBackgroundResource(R.drawable.tokopedianow_bg_search_category_switcher_widget_card_fifteenminutes)
            containerCardTwoHours.setBackgroundResource(R.drawable.tokopedianow_bg_search_category_switcher_widget_card_twohours)
            tpDescFifteenMinutes.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_search_category_description_fifteen_minutes))
            tpDescTwoHours.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_search_category_description_two_hours))
            cardFifthteenMinutes.setOnClickListener {
                listener?.onClickFifteenMinutes()
            }
            cardTwoHours.setOnClickListener {
                listener?.onClickTwoHours()
            }
        }
    }
}