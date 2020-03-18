package com.tokopedia.travelhomepage.homepage.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import kotlinx.android.synthetic.main.travel_homepage_category_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageCategoryListAdapter(private var list: List<TravelHomepageCategoryListModel.Category>,
                                        var travelHomepageActionListener: TravelHomepageActionListener):
        RecyclerView.Adapter<TravelHomepageCategoryListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(CategoryViewHolder.LAYOUT, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(list[position], position, travelHomepageActionListener)
    }


    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(category: TravelHomepageCategoryListModel.Category, position: Int, listener: TravelHomepageActionListener) {
            with(itemView) {
                category_image.loadImage(category.attributes.imageUrl)
                category_name.text = category.attributes.title
            }
            itemView.setOnClickListener {
                listener.onClickDynamicIcon(category, position + 1)
                listener.onItemClick(category.attributes.appUrl)
            }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_category_list_item
        }
    }
}