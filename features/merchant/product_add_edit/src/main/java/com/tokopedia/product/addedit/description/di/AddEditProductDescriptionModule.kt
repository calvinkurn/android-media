package com.tokopedia.product.addedit.description.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.product.addedit.description.data.remote.ProductVariantService
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDb
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepositoryImpl
import com.tokopedia.product.manage.common.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideInterceptors(loggingInterceptor: HttpLoggingInterceptor,
                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor) =
            mutableListOf(loggingInterceptor, commonErrorResponseInterceptor)

    @AddEditProductDescriptionScope
    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository =
            RestRequestInteractor.getInstance().restRepository.apply {
        updateInterceptors(interceptors, context)
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideGetYoutubeVideoUseCase(restRepository: RestRepository): GetYoutubeVideoDetailUseCase {
        return GetYoutubeVideoDetailUseCase(restRepository)
    }

}
