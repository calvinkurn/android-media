package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemSubMenuCategoryAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener

class DigitalItemCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(element: DigitalHomePageCategoryModel.Subtitle?, onItemBindListener: OnItemBindListener) {
        val binding = LayoutDigitalHomeCategoryItemBinding.bind(itemView)
        val layoutManager = GridLayoutManager(itemView.context, DigitalHomePageCategoryViewHolder.CATEGORY_SPAN_COUNT)
        binding.rvDigitalHomepageCategory.layoutManager = layoutManager
        binding.rvDigitalHomepageCategory.adapter = DigitalItemSubMenuCategoryAdapter(element?.submenu, onItemBindListener)
        binding.subtitle.text = element?.label ?: ""
    }

}
