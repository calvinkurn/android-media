package com.tokopedia.play.broadcaster.di.broadcast

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import dagger.Component

/**
 * Created by jegul on 20/05/20
 */
@Component(
        dependencies = [BaseAppComponent::class],
        modules = [PlayBroadcastViewModelModule::class,
            PlayBroadcastModule::class,
            PlayBroadcastFragmentModule::class]
)
@PlayBroadcastScope
interface PlayBroadcastComponent {

    fun inject(broadcastActivity: PlayBroadcastActivity)
}