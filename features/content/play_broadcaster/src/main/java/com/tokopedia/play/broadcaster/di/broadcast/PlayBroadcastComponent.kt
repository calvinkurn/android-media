package com.tokopedia.play.broadcaster.di.broadcast

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionUtil
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import dagger.Component

/**
 * Created by jegul on 20/05/20
 */
@Component(
        dependencies = [BaseAppComponent::class],
        modules = [PlayBroadcastViewModelModule::class,
            PlayBroadcastModule::class,
            PlayBroadcastFragmentModule::class,
            PlayBroadcastBindModule::class]
)
@PlayBroadcastScope
interface PlayBroadcastComponent {

    @ApplicationContext fun appContext(): Context

    fun playPermissionUtil(): PlayPermissionUtil

    fun playPusher(): PlayPusher

    fun inject(broadcastActivity: PlayBroadcastActivity)
}