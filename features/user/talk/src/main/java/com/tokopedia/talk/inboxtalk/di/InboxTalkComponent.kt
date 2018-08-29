package com.tokopedia.talk.inboxtalk.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.inboxtalk.activity.InboxTalkActivity
import com.tokopedia.talk.inboxtalk.fragment.InboxTalkFragment
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