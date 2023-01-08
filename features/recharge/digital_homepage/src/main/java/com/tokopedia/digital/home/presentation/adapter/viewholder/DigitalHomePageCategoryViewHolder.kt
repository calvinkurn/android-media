package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryBinding
import com.tokopedia.digital.home.presentation.adapter.DigitalItemCategoryAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.presentation.model.DigitalHomePageCategoryModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DigitalHomePageCategoryViewHolder(
    itemView: View?,
    val onItemBindListener: OnItemBindListener
) :
    AbstractViewHolder<DigitalHomePageCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageCategoryModel?) {
        itemView.let {
            val layoutManager = LinearLayoutManager(it.context)
            val binding = LayoutDigitalHomeCategoryBinding.bind(it)
            binding.rvDigitalHomepageCategory.layoutManager = layoutManager
            if (element?.isLoaded == true) {
                binding.viewRechargeHomeCategoryShimmering.root.hide()
                binding.rvDigitalHomepageCategory.show()
                binding.rvDigitalHomepageCategory.adapter =
                    DigitalItemCategoryAdapter(element.listSubtitle, onItemBindListener)
            } else {
                binding.viewRechargeHomeCategoryShimmering.root.show()
                binding.rvDigitalHomepageCategory.hide()
            }
        }

    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_category
        const val CATEGORY_SPAN_COUNT = 5
    }
}