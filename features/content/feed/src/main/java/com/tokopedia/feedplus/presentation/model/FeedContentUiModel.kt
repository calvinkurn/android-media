package com.tokopedia.feedplus.presentation.model

import com.tokopedia.feedplus.presentation.model.type.FeedContentType

/**
 * Created by meyta.taliti on 10/10/23.
 */
sealed interface FeedContentUiModel {
    val id: String
    val contentType: FeedContentType
    val share: FeedShareModel?
}
