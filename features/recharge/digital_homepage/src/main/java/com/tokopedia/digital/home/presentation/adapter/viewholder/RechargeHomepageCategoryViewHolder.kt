package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageDynamicIconsModel
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.layout_digital_home_category_item.view.*

class RechargeHomepageCategoryViewHolder(itemView: View, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<RechargeHomepageDynamicIconsModel>(itemView) {

    override fun bind(element: RechargeHomepageDynamicIconsModel?) {
        fun bind(element: RechargeHomepageDynamicIconsModel) {
            val layoutManager = GridLayoutManager(itemView.context, DigitalHomePageCategoryViewHolder.CATEGORY_SPAN_COUNT)
            itemView.category_recycler_view.layoutManager = layoutManager
            itemView.category_recycler_view.adapter = RechargeItemCategoryAdapter(element.items, onItemBindListener)
            itemView.subtitle.text = element.title
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_category
        const val CATEGORY_SPAN_COUNT = 5
    }
}