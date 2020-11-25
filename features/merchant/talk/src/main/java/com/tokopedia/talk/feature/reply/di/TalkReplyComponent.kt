package com.tokopedia.talk.feature.reply.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reply.presentation.fragment.TalkReplyFragment
import dagger.Component

@Component(modules = [TalkReplyViewModelModule::class], dependencies = [TalkComponent::class])
@TalkReplyScope
interface TalkReplyComponent {
    fun inject(talkReplyFragment: TalkReplyFragment)
}