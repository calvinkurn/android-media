package com.tokopedia.topchat.stub.chatsearch.di

import com.tokopedia.topchat.chatsearch.di.ChatSearchScope
import com.tokopedia.topchat.chatsearch.di.ChatSearchViewsModelModule
import com.tokopedia.topchat.stub.chatsearch.view.fragment.ChatSearchFragmentStub
import com.tokopedia.topchat.stub.common.di.FakeBaseAppComponent
import dagger.Component

@ChatSearchScope
@Component(
        modules = [ChatSearchModuleTest::class, ChatSearchViewsModelModule::class, ChatSearchUsecaseStub::class],
        dependencies = [FakeBaseAppComponent::class]
)
interface ChatSearchComponentTest {
    fun inject(chatSearchFragment: ChatSearchFragmentStub)
}