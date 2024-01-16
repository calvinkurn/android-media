package com.tokopedia.creation.common.util

/**
 * Created By : Jonathan Darwin on November 10, 2023
 */
object StoriesAppLinkBuilder {

    private const val STORY_ID_TEMPLATE = "{story_id}"

    fun buildForShareLink(
        appLinkTemplate: String,
        storyId: String,
    ): String {
        return appLinkTemplate.replace(STORY_ID_TEMPLATE, storyId)
    }
}
