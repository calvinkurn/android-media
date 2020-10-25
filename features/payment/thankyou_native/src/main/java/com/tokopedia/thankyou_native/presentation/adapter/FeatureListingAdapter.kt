package com.tokopedia.thankyou_native.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.factory.FeatureListingFactory
import com.tokopedia.thankyou_native.presentation.adapter.model.FeatureListItem
import com.tokopedia.thankyou_native.presentation.views.FeatureListView

class FeatureListingAdapter(val visitableList: ArrayList<Visitable<*>>,
                            typeFactory: FeatureListingFactory) :
        BaseAdapter<FeatureListingFactory>(typeFactory, visitableList) {
    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }
}

interface FeatureListingAdapterListener {
    fun onItemDisplayed(featureListItem: FeatureListItem)
    fun onItemClicked(featureListItem: FeatureListItem)
    fun openAppLink(appLink: String)
    fun openWebUrl(url: String)
}