package com.tokopedia.talk.feature.sellersettings.smartreply.detail.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.fragment.TalkSmartReplyDetailFragment
import dagger.Component

@Component(modules = [TalkSmartReplyDetailViewModelModule::class], dependencies = [TalkComponent::class])
@TalkSmartReplyDetailScope
interface TalkSmartReplyDetailComponent {
    fun inject(talkSmartReplyDetailFragment: TalkSmartReplyDetailFragment)
}