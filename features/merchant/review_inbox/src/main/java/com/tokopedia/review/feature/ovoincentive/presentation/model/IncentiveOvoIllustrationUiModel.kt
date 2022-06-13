package com.tokopedia.review.feature.ovoincentive.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.typefactory.IncentiveOvoIllustrationAdapterTypeFactory

data class IncentiveOvoIllustrationUiModel(
    val imageUrl: String,
    val text: String
): Visitable<IncentiveOvoIllustrationAdapterTypeFactory> {
    override fun type(typeFactory: IncentiveOvoIllustrationAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}