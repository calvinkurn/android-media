package com.tokopedia.product.addedit.preview.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.manage.common.feature.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.manage.common.feature.draft.data.db.AddEditProductDraftDb
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepositoryImpl
import com.tokopedia.product.manage.common.feature.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.UploadStatusDao
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository.UploadStatusRepository
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository.UploadStatusRepositoryImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides

@Module(includes = [AddEditProductPreviewViewModelModule::class])
class AddEditProductPreviewModule {
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @AddEditProductPreviewScope
    @Provides
    fun provideProductDraftDb(@ApplicationContext context: Context): AddEditProductDraftDb = AddEditProductDraftDb.getInstance(context)

    @AddEditProductPreviewScope
    @Provides
    fun provideProductDraftDao(draftDb: AddEditProductDraftDb): AddEditProductDraftDao = draftDb.getDraftDao()

    @AddEditProductPreviewScope
    @Provides
    fun provideUploadStatusDao(draftDb: AddEditProductDraftDb): UploadStatusDao = draftDb.uploadStatusDao()

    @AddEditProductPreviewScope
    @Provides
    fun provideUploadStatusRepository(dao: UploadStatusDao): UploadStatusRepository = UploadStatusRepositoryImpl(dao)

    @AddEditProductPreviewScope
    @Provides
    fun provideProductDraftRepository(
        draftDataSource: AddEditProductDraftDataSource,
        userSession: UserSessionInterface
    ): AddEditProductDraftRepository {
        return AddEditProductDraftRepositoryImpl(draftDataSource, userSession)
    }

    @AddEditProductPreviewScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper = PermissionCheckerHelper()
}
