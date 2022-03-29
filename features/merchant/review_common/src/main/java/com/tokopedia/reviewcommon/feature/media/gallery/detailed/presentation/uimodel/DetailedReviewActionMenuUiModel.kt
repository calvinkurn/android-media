package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.adapter.typefactory.DetailedReviewActionAdapterTypeFactory
import com.tokopedia.reviewcommon.uimodel.StringRes

data class DetailedReviewActionMenuUiModel(
    val name: StringRes
): Visitable<DetailedReviewActionAdapterTypeFactory> {
    override fun type(typeFactory: DetailedReviewActionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
