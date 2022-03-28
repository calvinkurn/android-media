package com.tokopedia.review.feature.gallery.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryMediaThumbnailUiModel

class ReviewGalleryAdapter(
    typeFactory: ReviewGalleryAdapterTypeFactory
) : BaseListAdapter<ReviewGalleryMediaThumbnailUiModel, ReviewGalleryAdapterTypeFactory>(typeFactory) {
    override fun addElement(visitables: MutableList<out Visitable<Any>>) {
        val previousSize = this.visitables.count()
        this.visitables.addAll(visitables)
        notifyItemRangeInserted(previousSize, visitables.count())
    }

    override fun removeErrorNetwork() {
        visitables.indexOf(errorNetworkModel).takeIf {
            it != RecyclerView.NO_POSITION
        }?.let { errorNetworkModelPosition ->
            visitables.removeAt(errorNetworkModelPosition)
            notifyItemRemoved(errorNetworkModelPosition)
        }
    }
}