package com.tokopedia.play.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.di.key.FragmentKey
import com.tokopedia.play.view.custom.dialog.InteractiveWinningDialogFragment
import com.tokopedia.play.view.fragment.*
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

    @Binds
    @IntoMap
    @FragmentKey(PlayErrorFragment::class)
    abstract fun getPlayErrorFragment(fragment: PlayErrorFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayFragment::class)
    abstract fun getPlayFragment(fragment: PlayFragment): Fragment

    /**
     * Dialog Fragment
     */
    @Binds
    @IntoMap
    @FragmentKey(InteractiveWinningDialogFragment::class)
    abstract fun getWinningDialogFragment(fragment: InteractiveWinningDialogFragment): Fragment
}