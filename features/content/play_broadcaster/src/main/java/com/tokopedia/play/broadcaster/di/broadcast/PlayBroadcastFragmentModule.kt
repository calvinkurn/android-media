package com.tokopedia.play.broadcaster.di.broadcast

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayInteractiveLeaderBoardBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayProductLiveBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.*
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 27/05/20
 */
@Module
abstract class PlayBroadcastFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastPrepareFragment::class)
    abstract fun getPlayPrepareBroadcastFragment(fragment: PlayBroadcastPrepareFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastSummaryFragment::class)
    abstract fun getPlayBroadcastSummaryFragment(fragment: PlayBroadcastSummaryFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastUserInteractionFragment::class)
    abstract fun getPlayLiveBroadcastFragment(fragment: PlayBroadcastUserInteractionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayProductLiveBottomSheet::class)
    abstract fun getProductLiveBottomSheet(fragment: PlayProductLiveBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBeforeLiveFragment::class)
    abstract fun getBeforeLiveFragment(fragment: PlayBeforeLiveFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayPermissionFragment::class)
    abstract fun getPermissionFragment(fragment: PlayPermissionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayInteractiveLeaderBoardBottomSheet::class)
    abstract fun getInteractiveLeaderBoardBottomSheet(fragment: PlayInteractiveLeaderBoardBottomSheet): Fragment
}