package com.tokopedia.product.addedit.shipment.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [AddEditProductShipmentModelModule::class])
@AddEditProductShipmentScope
class AddEditProductShipmentModule {
    @AddEditProductShipmentScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
