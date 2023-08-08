package com.tokopedia.stories.common.di

import com.tokopedia.stories.common.data.StoriesAvatarRepositoryImpl
import com.tokopedia.stories.common.domain.StoriesAvatarRepository
import dagger.Binds
import dagger.Module

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
@Module
internal interface StoriesAvatarModule {

    @Binds
    fun bindRepository(repository: StoriesAvatarRepositoryImpl): StoriesAvatarRepository
}
