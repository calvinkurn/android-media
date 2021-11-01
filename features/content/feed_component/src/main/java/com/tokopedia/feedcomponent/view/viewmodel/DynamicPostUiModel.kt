package com.tokopedia.feedcomponent.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory

data class DynamicPostUiModel(val feedXCard: FeedXCard) : Visitable<DynamicFeedTypeFactory> {

    override fun type(typeFactory: DynamicFeedTypeFactory): Int {
        return typeFactory.type(this)
    }
}
