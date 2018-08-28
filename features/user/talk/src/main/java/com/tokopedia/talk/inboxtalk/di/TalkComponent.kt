package com.tokopedia.talk.inboxtalk.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.talk.inboxtalk.InboxTalkActivity
import dagger.Component

/**
 * @author by nisie on 8/28/18.
 */
@InboxTalkScope
@Component(modules = arrayOf(InboxTalkModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface ShopPageComponent {

    fun inject(inboxTalkActivity: InboxTalkActivity)

}
