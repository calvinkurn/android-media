package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.adapter.typefactory

import android.view.View
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.adapter.viewholder.DetailedReviewActionViewHolder
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel.DetailedReviewActionMenuUiModel

class DetailedReviewActionAdapterTypeFactory(
    private val detailedReviewActionMenuListener: Listener
) : BaseAdapterTypeFactory() {
    fun type(detailedReviewActionMenuUiModel: DetailedReviewActionMenuUiModel): Int {
        return DetailedReviewActionViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DetailedReviewActionViewHolder.LAYOUT -> {
                DetailedReviewActionViewHolder(parent, detailedReviewActionMenuListener)
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {
        fun onReviewActionMenuClicked(@StringRes id: Int)
    }
}