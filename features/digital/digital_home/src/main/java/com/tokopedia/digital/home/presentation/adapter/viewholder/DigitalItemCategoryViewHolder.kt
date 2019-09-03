package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.presentation.adapter.adapter.DigitalItemSubMenuCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.layout_digital_home_category_item.view.*
import kotlinx.android.synthetic.main.layout_digital_home_category_item.view.category_recycler_view

class DigitalItemCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(element: DigitalHomePageCategoryModel.Subtitle?, onItemBindListener: OnItemBindListener) {
        val layoutManager = GridLayoutManager(itemView.context, DigitalHomePageCategoryViewHolder.CATEGORY_SPAN_COUNT)
        itemView?.category_recycler_view.layoutManager = layoutManager
        itemView?.category_recycler_view.adapter = DigitalItemSubMenuCategoryAdapter(element?.submenu, onItemBindListener)
        itemView.subtitle.text = element?.label
    }

}
