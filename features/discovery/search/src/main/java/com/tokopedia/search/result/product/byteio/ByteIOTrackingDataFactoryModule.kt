package com.tokopedia.search.result.product.byteio

import dagger.Binds
import dagger.Module

@Module
abstract class ByteIOTrackingDataFactoryModule {

    @Binds
    abstract fun provideByteIOTrackingDataFactory(
        byteIOTrackingDataFactory: ByteIOTrackingDataFactoryImpl,
    ): ByteIOTrackingDataFactory
}
