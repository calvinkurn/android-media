package com.tokopedia.universal_sharing.di

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.universal_sharing.data.api.ExtractBranchLinkApi
import com.tokopedia.universal_sharing.data.model.BranchLinkErrorResponse
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkDataStore
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkRepository
import com.tokopedia.universal_sharing.model.ImageGeneratorModel
import com.tokopedia.universal_sharing.model.ImagePolicyResponse
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.universal_sharing.usecase.ImagePolicyUseCase
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.url.TokopediaUrl
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
        val baseUrl = TokopediaUrl.getInstance().WEB
        return retrofitBuilder.client(
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(ErrorResponseInterceptor(BranchLinkErrorResponse::class.java))
                .build()
        ).baseUrl(baseUrl).build().create(ExtractBranchLinkApi::class.java)
    }

    @Module
    abstract class BindUniversalShareModule {
        @Binds
        abstract fun bindRepo(dataStore: ExtractBranchLinkDataStore): ExtractBranchLinkRepository

        @Binds
        abstract fun bindImageGeneratorUseCase(imageGeneratorUseCase: ImageGeneratorUseCase): GraphqlUseCase<ImageGeneratorModel>

        @Binds
        abstract fun bindImagePolicyUseCase(imagePolicyUseCase: ImagePolicyUseCase): CoroutineUseCase<String, ImagePolicyResponse>

        @Binds
        abstract fun bindAffiliateUseCase(affiliateEligibilityCheckUseCase: AffiliateEligibilityCheckUseCase): GraphqlUseCase<GenerateAffiliateLinkEligibility>
    }
}
