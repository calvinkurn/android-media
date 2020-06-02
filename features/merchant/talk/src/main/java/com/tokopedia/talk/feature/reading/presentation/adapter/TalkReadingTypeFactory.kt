package com.tokopedia.talk.feature.reading.presentation.adapter

import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel

interface TalkReadingTypeFactory {
    fun type(talkReadingUiModel: TalkReadingUiModel): Int
}