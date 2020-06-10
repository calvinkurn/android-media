package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.layout_digital_home_category.view.*

class RechargeHomepageCategoryViewHolder(itemView: View, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<RechargeHomepageSections.Section>(itemView) {

    override fun bind(element: RechargeHomepageSections.Section) {
        with (itemView) {
            val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
            category_recycler_view.layoutManager = layoutManager
            category_recycler_view.adapter = RechargeItemCategoryAdapter(element.items, onItemBindListener)
            title.text = element.title
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_category
        const val CATEGORY_SPAN_COUNT = 5
    }
}