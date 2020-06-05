package com.tokopedia.talk_old.inboxtalk.di

import com.tokopedia.talk_old.common.di.TalkComponent
import com.tokopedia.talk_old.inboxtalk.view.activity.InboxTalkActivity
import com.tokopedia.talk_old.inboxtalk.view.fragment.InboxTalkFragment
import dagger.Component

/**
 * @author by nisie on 8/29/18.
 */
@InboxTalkScope
@Component(modules = arrayOf(InboxTalkModule::class), dependencies = arrayOf(TalkComponent::class))
interface InboxTalkComponent {

    fun inject(inboxTalkActivity: InboxTalkActivity)

    fun inject(inboxTalkFragment: InboxTalkFragment)


}