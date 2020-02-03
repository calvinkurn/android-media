package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageCategoryListAdapter
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageCategoryListShimmeringAdapter
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageCategoryViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                       private val onItemClickListener: OnItemClickListener)
    : AbstractViewHolder<TravelHomepageCategoryListModel>(itemView) {

    private val categoriesRecyclerView: RecyclerView = itemView.findViewById(R.id.category_recycler_view)

    override fun bind(element: TravelHomepageCategoryListModel) {
        val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
        categoriesRecyclerView.layoutManager = layoutManager

        if (element.isLoaded) {
            categoriesRecyclerView.adapter = TravelHomepageCategoryListAdapter(element.categories, onItemClickListener)
        } else {
            onItemBindListener.onCategoryVHBind(element.isLoadFromCloud)

            categoriesRecyclerView.adapter = TravelHomepageCategoryListShimmeringAdapter()
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_category_list
        const val CATEGORY_SPAN_COUNT = 5
    }
}