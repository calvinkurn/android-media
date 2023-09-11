package com.tokopedia.stories.creation.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.domain.usecase.CreateStoryUseCase
import com.tokopedia.stories.creation.domain.usecase.GetStoryPreparationInfoUseCase
import com.tokopedia.stories.creation.domain.usecase.SetActiveProductTagUseCase
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
    private val getStoryPreparationInfoUseCase: GetStoryPreparationInfoUseCase,
    private val createStoryUseCase: CreateStoryUseCase,
    private val setActiveProductTagUseCase: SetActiveProductTagUseCase,
    private val mapper: StoriesCreationUiMapper,
) : StoriesCreationRepository {

    override suspend fun getStoryPreparationInfo(account: ContentAccountUiModel): StoriesCreationConfiguration {
        return withContext(dispatchers.io) {
            val response = getStoryPreparationInfoUseCase(
                GetStoryPreparationInfoRequest(
                    authorId = account.id,
                    authorType = account.type,
                )
            )

            mapper.mapStoryPreparationInfo(response)
        }
    }
}
