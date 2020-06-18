package com.tokopedia.play.broadcaster.di.setup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.*
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PlayBroadcastSetupFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastSetupBottomSheet::class)
    abstract fun getBroadcastSetupBottomSheet(fragment: PlayBroadcastSetupBottomSheet): Fragment
    /**
     * Etalase Setup
     */
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
    @FragmentKey(PlayEtalaseListFragment::class)
    abstract fun getPlayEtalaseListFragment(fragment: PlayEtalaseListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlaySearchSuggestionsFragment::class)
    abstract fun getPlaySearchSuggestionsFragment(fragment: PlaySearchSuggestionsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlaySearchResultFragment::class)
    abstract fun getPlaySearchResultFragment(fragment: PlaySearchResultFragment): Fragment

    /**
     * Cover
     */
    @Binds
    @IntoMap
    @FragmentKey(PlayCoverTitleSetupFragment::class)
    abstract fun getPlayCoverTitleSetupFragment(fragment: PlayCoverTitleSetupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastChooseCoverBottomSheet::class)
    abstract fun getPlayBroadcastChooseCoverBottomSheet(fragment: PlayBroadcastChooseCoverBottomSheet): Fragment
}