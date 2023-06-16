package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Shruti Agarwal on 10/02/23
 */
data class FeedModel(
    val items: List<Visitable<FeedAdapterTypeFactory>>,
    val pagination: FeedPaginationModel
) {
    companion object {
        val Empty: FeedModel
            get() = FeedModel(
                items = emptyList(),
                pagination = FeedPaginationModel.Empty,
            )
    }
}
