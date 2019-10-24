package com.tokopedia.tkpd.tkpdreputation.createreputation.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named


@CreateReviewScope
@Module
class CreateReviewModule {

    @CreateReviewScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}