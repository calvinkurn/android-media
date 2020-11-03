package com.tokopedia.kategori.adapters

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
import com.tokopedia.kategori.R
import com.tokopedia.kategori.analytics.CategoryAnalytics.Companion.categoryAnalytics
import com.tokopedia.kategori.view.fragments.CategoryLevelOneFragment
import com.tokopedia.kategori.model.CategoriesItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.item_category_level_one.view.*
import kotlinx.android.synthetic.main.item_shimmer_level_one.view.*

class CategoryLevelOneAdapter(private val categoryList: MutableList<CategoriesItem>,
                              private val listener: CategoryLevelOneFragment.CategorySelectListener,
                              private val trackingQueue: TrackingQueue?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categoryItem = 1
    private val viewMap = HashMap<Int, Boolean>()
    private val itemListTrackerList = ArrayList<CategoriesItem>()

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
            shimmerViewHolder.shimmerParent.setBackgroundColor(MethodChecker.getColor(shimmerViewHolder.itemView.context, R.color.category_unselected_background))
        }
    }

    private fun initCategoryViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoryList[position]

        holder.parentLayout.setOnClickListener {
            listener.onItemClicked(item.id
                    ?: "", position, item.name
                    ?: "", item.applinks)

            categoryAnalytics.eventSideBarCategoryClick(categoryList[position], position)
        }
        if (item.isSelected) {
            ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, item.iconImageUrl, R.drawable.square_shimmer)
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, R.color.white))
        } else {
            ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, item.iconImageUrlGray, R.drawable.square_shimmer)
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, R.color.category_background))
        }

        getEllipsizedMessage(item.name ?: "")?.let { TextAndContentDescriptionUtil.setTextAndContentDescription(holder.categoryName, it, holder.categoryName.context.getString(R.string.content_desc_category_name)) }
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
        if (holder.itemViewType == categoryItem) {
            val position = holder.adapterPosition
            val item = categoryList[position]
            item.position = position
            if (!viewMap.containsKey(position)) {
                viewMap[position] = true
                itemListTrackerList.add(item)
            }
        }
    }

    fun onPause() {
        categoryAnalytics.eventSideCategoryView(itemListTrackerList)
        itemListTrackerList.clear()
    }

    private fun getEllipsizedMessage(message: String): String? {
        return if (message.length > 18) {
            message.substring(0, 17).plus("...")
        } else message
    }
}