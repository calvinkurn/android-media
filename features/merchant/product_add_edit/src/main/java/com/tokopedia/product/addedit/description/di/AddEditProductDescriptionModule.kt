package com.tokopedia.product.addedit.description.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.addedit.description.data.remote.ProductVariantService
import com.tokopedia.product.addedit.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.addedit.draft.data.db.AddEditProductDraftDb
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepositoryImpl
import com.tokopedia.product.addedit.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit

@Module(includes = [AddEditProductDescriptionViewModelModule::class])
@AddEditProductDescriptionScope
class AddEditProductDescriptionModule {

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @AddEditProductDescriptionScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @AddEditProductDescriptionScope
    @Provides
    fun provideVariantRetrofit(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl(ProductVariantService.BASE_URL).build()
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideVariantService(retrofit: Retrofit): ProductVariantService {
        return retrofit.create(ProductVariantService::class.java)
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideProductDraftDb(@ApplicationContext context: Context): AddEditProductDraftDb = AddEditProductDraftDb.getInstance(context)

    @AddEditProductDescriptionScope
    @Provides
    fun provideProductDraftDao(draftDb: AddEditProductDraftDb): AddEditProductDraftDao = draftDb.getDraftDao()

    @AddEditProductDescriptionScope
    @Provides
    fun provideProductDraftRepository(draftDataSource: AddEditProductDraftDataSource, @ApplicationContext context: Context): AddEditProductDraftRepository {
        return AddEditProductDraftRepositoryImpl(draftDataSource, context)
    }
}
