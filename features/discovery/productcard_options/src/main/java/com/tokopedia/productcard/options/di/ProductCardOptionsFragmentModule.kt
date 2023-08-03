package com.tokopedia.productcard.options.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.productcard.options.ProductCardOptionsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductCardOptionsFragmentModule {

    @ProductCardOptionsScope
    @Binds
    internal abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ProductCardOptionsFragment::class)
    internal abstract fun getProductCardOptionsFragment(
        fragment: ProductCardOptionsFragment
    ): Fragment
}
