package com.tokopedia.createpost.producttag.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.createpost.producttag.di.key.FragmentKey
import com.tokopedia.createpost.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.createpost.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.createpost.producttag.view.fragment.factory.ProductTagFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
@Module
abstract class ContentCreationProductTagBindModule {

    /** Fragment */
    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: ProductTagFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ProductTagParentFragment::class)
    abstract fun bindProductTagParentFragment(fragment: ProductTagParentFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductTagSourceBottomSheet::class)
    abstract fun bindProductTagSourceBottomSheet(fragment: ProductTagSourceBottomSheet): Fragment
}