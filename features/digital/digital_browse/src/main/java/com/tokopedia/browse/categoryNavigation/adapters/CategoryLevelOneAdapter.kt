package com.tokopedia.browse.categoryNavigation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_category_level_one.view.*

class CategoryLevelOneAdapter(private val categoryList: MutableList<com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoriesItem>,
                              private val listener: CategorylevelOneFragment.CategorySelectListener,
                              private val trackingQueue: TrackingQueue?)
    : RecyclerView.Adapter<CategoryLevelOneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category_level_one, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.parentLayout.setOnClickListener {
            listener.onItemClicked(categoryList[position].id
                    ?: "", position, categoryList[position].name
                    ?: "", categoryList[position].applinks)

        }
        if (categoryList[position].isSelected) {
            ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, categoryList[position].iconImageUrl, R.drawable.loading_page)
            holder.categoryName.text = categoryList[position].name
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, R.color.white))

        } else {
            ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, categoryList[position].iconImageUrlGray, R.drawable.loading_page)
            holder.categoryName.text = categoryList[position].name
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, R.color.category_background))
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parentLayout: ConstraintLayout = view.parent_layout
        val categoryImage: ImageView = view.category_imageview
        val categoryName: TextView = view.category_name
    }
}