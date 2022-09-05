package com.tokopedia.search.result.presentation.view.fragment

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.ViewUpdater
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
}