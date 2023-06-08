package com.tokopedia.developer_options.presentation.di

import com.tokopedia.developer_options.branchlink.data.repository.BranchLinkRepository
import com.tokopedia.developer_options.branchlink.domain.BranchLinkUseCase
import com.tokopedia.universal_sharing.data.api.BranchLinkApi
import com.tokopedia.universal_sharing.data.repository.BranchLinkDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module(includes = [DevOptModule.BindDevOptModule::class])
class DevOptModule {

    @Provides
    fun provideBranchApi(retrofitBuilder: Retrofit.Builder, logger: HttpLoggingInterceptor): BranchLinkApi {
        val baseUrl = "https://www.tokopedia.com/"
        return retrofitBuilder.client(
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
        ).baseUrl(baseUrl).build().create(BranchLinkApi::class.java)
    }

    @Module
    abstract class BindDevOptModule {
        @Binds
        abstract fun bindRepo(dataStore: BranchLinkDataStore): BranchLinkRepository
    }

}
