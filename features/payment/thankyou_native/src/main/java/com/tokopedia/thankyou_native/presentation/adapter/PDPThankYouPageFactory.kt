package com.tokopedia.thankyou_native.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringListViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.thankyou_native.presentation.adapter.model.PDPItemAdapterModel
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.PDPViewHolder

class PDPThankYouPageFactory(listener: RecommendationListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            PDPViewHolder.LAYOUT_ID -> return PDPViewHolder(parent!!)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(PDPItemAdapterModel: PDPItemAdapterModel): Int {
        return PDPViewHolder.LAYOUT_ID
    }

}