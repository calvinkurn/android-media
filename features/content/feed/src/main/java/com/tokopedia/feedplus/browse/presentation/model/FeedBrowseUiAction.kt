package com.tokopedia.feedplus.browse.presentation.model

/**
 * Created by meyta.taliti on 11/08/23.
 */
sealed class FeedBrowseUiAction {

    object FetchTitle : FeedBrowseUiAction()

    object FetchSlots : FeedBrowseUiAction()

}
