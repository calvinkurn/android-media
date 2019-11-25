package com.tokopedia.filter.newdynamicfilter.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Category

import java.util.ArrayList

/**
 * @author by alifa on 7/6/17.
 */

class CategoryParentAdapter(private val clickListener: OnItemClickListener, var activeId: String?) : RecyclerView.Adapter<CategoryParentAdapter.ItemRowHolder>() {

    private var categories: List<Category>
    var activePosition: Int = 0

    override fun getItemCount(): Int {
        return categories.size
    }

    init {
        categories = ArrayList()
    }

    fun setDataList(dataList: List<Category>) {
        categories = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        @SuppressLint("InflateParams") val v = LayoutInflater.from(
                parent.context).inflate(R.layout.filter_item_category_parent, parent, false
        )
        return ItemRowHolder(v)
    }

    override fun onBindViewHolder(holder: ItemRowHolder, position: Int) {
        holder.bindData(position)
        val onParentClicked = View.OnClickListener { clickListener.onItemClicked(categories[position], position) }
        holder.categoryContainer.setOnClickListener(onParentClicked)
        holder.categoryIcon.setOnClickListener(onParentClicked)
        holder.categoryName.setOnClickListener(onParentClicked)
        holder.categoryContainer.setOnClickListener(onParentClicked)

    }

    inner class ItemRowHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryContainer: LinearLayout = view.findViewById(R.id.category_parent_container)
        var categoryIcon: ImageView = view.findViewById(R.id.category_parent_icon)
        var categoryName: TextView = view.findViewById(R.id.category_parent_text)

        fun bindData(position: Int) {
            val category = categories[position]
            this.categoryName.text = category.name
            ImageHandler.LoadImage(this.categoryIcon, category.iconImageUrl)
            if (category.id.equals(activeId)) {
                this.categoryContainer.isSelected = true
                activePosition = position
            } else {
                this.categoryContainer.isSelected = false
            }
        }

    }

    fun getPositionById(categoryId: String): Int {
        for (i in 0 until itemCount) {
            val category = categories[i]
            if (category.id.equals(categoryId)) {
                return i
            }
        }
        return 0

    }

    interface OnItemClickListener {
        fun onItemClicked(category: Category, position: Int)
    }

}