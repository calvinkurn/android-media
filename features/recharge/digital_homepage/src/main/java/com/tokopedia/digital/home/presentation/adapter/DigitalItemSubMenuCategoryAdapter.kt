package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryItemSubmenuBinding
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.presentation.model.DigitalHomePageCategoryModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.loadImage

class DigitalItemSubMenuCategoryAdapter(
    val submenu: List<DigitalHomePageCategoryModel.Submenu>?,
    val onItemBindListener: OnItemBindListener
) : RecyclerView.Adapter<DigitalItemSubMenuCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSubmenuCategoryViewHolder, position: Int) {
        viewHolder.bind(submenu?.get(position), onItemBindListener)
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
        return submenu?.size ?: Int.ZERO
    }

    class DigitalItemSubmenuCategoryViewHolder(val binding: LayoutDigitalHomeCategoryItemSubmenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            element: DigitalHomePageCategoryModel.Submenu?,
            onItemBindListener: OnItemBindListener
        ) {
            binding.categoryImage.loadImage(element?.icon ?: "")
            binding.categoryName.text = element?.label
            itemView.setOnClickListener {
                onItemBindListener.onCategoryItemClicked(element, adapterPosition + Int.ONE)
            }
        }

    }
}
