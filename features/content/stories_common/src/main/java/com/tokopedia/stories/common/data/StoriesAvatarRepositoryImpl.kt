package com.tokopedia.stories.common.data

import com.tokopedia.stories.common.domain.StoriesAvatarRepository
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarRepositoryImpl @Inject constructor(
    private val prefUtil: StoriesAvatarPreferenceUtil
) : StoriesAvatarRepository {

    override suspend fun setHasSeenCoachMark() {
        prefUtil.setHasSeenCoachMark()
    }

    override suspend fun hasSeenCoachMark(): Boolean {
        return prefUtil.hasSeenCoachMark()
    }
}
