package com.tokopedia.talk.talkdetails.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.inboxtalk.di.InboxTalkModule
import com.tokopedia.talk.inboxtalk.di.InboxTalkScope

import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.talk.talkdetails.view.fragment.TalkDetailsFragment
import dagger.Component

/**
 * Created by Hendri on 03/09/18.
 */
@TalkDetailsScope
@Component(modules = arrayOf(TalkDetailsModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface TalkDetailsComponent {

    fun inject(talkDetailsActivity: TalkDetailsActivity)

    fun inject(talkDetailsFragment: TalkDetailsFragment)

}