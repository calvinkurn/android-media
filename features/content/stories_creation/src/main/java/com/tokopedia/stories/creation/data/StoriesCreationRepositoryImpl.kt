package com.tokopedia.stories.creation.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.domain.usecase.CreateStoryUseCase
import com.tokopedia.stories.creation.domain.usecase.GetStoryPreparationInfoUseCase
import com.tokopedia.stories.creation.model.CreateStoryRequest
import com.tokopedia.stories.creation.model.GetStoryPreparationInfoRequest
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.mapper.StoriesCreationUiMapper
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
    private val mapper: StoriesCreationUiMapper,
) : StoriesCreationRepository {

    override suspend fun getCreationAccountList(): List<ContentAccountUiModel> {
        return withContext(dispatchers.io) {
            val response = feedXHeaderUseCase.apply {
                setRequestParams(
                    FeedXHeaderUseCase.createParam(listOf(FeedXHeaderRequestFields.CREATION.value))
                )
            }.executeOnBackground()

            mapper.mapCreationAccountList(response)
        }
    }

    override suspend fun getStoryPreparationInfo(account: ContentAccountUiModel): StoriesCreationConfiguration {
        return withContext(dispatchers.io) {
            val response = getStoryPreparationInfoUseCase(
                GetStoryPreparationInfoRequest.create(
                    authorId = account.id,
                    authorType = ContentCommonUserType.getUserType(account.type)
                )
            )

            mapper.mapStoryPreparationInfo(response)
        }
    }

    override suspend fun createStory(account: ContentAccountUiModel): String {
        return withContext(dispatchers.io) {
            val response = createStoryUseCase(
                CreateStoryRequest.create(
                    authorId = account.id,
                    authorType = ContentCommonUserType.getUserType(account.type)
                )
            )

            response.data.storyId
        }
    }
}
