package com.tokopedia.talk.feature.inbox.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxContainerFragment
import dagger.Component

@Component(dependencies = [TalkComponent::class])
@TalkInboxScope
interface TalkInboxContainerComponent {
    fun inject(talkInboxContainerFragment: TalkInboxContainerFragment)
}