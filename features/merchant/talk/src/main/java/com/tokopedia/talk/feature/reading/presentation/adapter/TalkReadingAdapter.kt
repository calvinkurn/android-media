package com.tokopedia.talk.feature.reading.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel

class TalkReadingAdapter(
        talkReadingAdapterTypeFactory: TalkReadingAdapterTypeFactory
) : BaseListAdapter<TalkReadingUiModel, TalkReadingAdapterTypeFactory>(talkReadingAdapterTypeFactory)