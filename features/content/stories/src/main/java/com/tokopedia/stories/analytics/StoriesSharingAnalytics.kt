package com.tokopedia.stories.analytics

/**
 * @author by astidhiyaa on 05/09/23
 */
interface StoriesSharingAnalytics {
    interface Factory {
        fun create(
            shopId: String,
        ): StoriesSharingAnalytics
    }
    fun onClickShareIcon(storyId: String)
    fun onClickShareOptions(storyId: String)
    fun onImpressShareSheet(storyId: String)
    fun onCloseShareSheet(storyId: String)
}
