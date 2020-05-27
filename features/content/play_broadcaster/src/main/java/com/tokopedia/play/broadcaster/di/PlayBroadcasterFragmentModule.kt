package com.tokopedia.play.broadcaster.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalaseDetailFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalasePickerFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 27/05/20
 */
@Module
abstract class PlayBroadcasterFragmentModule {

    @Binds
    @PlayBroadcasterScope
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

    @Binds
    @PlayBroadcasterScope
    @IntoMap
    @FragmentKey(PlayBroadcastSetupBottomSheet::class)
    abstract fun getBroadcastSetupBottomSheet(fragment: PlayBroadcastSetupBottomSheet): Fragment

    @Binds
    @PlayBroadcasterScope
    @IntoMap
    @FragmentKey(PlayEtalasePickerFragment::class)
    abstract fun getPlayEtalasePickerFragment(fragment: PlayEtalasePickerFragment): Fragment

    @Binds
    @PlayBroadcasterScope
    @IntoMap
    @FragmentKey(PlayEtalaseDetailFragment::class)
    abstract fun getPlayEtalaseDetailFragment(fragment: PlayEtalaseDetailFragment): Fragment

    @Binds
    @PlayBroadcasterScope
    @IntoMap
    @FragmentKey(PlayPrepareBroadcastFragment::class)
    abstract fun getPlayPrepareBroadcastFragment(fragment: PlayPrepareBroadcastFragment): Fragment
}