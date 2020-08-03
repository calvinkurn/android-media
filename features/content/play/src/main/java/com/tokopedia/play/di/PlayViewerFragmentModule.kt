package com.tokopedia.play.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.di.key.FragmentKey
import com.tokopedia.play.view.fragment.PlayBottomSheetFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.fragment.PlayYouTubeFragment
import com.tokopedia.play.view.fragment.factory.PlayViewerFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 31/07/20
 */
@Module
abstract class PlayViewerFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: PlayViewerFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PlayYouTubeFragment::class)
    abstract fun getPlayYouTubeFragment(fragment: PlayYouTubeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBottomSheetFragment::class)
    abstract fun getPlayBottomSheetFragment(fragment: PlayBottomSheetFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayVideoFragment::class)
    abstract fun getPlayVideoFragment(fragment: PlayVideoFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayUserInteractionFragment::class)
    abstract fun getPlayUserInteractionFragment(fragment: PlayUserInteractionFragment): Fragment
}