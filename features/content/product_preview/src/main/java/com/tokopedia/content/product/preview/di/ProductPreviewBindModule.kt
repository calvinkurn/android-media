package com.tokopedia.content.product.preview.di

import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ProductPreviewBindModule {
    @Binds
    @ProductPreviewScope
    abstract fun bindRepository(repository: ProductPreviewRepositoryImpl): ProductPreviewRepository
}
