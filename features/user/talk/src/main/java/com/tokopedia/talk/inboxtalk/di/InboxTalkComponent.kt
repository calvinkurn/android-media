package com.tokopedia.talk.inboxtalk.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.inboxtalk.InboxTalkActivity
import dagger.Component

/**
 * @author by nisie on 8/29/18.
 */
@InboxTalkScope
@Component(modules = arrayOf(InboxTalkModule::class), dependencies = arrayOf(TalkComponent::class))
interface InboxTalkComponent {

    fun inject(inboxTalkActivity: InboxTalkActivity)

}