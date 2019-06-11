package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoriesItem
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import kotlinx.android.synthetic.main.item_category_level_one.view.*

class CategoryLevelOneAdapter(private val categoryList: MutableList<CategoriesItem>, private val context: Context, private val listener: CategorylevelOneFragment.CategorySelectListener) : RecyclerView.Adapter<CategoryLevelOneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category_level_one, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.categoryName.text = categoryList[position].name
        Glide.with(holder.itemView.context)
                .load(categoryList[position].iconImageUrl)
                .placeholder(R.drawable.loading_page)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .into(holder.categoryImage)

        holder.parent_layout.setOnClickListener {
            listener.onItemClicked(categoryList[position].id!!, position, categoryList[position].name!!, categoryList[position].applinks)
        }
        if (categoryList[position].isSelected) {
            holder.parent_layout.setBackgroundColor(context.resources.getColor(R.color.white))
        } else {
            holder.parent_layout.setBackgroundColor(context.resources.getColor(R.color.unselected_background))
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val parent_layout = view.parent_layout
        val categoryImage = view.category_imageview
        val categoryName = view.category_name

    }
}