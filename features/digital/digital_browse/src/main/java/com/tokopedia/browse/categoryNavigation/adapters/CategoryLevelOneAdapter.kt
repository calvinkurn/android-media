package com.tokopedia.browse.categoryNavigation.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoriesItem
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_category_level_one.view.*

class CategoryLevelOneAdapter(private val categoryList: MutableList<CategoriesItem>,
                              private val listener: CategorylevelOneFragment.CategorySelectListener,
                              private val trackingQueue: TrackingQueue?)
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
        holder.itemView.addOnImpressionListener(
                categoryList[position], object : ViewHintListener {
            override fun onViewHint() {
                if (!viewMap.containsKey(position)) {
                    viewMap[position] = true
                    trackingQueue?.let {
                        CategoryAnalytics.createInstance().eventCategoryLevelOneView(it, categoryList[position], position)
                    }
                }
            }
        })

        holder.categoryName.text = categoryList[position].name

        ImageHandler.loadImage(holder.itemView.context, holder.categoryImage, categoryList[position].iconImageUrl, R.drawable.loading_page)

        holder.parentLayout.setOnClickListener {
            listener.onItemClicked(categoryList[position].id!!, position, categoryList[position].name!!, categoryList[position].applinks)
            CategoryAnalytics.createInstance().eventCategoryLevelOneClick(categoryList[position], position)
        }
        if (categoryList[position].isSelected) {
            holder.unselectedOverlay.hide()
        } else {
            holder.unselectedOverlay.show()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val parentLayout: ConstraintLayout = view.parent_layout
        val categoryImage: ImageView = view.category_imageview
        val categoryName: TextView = view.category_name
        val unselectedOverlay: View = view.unselected_overlay

    }
}