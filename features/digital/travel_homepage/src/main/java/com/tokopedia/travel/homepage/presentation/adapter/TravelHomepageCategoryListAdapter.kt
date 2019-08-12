package com.tokopedia.travel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageCategoryListModel
import kotlinx.android.synthetic.main.travel_homepage_category_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageCategoryListAdapter(private var list: List<TravelHomepageCategoryListModel.Category>,
                                        var listener: CategoryViewHolder.OnCategoryClickListener?):
        RecyclerView.Adapter<TravelHomepageCategoryListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(CategoryViewHolder.LAYOUT, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(list.get(position), position, listener)
    }


    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(category: TravelHomepageCategoryListModel.Category, position: Int, listener: OnCategoryClickListener?) {
            with(itemView) {
                category_image.loadImage(category.attributes.imageUrl)
                category_name.text = category.product
            }
            if (listener != null) itemView.setOnClickListener { listener.onCategoryClick(category, position) }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_category_list_item
        }

        interface OnCategoryClickListener {
            fun onCategoryClick(category: TravelHomepageCategoryListModel.Category, position: Int)
        }
    }
}