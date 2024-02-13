package com.tokopedia.content.product.preview.di

import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalytics
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalyticsImpl
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ProductPreviewBindModule {
    @Binds
    @ProductPreviewScope
    abstract fun bindRepository(repository: ProductPreviewRepositoryImpl): ProductPreviewRepository

    @Binds
    @ProductPreviewScope
    abstract fun bindAnalytic(analytic: ProductPreviewAnalyticsImpl.Factory): ProductPreviewAnalytics.Factory
}
