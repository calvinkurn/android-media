package com.tokopedia.broadcast.message.common.di.component

import com.tokopedia.broadcast.message.common.di.module.BroadcastMessagePreviewModule
import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessagePreviewScope
import com.tokopedia.broadcast.message.view.fragment.BroadcastMessagePreviewFragment
import dagger.Component

@BroadcastMessagePreviewScope
@Component(modules = arrayOf(BroadcastMessagePreviewModule::class), dependencies = arrayOf(BroadcastMessageComponent::class))
interface BroadcastMessagePreviewComponent{
    fun inject(fragment: BroadcastMessagePreviewFragment)
}