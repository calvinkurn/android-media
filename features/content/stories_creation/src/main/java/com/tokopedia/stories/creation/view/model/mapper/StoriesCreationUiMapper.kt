package com.tokopedia.stories.creation.view.model.mapper

import com.google.gson.Gson
import com.tokopedia.content.common.model.FeedXHeaderResponse
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.model.GetStoryPreparationInfoResponse
import com.tokopedia.stories.creation.model.StoriesCreationConfigResponse
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
class StoriesCreationUiMapper @Inject constructor(
    private val gson: Gson,
) {

    fun mapCreationAccountList(response: FeedXHeaderResponse): List<ContentAccountUiModel> {
        return response.feedXHeaderData.data.creation.authors.map { author ->
            ContentAccountUiModel(
                id = author.id,
                name = author.name,
                iconUrl = author.image,
                badge = "",
                type = author.type,
                hasUsername = author.hasUsername,
                hasAcceptTnc = author.hasAcceptTnC,
                enable = author.items.any { item -> item.type == CREATION_TYPE_STORY && item.isActive }
            )
        }
    }

    fun mapStoryPreparationInfo(response: GetStoryPreparationInfoResponse): StoriesCreationConfiguration {
        val config = mapConfig(response.data)

        return StoriesCreationConfiguration(
            storiesId = config.draftStoryId,
            maxProductTag = 30,
            showDuration = "24 Jam",
            maxStoriesConfig = StoriesCreationConfiguration.MaxStoriesConfig(
                isLimitReached = config.maxStoryBottomSheetConfig.isLimitReached,
                imageUrl = config.maxStoryBottomSheetConfig.imageUrl,
                title = config.maxStoryBottomSheetConfig.title,
                description = config.maxStoryBottomSheetConfig.description,
                primaryText = config.maxStoryBottomSheetConfig.activeButtonCopy,
                secondaryText = config.maxStoryBottomSheetConfig.buttonCopy,
            )
        )
    }

    private fun mapConfig(data: GetStoryPreparationInfoResponse.Data): StoriesCreationConfigResponse {
        return try {
            gson.fromJson(data.config, StoriesCreationConfigResponse::class.java)
        } catch (e: Throwable) {
            StoriesCreationConfigResponse()
        }
    }

    companion object {
        private const val CREATION_TYPE_STORY = "story"
    }
}
