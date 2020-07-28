package com.tokopedia.talk.feature.reading.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.reading.data.model.discussiondata.Question
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory

class TalkReadingUiModel(
        val question: Question,
        val shopId: String
) : Visitable<TalkReadingAdapterTypeFactory>{

    override fun type(typeFactory: TalkReadingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}