package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.thankyou_native.presentation.adapter.factory.FeatureListingFactory

class FeatureListingAdapter(val visitableList: ArrayList<Visitable<*>>,
                            typeFactory: FeatureListingFactory) :
        BaseAdapter<FeatureListingFactory>(typeFactory, visitableList) {
    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }
}

interface FeatureListingAdapterListener {
    fun openAppLink(appLink: String)
    fun openWebUrl(url: String)
}