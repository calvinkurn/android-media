package com.tokopedia.thankyou_native.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.GyroAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationListItem
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroTokomemberItem
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature.GyroListItemViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature.GyroTokomemberViewHolder

class GyroRecommendationFactory(val listener: GyroAdapterListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            GyroListItemViewHolder.LAYOUT_ID -> return GyroListItemViewHolder(parent, listener)
            GyroTokomemberViewHolder.LAYOUT_ID -> return GyroTokomemberViewHolder(parent,listener)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(gyroRecommendationList: GyroRecommendationListItem): Int {
        return GyroListItemViewHolder.LAYOUT_ID
    }

    fun type(gyroTokomemberItem: GyroTokomemberItem): Int {
        return GyroTokomemberViewHolder.LAYOUT_ID
    }

}
