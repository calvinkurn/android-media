package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastInteractionFragment
import dagger.Component

/**
 * Created by jegul on 20/05/20
 */
@Component(modules = [PlayBroadcasterViewModelModule::class, PlayBroadcasterModule::class, PlayBroadcasterFragmentModule::class])
interface PlayBroadcasterComponent {

    fun inject(broadcastActivity: PlayBroadcastActivity)

    fun inject(broadcastFragment: PlayBroadcastFragment)

    fun inject(broadcastInteractionFragment: PlayBroadcastInteractionFragment)
}