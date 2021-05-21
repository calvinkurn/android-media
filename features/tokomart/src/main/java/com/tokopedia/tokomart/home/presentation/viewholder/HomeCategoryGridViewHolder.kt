package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokoMart
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapter
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapterTypeFactory
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import kotlinx.android.synthetic.main.item_tokomart_home_category_grid.view.*

class HomeCategoryGridViewHolder(itemView: View): AbstractViewHolder<HomeCategoryGridUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_home_category_grid

        private const val GRID_SPAN_COUNT = 2
    }

    private val adapter by lazy { TokoMartHomeAdapter(TokoMartHomeAdapterTypeFactory(), TokoMartHomeListDiffer()) }

    override fun bind(data: HomeCategoryGridUiModel) {
        itemView.apply {
            textTitle.text = data.title
            textSeeAll.setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalTokoMart.CATEGORY_LIST)
            }

            with(rvCategory) {
                adapter = this@HomeCategoryGridViewHolder.adapter
                layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
            }

            adapter.submitList(data.categoryList)
        }
    }
}