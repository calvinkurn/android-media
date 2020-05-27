package com.tokopedia.play.broadcaster.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastInteractionFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import dagger.Component

/**
 * Created by jegul on 20/05/20
 */
@Component(modules = [PlayBroadcasterViewModelModule::class, PlayBroadcasterModule::class],
        dependencies = [BaseAppComponent::class])
@PlayBroadcasterScope
interface PlayBroadcasterComponent {

    fun inject(broadcastActivity: PlayBroadcastActivity)

    fun inject(prepareBroadcastFragment: PlayPrepareBroadcastFragment)

    fun inject(broadcastSetupFragment: PlayBroadcastSetupFragment)

    fun inject(broadcastFragment: PlayBroadcastFragment)

    fun inject(broadcastInteractionFragment: PlayBroadcastInteractionFragment)
}