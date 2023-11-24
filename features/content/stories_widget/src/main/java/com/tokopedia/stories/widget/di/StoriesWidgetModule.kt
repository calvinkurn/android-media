package com.tokopedia.stories.widget.di

import com.tokopedia.stories.widget.data.StoriesWidgetRepositoryImpl
import com.tokopedia.stories.widget.domain.StoriesWidgetRepository
import dagger.Binds
import dagger.Module

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
@Module
internal interface StoriesWidgetModule {

    @Binds
    fun bindRepository(repository: StoriesWidgetRepositoryImpl): StoriesWidgetRepository
}
