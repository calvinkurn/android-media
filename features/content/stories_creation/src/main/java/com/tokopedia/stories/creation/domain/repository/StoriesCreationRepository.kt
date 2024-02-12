package com.tokopedia.stories.creation.domain.repository

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.creation.common.upload.model.stories.StoriesStatus
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration

/**
 * Created By : Jonathan Darwin on September 08, 2023
 */
interface StoriesCreationRepository {

    suspend fun getCreationAccountList(): List<ContentAccountUiModel>

    suspend fun getStoryPreparationInfo(
        account: ContentAccountUiModel
    ): StoriesCreationConfiguration

    suspend fun createStory(
        account: ContentAccountUiModel
    ): String

    suspend fun updateStoryStatus(
        storyId: String,
        status: StoriesStatus
    )
}
