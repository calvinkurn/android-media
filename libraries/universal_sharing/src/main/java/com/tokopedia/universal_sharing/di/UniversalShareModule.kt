package com.tokopedia.universal_sharing.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.universal_sharing.data.api.ExtractBranchLinkApi
import com.tokopedia.universal_sharing.data.model.BranchLinkErrorResponse
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkDataStore
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.lang.Error

@Module(includes = [UniversalShareModule.BindUniversalShareModule::class])
class UniversalShareModule {

    @Provides
    fun provideExtractBranchLinkApi(retrofitBuilder: Retrofit.Builder, logger: HttpLoggingInterceptor): ExtractBranchLinkApi {
        return retrofitBuilder.client(OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(ErrorResponseInterceptor(BranchLinkErrorResponse::class.java))
                .build()).baseUrl("https://www.tokopedia.com/").build().create(ExtractBranchLinkApi::class.java)
    }

    @Module
    abstract class BindUniversalShareModule {
        @Binds
        abstract fun bindRepo(dataStore: ExtractBranchLinkDataStore): ExtractBranchLinkRepository
    }
}