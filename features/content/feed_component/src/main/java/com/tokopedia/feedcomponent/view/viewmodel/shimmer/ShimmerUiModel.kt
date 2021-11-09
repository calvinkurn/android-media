package com.tokopedia.feedcomponent.view.viewmodel.shimmer

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory

class ShimmerUiModel : Visitable<DynamicFeedTypeFactory> {
    override fun type(typeFactory: DynamicFeedTypeFactory): Int {
        return typeFactory.type(this)
    }
}