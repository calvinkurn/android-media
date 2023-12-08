package com.tokopedia.content.product.picker.seller.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.content.product.picker.ProductSetupFragment
import com.tokopedia.content.product.picker.seller.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductSortBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductSummaryBottomSheet
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
@Module
abstract class ProductPickerFragmentModule {

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
}
