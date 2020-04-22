package com.tokopedia.talk.feature.reading.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reading.presentation.activity.TalkReadingActivity
import com.tokopedia.talk.feature.reading.presentation.fragment.TalkReadingFragment
import dagger.Component

@Component(modules = [TalkReadingViewModelModule::class], dependencies = [TalkComponent::class])
@TalkReadingScope
interface TalkReadingComponent {
    fun inject(talkReadingFragment: TalkReadingFragment)
}