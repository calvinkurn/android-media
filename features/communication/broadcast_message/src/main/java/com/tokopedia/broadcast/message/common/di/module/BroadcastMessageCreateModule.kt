package com.tokopedia.broadcast.message.common.di.module

import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessageCreateScope
import com.tokopedia.shop.common.di.ShopCommonModule
import dagger.Module

@BroadcastMessageCreateScope
@Module(includes = arrayOf(ShopCommonModule::class))
class BroadcastMessageCreateModule