package com.tokopedia.feedplus.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created By : Muhammad Furqan on 09/10/23
 */
object FeedContentManager {
    val muteState = MutableStateFlow<Boolean?>(null)
}
