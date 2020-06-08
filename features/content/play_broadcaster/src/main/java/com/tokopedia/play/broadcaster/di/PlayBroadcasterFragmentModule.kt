package com.tokopedia.play.broadcaster.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayPrivacyPolicyBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.*
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastUserInteractionFragment
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
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastSetupBottomSheet::class)
    abstract fun getBroadcastSetupBottomSheet(fragment: PlayBroadcastSetupBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayPrivacyPolicyBottomSheet::class)
    abstract fun getPrivacyPolicyBottomSheet(fragment: PlayPrivacyPolicyBottomSheet): Fragment

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
    @FragmentKey(PlayBroadcastSetupFragment::class)
    abstract fun getPlayPrepareBroadcastFragment(fragment: PlayBroadcastSetupFragment): Fragment

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
    @FragmentKey(PlayBroadcastFragment::class)
    abstract fun getParentBroadcastFragment(fragment: PlayBroadcastFragment): Fragment
}