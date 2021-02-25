package com.tokopedia.topchat.stub.chatsearch.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatsearch.di.ChatSearchScope
import com.tokopedia.topchat.chatsearch.di.ChatSearchViewsModelModule
import com.tokopedia.topchat.stub.chatsearch.view.fragment.ChatSearchFragmentStub
import dagger.Component

@ChatSearchScope
@Component(
        modules = [ChatSearchModuleTest::class, ChatSearchViewsModelModule::class, ChatSearchUsecaseStub::class],
        dependencies = [BaseAppComponent::class]
)
interface ChatSearchComponentTest {
    fun inject(chatSearchFragment: ChatSearchFragmentStub)
}