package com.tokopedia.universal_sharing.di

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.universal_sharing.data.api.ExtractBranchLinkApi
import com.tokopedia.universal_sharing.data.model.BranchLinkErrorResponse
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkDataStore
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkRepository
import com.tokopedia.universal_sharing.usecase.ImagePolicyUseCase
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module(includes = [UniversalShareUseCaseModule.BindUniversalShareModule::class])
open class UniversalShareUseCaseModule {

    @Provides
    fun provideExtractBranchLinkApi(retrofitBuilder: Retrofit.Builder, logger: HttpLoggingInterceptor): ExtractBranchLinkApi {
        val baseUrl = "https://www.tokopedia.com/"
        return retrofitBuilder.client(
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(ErrorResponseInterceptor(BranchLinkErrorResponse::class.java))
                .build()
        ).baseUrl(baseUrl).build().create(ExtractBranchLinkApi::class.java)
    }

    @Provides
    fun provideAffiliateUsecase(@ApplicationContext repo: GraphqlRepository): AffiliateEligibilityCheckUseCase {
        return AffiliateEligibilityCheckUseCase(repo)
    }

    @Provides
    fun provideImagePolicyUsecase(@ApplicationContext repo: GraphqlRepository): ImagePolicyUseCase {
        return ImagePolicyUseCase(repo)
    }

    @Module
    abstract class BindUniversalShareModule {
        @Binds
        abstract fun bindRepo(dataStore: ExtractBranchLinkDataStore): ExtractBranchLinkRepository
    }
}
