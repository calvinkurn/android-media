package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryItemBinding
import com.tokopedia.digital.home.presentation.adapter.DigitalItemSubMenuCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.presentation.model.DigitalHomePageCategoryModel

class DigitalItemCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        element: DigitalHomePageCategoryModel.Subtitle?,
        onItemBindListener: OnItemBindListener
    ) {
        val binding = LayoutDigitalHomeCategoryItemBinding.bind(itemView)
        val layoutManager = GridLayoutManager(
            itemView.context,
            DigitalHomePageCategoryViewHolder.CATEGORY_SPAN_COUNT
        )
        binding.rvDigitalHomepageCategory.layoutManager = layoutManager
        binding.rvDigitalHomepageCategory.adapter =
            DigitalItemSubMenuCategoryAdapter(element?.submenu, onItemBindListener)
        binding.subtitle.text = element?.label ?: ""
    }

}