package com.tokopedia.topchat.chatsearch.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
@ChatSearchScope
class ChatSearchModule {

    @ChatSearchScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}