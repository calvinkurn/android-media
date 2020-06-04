package com.tokopedia.talk_old.addtalk.di

import com.tokopedia.talk_old.addtalk.view.activity.AddTalkActivity
import com.tokopedia.talk_old.addtalk.view.fragment.AddTalkFragment
import com.tokopedia.talk_old.common.di.TalkComponent
import dagger.Component

/**
 * @author : Steven 17/09/18
 */
@AddTalkScope
@Component(modules = arrayOf(AddTalkModule::class), dependencies = arrayOf(TalkComponent::class))
interface AddTalkComponent {

    fun inject(addTalkActivity: AddTalkActivity)

    fun inject(addTalkFragment: AddTalkFragment)
}