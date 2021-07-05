package com.tokopedia.topchat.stub.chatsearch.di

import com.tokopedia.topchat.chatsearch.di.ChatSearchScope
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import com.tokopedia.topchat.stub.chatsearch.usecase.GetSearchQueryUseCaseStub
import dagger.Module
import dagger.Provides

@Module
class ChatSearchUsecaseStub(private val getSearchQueryUseCaseStub: GetSearchQueryUseCaseStub) {

    @ChatSearchScope
    @Provides
    fun provideGetSearchQueryUseCaseStub(): GetSearchQueryUseCase = getSearchQueryUseCaseStub
}