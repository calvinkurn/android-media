package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.AllBrandAdapter
import com.tokopedia.unifyprinciples.Typography

class AllBrandViewHolder(itemView: View?) : AbstractViewHolder<AllBrandViewModel>(itemView) {

    private var context: Context? = null
    private var adapter: AllBrandAdapter? = null
    private var headerView: Typography? = null
    private var totalBrandView: AppCompatTextView? = null
    private var recyclerView: RecyclerView? = null

    init {
        headerView = itemView?.findViewById(R.id.tv_header)
        totalBrandView = itemView?.findViewById(R.id.tv_total_brand)
        recyclerView = itemView?.findViewById(R.id.rv_new_brand)

        itemView?.context?.let {
            context = it
            adapter = AllBrandAdapter(it)
            recyclerView?.adapter = adapter
            recyclerView?.layoutManager = GridLayoutManager(it, 3)
        }
    }

    override fun bind(element: AllBrandViewModel?) {

        headerView?.text = element?.header?.title

        element?.allBrands?.let {
            adapter?.setAllBrands(it)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_layout
    }
}