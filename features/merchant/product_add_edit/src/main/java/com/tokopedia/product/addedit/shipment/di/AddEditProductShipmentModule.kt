package com.tokopedia.product.addedit.shipment.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDb
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepositoryImpl
import com.tokopedia.product.manage.common.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [AddEditProductShipmentModelModule::class])
@AddEditProductShipmentScope
class AddEditProductShipmentModule {

    @AddEditProductShipmentScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

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
    fun provideProductDraftRepository(
            draftDataSource: AddEditProductDraftDataSource,
            userSession: UserSessionInterface
    ): AddEditProductDraftRepository {
        return AddEditProductDraftRepositoryImpl(draftDataSource, userSession)
    }
}
