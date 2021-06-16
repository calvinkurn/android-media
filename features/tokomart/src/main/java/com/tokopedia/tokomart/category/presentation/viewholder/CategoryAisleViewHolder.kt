package com.tokopedia.tokomart.category.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.unifycomponents.ImageUnify

class CategoryAisleViewHolder(
        itemView: View,
        private val categoryAisleListener: CategoryAisleListener,
): AbstractViewHolder<CategoryAisleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_aisle
    }

    override fun bind(aisle: CategoryAisleDataView) {
        when (aisle.items.size) {
            0 -> {
                val aisleContainer = itemView.findViewById<ConstraintLayout?>(R.id.tokomartSearchCategoryAisleContainer)
                aisleContainer?.visibility = View.GONE
            }
            1 -> {
                val rightAisleCard = itemView.findViewById<Group?>(R.id.tokomartSearchCategoryAisleGroupRight)
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
        val txtCategoryNameLeft = itemView.findViewById<TextView?>(R.id.tokomartSearchCategoryAisleNameLeft)
        val imgCategoryLeft = itemView.findViewById<ImageUnify?>(R.id.tokomartSearchCategoryAisleImageLeft)

        bindNavigationItem(item, txtCategoryNameLeft, imgCategoryLeft)
    }

    private fun bindRightAisle(item: CategoryAisleItemDataView) {
        val txtCategoryNameRight = itemView.findViewById<TextView?>(R.id.tokomartSearchCategoryAisleNameRight)
        val imgCategoryRight = itemView.findViewById<ImageUnify?>(R.id.tokomartSearchCategoryAisleImageRight)

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