package com.tokopedia.search.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.mps.MPSFragment
import com.tokopedia.search.result.mps.bottomsheet.MPSShimmeringFragment
import com.tokopedia.search.result.shop.presentation.fragment.ShopListFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchFragmentModule {

    @SearchScope
    @Binds
    internal abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ShopListFragment::class)
    internal abstract fun getShopListFragment(fragment: ShopListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MPSFragment::class)
    internal abstract fun getMPSFragment(fragment: MPSFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MPSShimmeringFragment::class)
    internal abstract fun getMPSShimmeringFragment(fragment: MPSShimmeringFragment): Fragment
}
