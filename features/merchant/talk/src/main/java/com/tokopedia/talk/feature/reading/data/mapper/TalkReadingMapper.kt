package com.tokopedia.talk.feature.reading.data.mapper

import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregate
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel

object TalkReadingMapper {

    fun mapDiscussionAggregateResponseToTalkReadingHeaderModel(discussionAggregate: DiscussionAggregate): TalkReadingHeaderModel {
        return TalkReadingHeaderModel(
                discussionAggregate.productName,
                discussionAggregate.thumbnail
        )
    }

    fun mapDiscussionDataResponseToTalkReadingUiModel() {

    }


}