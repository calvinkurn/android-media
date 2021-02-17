package com.tokopedia.play.broadcaster.di.setup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayCoverImageChooserBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.CoverCropEditBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.SimpleEditProductBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory
import com.tokopedia.play.broadcaster.view.fragment.setup.cover.PlayCoverSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.setup.etalase.*
import com.tokopedia.play.broadcaster.view.fragment.setup.tags.PlayTitleAndTagsSetupFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PlayBroadcastSetupFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

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
     * Title & Tags
     */
    @Binds
    @IntoMap
    @FragmentKey(PlayTitleAndTagsSetupFragment::class)
    abstract fun getPlayTitleAndTagsSetupFragment(fragment: PlayTitleAndTagsSetupFragment): Fragment

    /**
     * Edit Container
     */
    @Binds
    @IntoMap
    @FragmentKey(CoverCropEditBottomSheet::class)
    abstract fun getCoverCropEditBottomSheet(fragment: CoverCropEditBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SimpleEditProductBottomSheet::class)
    abstract fun getSimpleEditProductBottomSheet(fragment: SimpleEditProductBottomSheet): Fragment
}