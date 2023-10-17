package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel

/**
 * Created by kenny.hadisaputra on 16/10/23
 */
internal data class FeedBrowseUiModel2(
    val result: ResultState,
    val model: FeedBrowseModel,
)
