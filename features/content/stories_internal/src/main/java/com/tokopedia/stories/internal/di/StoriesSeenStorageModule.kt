package com.tokopedia.stories.internal.di

import com.tokopedia.stories.internal.storage.AppSessionStoriesSeenStorage
import com.tokopedia.stories.internal.storage.StoriesSeenStorage
import dagger.Module
import dagger.Provides

/**
 * Created by kenny.hadisaputra on 22/08/23
 */
@Module
object StoriesSeenStorageModule {

    @Provides
    fun provideStoriesSeenStorage(): StoriesSeenStorage {
        return AppSessionStoriesSeenStorage
    }
}
