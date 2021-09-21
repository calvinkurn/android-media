package com.tokopedia.category.navbottomsheet.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.category.navbottomsheet.R
import com.tokopedia.category.navbottomsheet.model.CategoriesItem
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import kotlinx.android.synthetic.main.item_cat_level_one.view.*
import kotlinx.android.synthetic.main.item_shimmer_cat_level_one.view.*


class  CategoryNavLevelOneAdapter(private val categoryList: MutableList<CategoriesItem?>,
                              private val listener: CategorySelectListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categoryItem = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            categoryItem -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cat_level_one, parent, false)
                CategoryViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shimmer_cat_level_one, parent, false)
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
            shimmerViewHolder.shimmerParent.setBackgroundColor(MethodChecker.getColor(shimmerViewHolder.itemView.context, R.color.Unify_N0))
        } else {
            shimmerViewHolder.shimmerParent.setBackgroundColor(MethodChecker.getColor(shimmerViewHolder.itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N50))
        }
    }

    private fun initCategoryViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoryList[position]

        holder.parentLayout.setOnClickListener {
            listener.onItemClicked(item?.id
                    ?: "", position, item?.name
                    ?: "")

        }
        if (item?.isSelected == true) {
            holder.categoryImage.loadImage(item.iconImageUrl ?:"", R.drawable.square_shimmer)
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, R.color.Unify_N0))
            holder.categoryName.setTextColor(MethodChecker.getColor(holder.itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
        } else {
            holder.categoryImage.loadImage(item?.iconImageUrlGray ?:"", R.drawable.square_shimmer)
            holder.parentLayout.setBackgroundColor(MethodChecker.getColor(holder.itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N50_68))
            holder.categoryName.setTextColor(MethodChecker.getColor(holder.itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }

        with(holder.categoryName) {setTextAndContentDescription(getEllipsizedMessage(item?.name ?: ""), R.string.nbs_content_desc_name)}
    }


    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return categoryList[position]?.type?:0
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parentLayout: ConstraintLayout = view.parent_layout
        val categoryImage: ImageView = view.category_imageview
        val categoryName: TextView = view.category_name
    }

    class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shimmerParent: ConstraintLayout = view.shimmer_parent
    }

    private fun getEllipsizedMessage(message: String): String? {
        return if (message.length > 18) {
            message.substring(0, 17).plus("...")
        } else message
    }


    interface CategorySelectListener {
        fun onItemClicked(id: String, position: Int, categoryName: String)
    }
}