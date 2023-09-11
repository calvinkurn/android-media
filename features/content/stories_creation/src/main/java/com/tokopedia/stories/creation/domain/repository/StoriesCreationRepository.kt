package com.tokopedia.stories.creation.domain.repository

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration

/**
 * Created By : Jonathan Darwin on September 08, 2023
 */
interface StoriesCreationRepository {

    suspend fun getStoryPreparationInfo(
        account: ContentAccountUiModel
    ): StoriesCreationConfiguration

    suspend fun createStory(
        account: ContentAccountUiModel
    ): String

    suspend fun setActiveProductTag(
        storyId: String,
        productIds: List<String>,
    )
}
