package com.tokopedia.search.result.product.byteio

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class ByteIOTrackingDataFactoryModule {

    @Binds
    @SearchScope
    abstract fun provideByteIOTrackingDataFactory(
        byteIOTrackingDataFactory: ByteIOTrackingDataFactoryImpl,
    ): ByteIOTrackingDataFactory
}
