package com.tokopedia.content.product.picker.seller.di

import com.tokopedia.content.product.picker.seller.data.ProductPickerSellerCommonRepositoryImpl
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on November 14, 2023
 */
@Module
abstract class ProductPickerBindModule {

    @Binds
    abstract fun bindProductPickerSellerCommonRepository(repo: ProductPickerSellerCommonRepositoryImpl): ProductPickerSellerCommonRepository
}
