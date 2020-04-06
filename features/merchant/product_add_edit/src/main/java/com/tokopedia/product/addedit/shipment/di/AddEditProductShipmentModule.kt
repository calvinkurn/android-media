package com.tokopedia.product.addedit.shipment.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.addedit.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.addedit.draft.data.db.AddEditProductDraftDb
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepositoryImpl
import com.tokopedia.product.addedit.draft.data.db.source.AddEditProductDraftDataSource
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

    @AddEditProductShipmentScope
    @Provides
    fun provideProductDraftDb(@ApplicationContext context: Context): AddEditProductDraftDb = AddEditProductDraftDb.getInstance(context)

    @AddEditProductShipmentScope
    @Provides
    fun provideProductDraftDao(draftDb: AddEditProductDraftDb): AddEditProductDraftDao = draftDb.getDraftDao()

    @AddEditProductShipmentScope
    @Provides
    fun provideProductDraftRepository(draftDataSource: AddEditProductDraftDataSource, @ApplicationContext context: Context): AddEditProductDraftRepository {
        return AddEditProductDraftRepositoryImpl(draftDataSource, context)
    }
}
