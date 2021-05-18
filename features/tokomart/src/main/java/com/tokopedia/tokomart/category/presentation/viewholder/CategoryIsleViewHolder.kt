package com.tokopedia.tokomart.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import kotlinx.android.synthetic.main.item_tokomart_category_aisle.view.*

class CategoryIsleViewHolder(itemView: View): AbstractViewHolder<CategoryIsleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_aisle
    }

    override fun bind(element: CategoryIsleDataView?) {
        val dummyData = listOf(
                CategoryIsleDataView("Daging & Seafood"),
                CategoryIsleDataView("Makanan Kering")
        )

        itemView.run {
            txt_category_name_left.text = dummyData[0].name
            txt_category_name_right.text = dummyData[1].name
            img_category_left.setImageUrl("https://thefatkidinside.com/wp-content/uploads/2019/08/jakarta-header-716x375.jpg")
            img_category_right.setImageUrl("https://thefatkidinside.com/wp-content/uploads/2019/08/jakarta-header-716x375.jpg")
        }
    }
}