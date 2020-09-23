package com.tokopedia.thankyou_native.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.FeatureListingAdapter
import com.tokopedia.thankyou_native.presentation.adapter.FeatureListingAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.model.FeatureListItem
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature.FeatureListViewModel

class FeatureListingFactory(val listener: FeatureListingAdapterListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            FeatureListViewModel.LAYOUT_ID -> return FeatureListViewModel(parent!!, listener)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(featureList: FeatureListItem): Int {
        return FeatureListViewModel.LAYOUT_ID
    }

}
