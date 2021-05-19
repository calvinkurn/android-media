package com.tokopedia.tokomart.category.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.item_tokomart_category_aisle.view.*

class CategoryAisleViewHolder(itemView: View): AbstractViewHolder<CategoryAisleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_aisle
    }

    override fun bind(aisle: CategoryAisleDataView) {
        val txtCategoryNameLeft = itemView.findViewById<TextView>(R.id.tokomartSearchCategoryAisleNameLeft)
        val txtCategoryNameRight = itemView.findViewById<TextView>(R.id.tokomartSearchCategoryAisleNameRight)
        val imgCategoryleft = itemView.findViewById<ImageUnify>(R.id.tokomartSearchCategoryAisleImageLeft)
        val imgCategoryRight = itemView.findViewById<ImageUnify>(R.id.tokomartSearchCategoryAisleImageRight)

        itemView.run {
            txtCategoryNameLeft.text = aisle.items[0].name
            txtCategoryNameRight.text = aisle.items[1].name
            imgCategoryleft.setImageUrl(aisle.items[0].imgUrl)
            imgCategoryRight.setImageUrl(aisle.items[1].imgUrl)
        }
    }
}