package com.tokopedia.talk.feature.inbox.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxFragment

import dagger.Component

@Component(modules = [TalkInboxViewModelModule::class, TalkInboxModule::class], dependencies = [TalkComponent::class])
@TalkInboxScope
interface TalkInboxComponent {
    fun inject(talkInboxFragment: TalkInboxFragment)
}