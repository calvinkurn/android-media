package com.tokopedia.stories.widget.di

import com.tokopedia.stories.widget.data.StoriesAvatarRepositoryImpl
import com.tokopedia.stories.widget.domain.StoriesAvatarRepository
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
