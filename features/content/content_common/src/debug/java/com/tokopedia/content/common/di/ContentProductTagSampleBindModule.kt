package com.tokopedia.content.common.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.sample.analytic.ContentProductTagSampleAnalyticImpl
import dagger.Binds
import dagger.Module

/**
 * Created by kenny.hadisaputra on 29/08/22
 */
@Module
abstract class ContentProductTagSampleBindModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    abstract fun bindContentProductTagSampleAnalytic(analytic: ContentProductTagSampleAnalyticImpl): ContentProductTagAnalytic
}