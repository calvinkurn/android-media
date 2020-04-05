package com.tokopedia.product.addedit.description.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.product.addedit.description.data.remote.ProductVariantService
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
    fun provideTickerRetrofit(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl(ProductVariantService.BASE_URL)
                .build()
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideTickerService(retrofit: Retrofit): ProductVariantService {
        return retrofit.create(ProductVariantService::class.java)
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideRestRepository(): RestRepository = RestRequestInteractor.getInstance().restRepository

//    @AddEditProductDescriptionScope
//    @Provides
//    fun provideGetYoutubeVideoUseCase(restRepository: RestRepository): GetYoutubeVideoUseCase {
//        return GetYoutubeVideoUseCase(restRepository)
//    }

}
