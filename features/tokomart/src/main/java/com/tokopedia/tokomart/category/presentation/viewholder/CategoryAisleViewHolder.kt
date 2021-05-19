package com.tokopedia.tokomart.category.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.item_tokomart_category_aisle.view.*

class CategoryAisleViewHolder(itemView: View): AbstractViewHolder<CategoryAisleDataView>(itemView) {

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
                val rightAisleCard = itemView.findViewById<CardUnify?>(R.id.tokomartSearchCategoryAisleCardRight)
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

        txtCategoryNameLeft?.text = item.name
        imgCategoryLeft?.setImageUrl(item.imgUrl)
    }

    private fun bindRightAisle(item: CategoryAisleItemDataView) {
        val txtCategoryNameRight = itemView.findViewById<TextView?>(R.id.tokomartSearchCategoryAisleNameRight)
        val imgCategoryRight = itemView.findViewById<ImageUnify?>(R.id.tokomartSearchCategoryAisleImageRight)

        txtCategoryNameRight?.text = item.name
        imgCategoryRight?.setImageUrl(item.imgUrl)
    }
}