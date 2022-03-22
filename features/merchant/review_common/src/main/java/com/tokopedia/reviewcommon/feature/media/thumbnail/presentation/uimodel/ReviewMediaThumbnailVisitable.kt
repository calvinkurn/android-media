package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory

interface ReviewMediaThumbnailVisitable : Visitable<ReviewMediaThumbnailTypeFactory>, Parcelable {
    fun areItemsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean
    fun areContentsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean
    fun getChangePayload(newItem: ReviewMediaThumbnailVisitable?): List<String>
}