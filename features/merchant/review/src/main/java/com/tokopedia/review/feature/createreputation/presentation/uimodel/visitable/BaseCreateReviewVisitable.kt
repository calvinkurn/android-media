package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

interface BaseCreateReviewVisitable<T: BaseAdapterTypeFactory> : Visitable<T> {

    companion object {
        const val PAYLOAD_SHOWING = "payloadShowing"
        const val PAYLOAD_HIDING = "payloadHiding"
    }

    fun areItemsTheSame(other: Any?): Boolean
    fun areContentsTheSame(other: Any?): Boolean
    fun getChangePayload(other: Any?): List<String>?
    fun getSpanSize(): Int

    open class BaseCreateReviewHolderInfo(
        val shouldShow: Boolean = false,
        val shouldHide: Boolean = false,
    ) : RecyclerView.ItemAnimator.ItemHolderInfo()
}