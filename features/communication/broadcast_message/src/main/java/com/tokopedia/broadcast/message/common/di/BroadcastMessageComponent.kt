package com.tokopedia.broadcast.message.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.broadcast.message.view.fragment.BroadcastMessageListFragment
import dagger.Component

@BroadcastMessageScope
@Component(modules = arrayOf(BroadcastMessageModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface BroadcastMessageComponent {
    fun inject(fragment: BroadcastMessageListFragment)
}