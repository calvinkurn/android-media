package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoriesItem
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_category_level_one.view.*

class CategoryLevelOneAdapter(private val categoryList: MutableList<CategoriesItem>, private val context: Context,
                              private val listener: CategorylevelOneFragment.CategorySelectListener)
    : RecyclerView.Adapter<CategoryLevelOneAdapter.ViewHolder>() {

    val viewMap = HashMap<Int, Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category_level_one, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.categoryName.text = categoryList[position].name

        ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, categoryList[position].iconImageUrl, R.drawable.loading_page)

        holder.parent_layout.setOnClickListener {
            listener.onItemClicked(categoryList[position].id!!, position, categoryList[position].name!!, categoryList[position].applinks)
            CategoryAnalytics.createInstance().eventCategoryLevelOneClick(holder.itemView.context,categoryList[position], position)
        }
        if (categoryList[position].isSelected) {
            holder.unselected_overlay.hide()
        } else {
            holder.unselected_overlay.show()
        }

    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap.containsKey(position)) {
            viewMap[position] = true
            CategoryAnalytics.createInstance().eventCategoryLevelOneView(holder.itemView.context,categoryList[position], position)
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val parent_layout = view.parent_layout
        val categoryImage = view.category_imageview
        val categoryName = view.category_name
        val unselected_overlay = view.unselected_overlay

    }
}