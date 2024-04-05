package com.tokopedia.content.product.preview.di

import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalytics
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalyticsImpl
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface ProductPreviewBindModule {
    @Binds
    @ProductPreviewScope
    fun bindRepository(repository: ProductPreviewRepositoryImpl): ProductPreviewRepository

    @Binds
    @ProductPreviewScope
    fun bindAnalytic(analytic: ProductPreviewAnalyticsImpl.Factory): ProductPreviewAnalytics.Factory
}
