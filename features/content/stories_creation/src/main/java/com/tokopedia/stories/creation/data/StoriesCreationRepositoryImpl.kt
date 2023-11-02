package com.tokopedia.stories.creation.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 08, 2023
 */
class StoriesCreationRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : StoriesCreationRepository {
}
