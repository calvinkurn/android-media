package com.tokopedia.play.broadcaster.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.fragment.PlayLiveBroadcastFragment
import dagger.Component

/**
 * Created by jegul on 20/05/20
 */
@Component(
        dependencies = [BaseAppComponent::class],
        modules = [PlayBroadcasterViewModelModule::class, PlayBroadcasterModule::class, PlayBroadcasterFragmentModule::class]
)
@PlayBroadcasterScope
interface PlayBroadcasterComponent {

    fun inject(broadcastActivity: PlayBroadcastActivity)
}