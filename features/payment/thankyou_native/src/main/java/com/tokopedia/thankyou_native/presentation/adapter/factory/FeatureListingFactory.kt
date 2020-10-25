package com.tokopedia.thankyou_native.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.FeatureListingAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.model.FeatureListItem
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature.FeatureListViewHolder

class FeatureListingFactory(val listener: FeatureListingAdapterListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            FeatureListViewHolder.LAYOUT_ID -> return FeatureListViewHolder(parent!!, listener)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(featureList: FeatureListItem): Int {
        return FeatureListViewHolder.LAYOUT_ID
    }

}
