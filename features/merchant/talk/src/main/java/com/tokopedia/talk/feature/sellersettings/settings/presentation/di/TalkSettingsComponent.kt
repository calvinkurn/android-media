package com.tokopedia.talk.feature.sellersettings.settings.presentation.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.sellersettings.settings.presentation.fragment.TalkSettingsFragment
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.TalkSmartReplyDetailScope
import dagger.Component

@Component(dependencies = [TalkComponent::class])
@TalkSmartReplyDetailScope
interface TalkSettingsComponent {
    fun inject(talkSettingsFragment: TalkSettingsFragment)
}