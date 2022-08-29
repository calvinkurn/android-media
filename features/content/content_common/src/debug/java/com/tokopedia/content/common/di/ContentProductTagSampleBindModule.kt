package com.tokopedia.content.common.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.content.common.producttag.view.fragment.factory.ProductTagFragmentFactory
import dagger.Binds
import dagger.Module

/**
 * Created by kenny.hadisaputra on 29/08/22
 */
@Module
abstract class ContentProductTagSampleBindModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: ProductTagFragmentFactory): FragmentFactory
}