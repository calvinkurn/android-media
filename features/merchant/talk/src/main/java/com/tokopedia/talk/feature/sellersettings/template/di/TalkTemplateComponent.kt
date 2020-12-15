package com.tokopedia.talk.feature.sellersettings.template.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.sellersettings.template.presentation.fragment.TalkEditTemplateFragment
import com.tokopedia.talk.feature.sellersettings.template.presentation.fragment.TalkTemplateListFragment

import dagger.Component

@Component(modules = [TalkTemplateViewModelModule::class], dependencies = [TalkComponent::class])
@TalkTemplateScope
interface TalkTemplateComponent {
    fun inject(talkTemplateListFragment: TalkTemplateListFragment)
    fun inject(talkEditTemplateFragment: TalkEditTemplateFragment)
}