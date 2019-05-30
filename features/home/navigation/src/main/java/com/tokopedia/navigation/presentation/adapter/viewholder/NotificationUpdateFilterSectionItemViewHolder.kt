package com.tokopedia.navigation.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel


/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFilterSectionItemViewHolder(
        itemView: View,
        var itemSectionListener: ItemSectionListener,
        var filterType: String
)
    : AbstractViewHolder<NotificationUpdateFilterSectionItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_filter_item_section
    }

    private val category: TextView

    init {
        category = itemView.findViewById(R.id.chips_item)
    }

    override fun bind(element: NotificationUpdateFilterSectionItemViewModel) {
        category.text = element.text
        if(element.selected) {
            category.background = MethodChecker.getDrawable(category.context, R.drawable.bg_item_filter_pressed)
        } else {
            category.background = MethodChecker.getDrawable(category.context, R.drawable.bg_item_filter_neutral)
        }
        category.setOnClickListener {
            itemSectionListener.itemSectionPicked(adapterPosition, filterType)
        }
    }
}