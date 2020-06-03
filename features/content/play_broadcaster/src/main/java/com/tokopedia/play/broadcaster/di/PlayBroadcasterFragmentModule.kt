package com.tokopedia.play.broadcaster.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.*
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory
import com.tokopedia.play.broadcaster.view.fragment.PlayLiveBroadcastFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 27/05/20
 */
@Module
abstract class PlayBroadcasterFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastSetupBottomSheet::class)
    abstract fun getBroadcastSetupBottomSheet(fragment: PlayBroadcastSetupBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayEtalasePickerFragment::class)
    abstract fun getPlayEtalasePickerFragment(fragment: PlayEtalasePickerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayEtalaseDetailFragment::class)
    abstract fun getPlayEtalaseDetailFragment(fragment: PlayEtalaseDetailFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayPrepareBroadcastFragment::class)
    abstract fun getPlayPrepareBroadcastFragment(fragment: PlayPrepareBroadcastFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastSummaryFragment::class)
    abstract fun getPlayBroadcastSummaryFragment(fragment: PlayBroadcastSummaryFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayLiveBroadcastFragment::class)
    abstract fun getPlayLiveBroadcastFragment(fragment: PlayLiveBroadcastFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayCoverTitleSetupFragment::class)
    abstract fun getPlayCoverTitleSetupFragment(fragment: PlayCoverTitleSetupFragment): Fragment
}