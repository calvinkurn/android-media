package com.tokopedia.talk.feature.write.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.presentation.fragment.TalkWriteFragment
import dagger.Component

@Component(modules = [TalkWriteViewModelModule::class], dependencies = [TalkComponent::class])
@TalkWriteScope
interface TalkWriteComponent {
    fun inject(talkWriteFragment: TalkWriteFragment)
}