package com.tokopedia.browse.categoryNavigation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics.Companion.categoryAnalytics
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_category_level_one.view.*
import kotlinx.android.synthetic.main.item_shimmer_level_one.view.*

class CategoryLevelOneAdapter(private val categoryList: MutableList<com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoriesItem>,
                              private val listener: CategorylevelOneFragment.CategorySelectListener,
                              private val trackingQueue: TrackingQueue?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categoryItem = 1
    private val viewMap = HashMap<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            categoryItem -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_level_one, parent, false)
                CategoryViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shimmer_level_one, parent, false)
                ShimmerViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            categoryItem -> {
                initCategoryViewHolder(holder as CategoryViewHolder, position)
            }
            else -> {
                initShimmerViewHolder(holder as ShimmerViewHolder, position)
            }
        }
    }

    private fun initShimmerViewHolder(shimmerViewHolder: ShimmerViewHolder, position: Int) {
        if (position == 0) {
            shimmerViewHolder.shimmerParent.setBackgroundColor(MethodChecker.getColor(shimmerViewHolder.itemView.context, R.color.white))
        } else {
            shimmerViewHolder.shimmerParent.setBackgroundColor(MethodChecker.getColor(shimmerViewHolder.itemView.context, R.color.unselected_background))
        }
    }

    private fun initCategoryViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.parentLayout.setOnClickListener {
            listener.onItemClicked(categoryList[position].id
                    ?: "", position, categoryList[position].name
                    ?: "", categoryList[position].applinks)

            categoryAnalytics.eventSideBarCategoryClick(categoryList[position], position)
        }
        if (categoryList[position].isSelected) {
            ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, categoryList[position].iconImageUrl, R.drawable.category_ic_broken_image)
            holder.categoryName.text = categoryList[position].name
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, R.color.white))

        } else {
            ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, categoryList[position].iconImageUrlGray, R.drawable.category_ic_broken_image)
            holder.categoryName.text = categoryList[position].name
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, R.color.category_background))
        }
    }


    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return categoryList[position].type
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parentLayout: ConstraintLayout = view.parent_layout
        val categoryImage: ImageView = view.category_imageview
        val categoryName: TextView = view.category_name
    }

    class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shimmerParent: LinearLayout = view.shimmer_parent
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap.containsKey(position)) {
            viewMap[position] = true
            trackingQueue?.let {
                categoryAnalytics.eventSideCategoryView(it, categoryList[position], position)
            }
        }
    }

}