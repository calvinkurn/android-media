package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.unifycomponents.ImageUnify

class CategoryAisleViewHolder(
        itemView: View,
        private val categoryAisleListener: CategoryAisleListener,
): AbstractViewHolder<CategoryAisleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_aisle
    }

    override fun bind(aisle: CategoryAisleDataView) {
        when (aisle.items.size) {
            0 -> {
                val aisleContainer = itemView.findViewById<ConstraintLayout?>(R.id.tokoNowSearchCategoryAisleContainer)
                aisleContainer?.visibility = View.GONE
            }
            1 -> {
                val rightAisleCard = itemView.findViewById<Group?>(R.id.tokoNowSearchCategoryAisleGroupRight)
                rightAisleCard?.visibility = View.INVISIBLE
                bindLeftAisle(aisle.items[0])
            }
            else -> {
                bindLeftAisle(aisle.items[0])
                bindRightAisle(aisle.items[1])
            }
        }
    }

    private fun bindLeftAisle(item: CategoryAisleItemDataView) {
        val txtCategoryNameLeft = itemView.findViewById<TextView?>(R.id.tokoNowSearchCategoryAisleNameLeft)
        val imgCategoryLeft = itemView.findViewById<ImageUnify?>(R.id.tokoNowSearchCategoryAisleImageLeft)

        bindNavigationItem(item, txtCategoryNameLeft, imgCategoryLeft)
    }

    private fun bindRightAisle(item: CategoryAisleItemDataView) {
        val txtCategoryNameRight = itemView.findViewById<TextView?>(R.id.tokoNowSearchCategoryAisleNameRight)
        val imgCategoryRight = itemView.findViewById<ImageUnify?>(R.id.tokoNowSearchCategoryAisleImageRight)

        bindNavigationItem(item, txtCategoryNameRight, imgCategoryRight)
    }

    private fun bindNavigationItem(
            item: CategoryAisleItemDataView,
            txtCategoryName: TextView?,
            imgCategory: ImageUnify?,
    ) {
        txtCategoryName?.text = item.name
        imgCategory?.loadImage(item.imgUrl)
        imgCategory?.setOnClickListener {
            categoryAisleListener.onAisleClick(item)
        }
    }
}