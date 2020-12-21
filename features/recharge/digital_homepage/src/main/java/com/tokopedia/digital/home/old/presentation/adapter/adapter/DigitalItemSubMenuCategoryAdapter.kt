package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_digital_home_category_item_submenu.view.*

class DigitalItemSubMenuCategoryAdapter(val submenu: List<DigitalHomePageCategoryModel.Submenu>?, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemSubMenuCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSubmenuCategoryViewHolder, position: Int) {
        viewHolder.bind(submenu?.get(position), onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemSubmenuCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_category_item_submenu, parent, false)
        return DigitalItemSubmenuCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return submenu?.size?:0
    }

    class DigitalItemSubmenuCategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: DigitalHomePageCategoryModel.Submenu?, onItemBindListener: OnItemBindListener) {
            itemView.category_image.loadImage(element?.icon?:"")
            itemView.category_name.text = element?.label
            itemView.setOnClickListener {
                onItemBindListener.onCategoryItemClicked(element, adapterPosition +1)
            }
        }

    }
}
