package com.tokopedia.kol.feature.postdetail.view.datamodel

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class ContentDetailRevampDataUiModel(
    var postList: List<FeedXCard> = emptyList(),
    var cursor: String = ""
): ImpressHolder()