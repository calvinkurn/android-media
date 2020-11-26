package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageCategoryListAdapter
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageCategoryListShimmeringAdapter
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageCategoryViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                       private val travelHomepageActionListener: TravelHomepageActionListener)
    : AbstractViewHolder<TravelHomepageCategoryListModel>(itemView) {

    private val categoriesRecyclerView: RecyclerView = itemView.findViewById(R.id.category_recycler_view)
    lateinit var adapter: TravelHomepageCategoryListAdapter
    private var currentPosition = -1

    override fun bind(element: TravelHomepageCategoryListModel) {
        val layoutManager = GridLayoutManager(itemView.context, CATEGORY_SPAN_COUNT)
        categoriesRecyclerView.layoutManager = layoutManager

        if (element.isLoaded) {
            if (element.isSuccess && element.categories.isNotEmpty()) {
                categoriesRecyclerView.show()
                if (!::adapter.isInitialized || currentPosition != element.layoutData.position) {
                    currentPosition = element.layoutData.position
                    adapter = TravelHomepageCategoryListAdapter(element.categories, travelHomepageActionListener)
                    categoriesRecyclerView.adapter = adapter
                } else adapter.updateData(element.categories)
            } else categoriesRecyclerView.hide()

        } else {
            currentPosition = -1
            categoriesRecyclerView.show()
            onItemBindListener.onCategoryItemBind(element.layoutData, element.isLoadFromCloud)
            categoriesRecyclerView.adapter = TravelHomepageCategoryListShimmeringAdapter()
        }
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_category_list
        const val CATEGORY_SPAN_COUNT = 5
    }
}