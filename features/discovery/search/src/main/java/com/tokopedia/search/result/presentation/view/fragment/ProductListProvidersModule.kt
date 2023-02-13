package com.tokopedia.search.result.presentation.view.fragment

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.ScreenNameProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.productfilterindicator.ProductFilterIndicator
import com.tokopedia.search.result.product.productfilterindicator.ProductFilterIndicatorDelegate
import com.tokopedia.search.utils.FragmentProvider
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class ProductListFragmentModule(
    private val productListFragment: ProductListFragment
) {

    @SearchScope
    @Provides
    fun provideFragment(): ProductListFragment = productListFragment
}

@Module(includes = [ProductListFragmentModule::class])
abstract class ProductListProvidersModule {

    @Binds
    @SearchScope
    abstract fun provideParameterListener(provider: ProductListFragment): ProductListParameterListener

    @Binds
    @SearchScope
    abstract fun provideQueryKeyProvider(provider: ProductListFragment): QueryKeyProvider

    @Binds
    @SearchScope
    abstract fun provideSearchParameterProvider(provider: ProductListFragment): SearchParameterProvider

    @Binds
    @SearchScope
    abstract fun provideClassNameProvider(provider: ProductListFragment): ClassNameProvider

    @Binds
    @SearchScope
    abstract fun provideViewUpdater(viewUpdater: RecyclerViewUpdater): ViewUpdater

    @Binds
    @SearchScope
    abstract fun provideProductFilterIndicator(productFilterIndicator: ProductFilterIndicatorDelegate): ProductFilterIndicator

    @Binds
    @SearchScope
    abstract fun provideScreenNameProvider(provider: ProductListFragment): ScreenNameProvider

    @Binds
    @SearchScope
    abstract fun provideFragmentProvider(provider: ProductListFragment): FragmentProvider
}
