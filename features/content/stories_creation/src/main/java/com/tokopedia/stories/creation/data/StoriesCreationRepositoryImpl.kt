package com.tokopedia.stories.creation.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.domain.usecase.CreateStoryUseCase
import com.tokopedia.stories.creation.domain.usecase.GetStoryPreparationInfoUseCase
import com.tokopedia.stories.creation.domain.usecase.SetActiveProductTagUseCase
import com.tokopedia.stories.creation.model.CreateStoryRequest
import com.tokopedia.stories.creation.model.GetStoryPreparationInfoRequest
import com.tokopedia.stories.creation.model.SetActiveProductTagRequest
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.mapper.StoriesCreationUiMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 08, 2023
 */
class StoriesCreationRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val getStoryPreparationInfoUseCase: GetStoryPreparationInfoUseCase,
    private val createStoryUseCase: CreateStoryUseCase,
    private val setActiveProductTagUseCase: SetActiveProductTagUseCase,
    private val mapper: StoriesCreationUiMapper,
) : StoriesCreationRepository {

    override suspend fun getCreationAccountList(): List<ContentAccountUiModel> {
        return withContext(dispatchers.io) {
//            val response = feedXHeaderUseCase.apply {
//                setRequestParams(
//                    FeedXHeaderUseCase.createParam(listOf(FeedXHeaderRequestFields.CREATION.value))
//                )
//            }.executeOnBackground()
//
//            mapper.mapCreationAccountList(response)

            delay(200)
            listOf(
                ContentAccountUiModel(
                    id = "123",
                    name = "Jonathan Darwin",
                    iconUrl = "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
                    type = "content-shop",
                    hasUsername = true,
                    hasAcceptTnc = true,
                    badge = "",
                    enable = true,
                )
            )
        }
    }

    override suspend fun getStoryPreparationInfo(account: ContentAccountUiModel): StoriesCreationConfiguration {
        return withContext(dispatchers.io) {
//            val response = getStoryPreparationInfoUseCase(
//                GetStoryPreparationInfoRequest(
//                    authorId = account.id,
//                    authorType = account.type,
//                )
//            )
//
//            mapper.mapStoryPreparationInfo(response)

            delay(200)
            StoriesCreationConfiguration.Empty.copy(
                maxStoriesConfig = StoriesCreationConfiguration.MaxStoriesConfig(
                    isLimitReached = true,
                    imageUrl = "https://images.tokopedia.net/img/android/content/content_creation/ic_content_too_much.png",
                    title = "Oops, kamu sudah terlalu banyak upload Story",
                    description = "Kamu tetap bisa upload lebih dari 30 Story, tapi yang terlama akan kami hapus, ya.",
                    primaryText = "Buat Story",
                    secondaryText = "Kembali"
                )
            )
        }
    }

    override suspend fun createStory(account: ContentAccountUiModel): String {
        return withContext(dispatchers.io) {
//            val response = createStoryUseCase(
//                CreateStoryRequest(
//                    authorId = account.id,
//                    authorType = account.type,
//                )
//            )
//
//            response.data.storyId

            delay(200)
            "123"
        }
    }

    override suspend fun setActiveProductTag(
        storyId: String,
        productIds: List<String>
    ) {
        return withContext(dispatchers.io) {
            setActiveProductTagUseCase(
                SetActiveProductTagRequest(
                    storyId = storyId,
                    productIds = productIds
                )
            )
        }
    }
}
