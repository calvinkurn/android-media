package com.tokopedia.broadcast.message.common.di.component

import com.tokopedia.broadcast.message.common.di.module.BroadcastMessageCreateModule
import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessageCreateScope
import dagger.Component

@BroadcastMessageCreateScope
@Component(modules = arrayOf(BroadcastMessageCreateModule::class), dependencies = arrayOf(BroadcastMessageComponent::class))
interface BroadcastMessageCreateComponent{
}