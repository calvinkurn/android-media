package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

interface BaseCreateReviewVisitable<T: BaseAdapterTypeFactory> : Visitable<T> {
    fun areItemsTheSame(other: Any?): Boolean
    fun areContentsTheSame(other: Any?): Boolean
}