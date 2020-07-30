package com.tokopedia.talk_old.talkdetails.di

import com.tokopedia.talk_old.common.di.TalkComponent

import com.tokopedia.talk_old.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.talk_old.talkdetails.view.fragment.TalkDetailsFragment
import dagger.Component

/**
 * Created by Hendri on 03/09/18.
 */
@TalkDetailsScope
@Component(modules = arrayOf(TalkDetailsModule::class), dependencies = arrayOf(TalkComponent::class))
interface TalkDetailsComponent {

    fun inject(talkDetailsActivity: TalkDetailsActivity)

    fun inject(talkDetailsFragment: TalkDetailsFragment)

}