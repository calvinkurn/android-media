package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemSubMenuCategoryAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.layout_digital_home_category_item.view.*

class DigitalItemCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(element: DigitalHomePageCategoryModel.Subtitle?, onItemBindListener: OnItemBindListener) {
        val layoutManager = GridLayoutManager(itemView.context, DigitalHomePageCategoryViewHolder.CATEGORY_SPAN_COUNT)
        itemView.rv_digital_homepage_category.layoutManager = layoutManager
        itemView.rv_digital_homepage_category.adapter = DigitalItemSubMenuCategoryAdapter(element?.submenu, onItemBindListener)
        itemView.subtitle.text = element?.label ?: ""
    }

}
