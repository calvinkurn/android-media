package com.tokopedia.review.feature.ovoincentive.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.viewholder.IncentiveOvoIllustrationViewHolder
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoIllustrationUiModel

class IncentiveOvoIllustrationAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(uiModel: IncentiveOvoIllustrationUiModel): Int {
        return IncentiveOvoIllustrationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            IncentiveOvoIllustrationViewHolder.LAYOUT -> IncentiveOvoIllustrationViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}