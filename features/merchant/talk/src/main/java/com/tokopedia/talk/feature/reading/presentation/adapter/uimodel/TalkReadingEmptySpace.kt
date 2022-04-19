package com.tokopedia.talk.feature.reading.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory

class TalkReadingEmptySpace : Visitable<TalkReadingAdapterTypeFactory> {

    override fun type(typeFactory: TalkReadingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}