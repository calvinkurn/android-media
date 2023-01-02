package com.tokopedia.play.broadcaster.shorts.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSortBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsPreparationFragment
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsSummaryFragment
import com.tokopedia.play.broadcaster.view.bottomsheet.ProductPickerUGCBottomSheet
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Module
abstract class PlayShortsFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PlayShortsPreparationFragment::class)
    abstract fun bindPlayShortsPreparationFragment(fragment: PlayShortsPreparationFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayShortsSummaryFragment::class)
    abstract fun bindPlayShortsSummaryFragment(fragment: PlayShortsSummaryFragment): Fragment

    /** Product Picker */
    @Binds
    @IntoMap
    @FragmentKey(ProductSetupFragment::class)
    abstract fun getPlayBroProductSetupFragment(fragment: ProductSetupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(EtalaseListBottomSheet::class)
    abstract fun getPlayBroEtalaseAndCampaignListBottomSheet(fragment: EtalaseListBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductChooserBottomSheet::class)
    abstract fun getPlayBroProductChooserBottomSheet(fragment: ProductChooserBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductSortBottomSheet::class)
    abstract fun getPlayBroProductSortBottomSheet(fragment: ProductSortBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductSummaryBottomSheet::class)
    abstract fun getPlayBroProductSummaryBottomSheet(fragment: ProductSummaryBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductPickerUGCBottomSheet::class)
    abstract fun getPlayPlaceholderBottomSheet(bottomSheet: ProductPickerUGCBottomSheet): Fragment
}
