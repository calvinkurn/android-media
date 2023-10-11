package com.tokopedia.play.broadcaster.setup.di

import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@Module
class PlayBroadcastRepositoryTestModule(
    private val mockRepo: PlayBroadcastRepository
) {

    @Provides
    @ActivityRetainedScope
    fun providePlayBroadcastRepository(): PlayBroadcastRepository = mockRepo
}
