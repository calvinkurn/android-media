package com.tokopedia.product.addedit.draft.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.manage.common.feature.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.manage.common.feature.draft.data.db.AddEditProductDraftDb
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepositoryImpl
import com.tokopedia.product.manage.common.feature.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [AddEditProductDraftViewModelModule::class])
class AddEditProductDraftModule {

    @AddEditProductDraftScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @AddEditProductDraftScope
    @Provides
    fun provideProductDraftDb(@ApplicationContext context: Context): AddEditProductDraftDb = AddEditProductDraftDb.getInstance(context)

    @AddEditProductDraftScope
    @Provides
    fun provideProductDraftDao(draftDb: AddEditProductDraftDb): AddEditProductDraftDao = draftDb.getDraftDao()

    @AddEditProductDraftScope
    @Provides
    fun provideProductDraftRepository(
            draftDataSource: AddEditProductDraftDataSource,
            userSession: UserSessionInterface
    ): AddEditProductDraftRepository {
        return AddEditProductDraftRepositoryImpl(draftDataSource, userSession)
    }
}