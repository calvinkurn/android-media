package com.tokopedia.talk.feature.sellersettings.settings.presentation.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.sellersettings.settings.presentation.fragment.TalkSettingsFragment
import dagger.Component

@Component(modules = [TalkSettingsViewModelModule::class], dependencies = [TalkComponent::class])
@TalkSettingsScope
interface TalkSettingsComponent {
    fun inject(talkSettingsFragment: TalkSettingsFragment)
}
