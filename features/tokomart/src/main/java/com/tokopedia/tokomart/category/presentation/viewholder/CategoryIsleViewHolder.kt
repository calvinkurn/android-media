package com.tokopedia.tokomart.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import com.tokopedia.tokomart.category.presentation.view.CategoryIsleAdapter
import kotlinx.android.synthetic.main.item_tokomart_category_isle.view.*

class CategoryIsleViewHolder(itemView: View): AbstractViewHolder<CategoryIsleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_isle
    }

    override fun bind(element: CategoryIsleDataView?) {
        val dummyData = listOf(
                CategoryIsleDataView("Daging & Seafood"),
                CategoryIsleDataView("Makanan Kering")
        )
        val adapter = CategoryIsleAdapter(dummyData.subList(0, 2))
        itemView.rv_category_isle.setHasFixedSize(true)
        itemView.rv_category_isle.layoutManager = GridLayoutManager(itemView.context, 2)
        itemView.rv_category_isle.adapter = adapter
    }
}