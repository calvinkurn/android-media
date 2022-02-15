package com.tokopedia.play.broadcaster.di.setup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayCoverImageChooserBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.CoverCropEditBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory
import com.tokopedia.play.broadcaster.view.fragment.setup.cover.PlayCoverSetupFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PlayBroadcastSetupFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

    /**
     * Cover
     */
    @Binds
    @IntoMap
    @FragmentKey(PlayCoverSetupFragment::class)
    abstract fun getPlayCoverTitleSetupFragment(fragment: PlayCoverSetupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayCoverImageChooserBottomSheet::class)
    abstract fun getPlayBroadcastChooseCoverBottomSheet(fragment: PlayCoverImageChooserBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayGalleryImagePickerBottomSheet::class)
    abstract fun getGalleryImagePickerBottomSheet(fragment: PlayGalleryImagePickerBottomSheet): Fragment

    /**
     * Edit Container
     */
    @Binds
    @IntoMap
    @FragmentKey(CoverCropEditBottomSheet::class)
    abstract fun getCoverCropEditBottomSheet(fragment: CoverCropEditBottomSheet): Fragment
}