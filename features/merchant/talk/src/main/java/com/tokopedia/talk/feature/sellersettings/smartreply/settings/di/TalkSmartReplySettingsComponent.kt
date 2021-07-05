package com.tokopedia.talk.feature.sellersettings.smartreply.settings.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.fragment.TalkSmartReplyDetailFragment
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.fragment.TalkSmartReplySettingsFragment
import dagger.Component

@Component(modules = [TalkSmartReplySettingsViewModelModule::class], dependencies = [TalkComponent::class])
@TalkSmartReplySettingsScope
interface TalkSmartReplySettingsComponent {
    fun inject(talkSmartReplySettingsFragment: TalkSmartReplySettingsFragment)
}