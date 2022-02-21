package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryItemSubmenuBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.loadImage

class RechargeItemCategoryAdapter(
    var items: List<RechargeHomepageSections.Item>,
    val listener: RechargeHomepageItemListener
) : RecyclerView.Adapter<RechargeItemCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSubmenuCategoryViewHolder, position: Int) {
        viewHolder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): DigitalItemSubmenuCategoryViewHolder {
        val view = LayoutDigitalHomeCategoryItemSubmenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigitalItemSubmenuCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemSubmenuCategoryViewHolder(val binding: LayoutDigitalHomeCategoryItemSubmenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            element: RechargeHomepageSections.Item,
            onItemBindListener: RechargeHomepageItemListener
        ) {
            with(binding) {
                binding.categoryImage.loadImage(element.mediaUrl)
                binding.categoryName.text = element.title
                root.setOnClickListener {
                    onItemBindListener.onRechargeSectionItemClicked(element)
                }
            }

        }

    }
}
