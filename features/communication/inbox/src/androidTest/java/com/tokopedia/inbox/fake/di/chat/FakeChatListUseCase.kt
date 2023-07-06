package com.tokopedia.inbox.fake.di.chat

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.fake.domain.usecase.chat.FakeGetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import dagger.Module
import dagger.Provides

@Module
class FakeChatListUseCase {

    @ActivityScope
    @Provides
    fun provideGetChatListMessageUseCase(
        fake: FakeGetChatListMessageUseCase
    ): GetChatListMessageUseCase {
        return fake
    }

    // -- separator -- //
}
