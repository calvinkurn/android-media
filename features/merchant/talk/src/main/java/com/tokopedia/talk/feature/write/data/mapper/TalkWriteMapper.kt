package com.tokopedia.talk.feature.write.data.mapper

import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingFormCategory
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory

object TalkWriteMapper {

    fun mapDiscussionCategoryToTalkCategory(discussionCategories: List<DiscussionGetWritingFormCategory>): List<TalkWriteCategory> {
        return discussionCategories.map {
            TalkWriteCategory(it.name, it.message, false)
        }
    }
}