package com.tokopedia.sellerfeedback.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql.core.GqlClient
import com.tokopedia.gql.ktor.KtorClient
import com.tokopedia.gql.toKtorEngine
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.multiplatform.seller.feedback.data.repository.SubmitFeedbackRepository
import com.tokopedia.multiplatform.seller.feedback.data.repository.SubmitFeedbackRepositoryImpl
import com.tokopedia.multiplatform.seller.feedback.domain.SubmitFeedbackUseCase
import com.tokopedia.sellerfeedback.di.scope.SellerFeedbackScope
import com.tokopedia.shared.data.UploadPolicyApiImpl
import com.tokopedia.shared.data.repository.UploadPolicyRepository
import com.tokopedia.shared.data.repository.UploadPolicyRepositoryImpl
import com.tokopedia.shared.domain.GetHostPolicyUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class SellerFeedbackModule {

    @SellerFeedbackScope
    @Provides
    fun getGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerFeedbackScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerFeedbackScope
    @Provides
    fun provideOkHttpClientEngine(
        @UploaderQualifier okHttpClient: OkHttpClient.Builder
    ): KtorClient {
        return KtorClient(
            baseUrl = "https://gql.tokopedia.com/",
            engine = okHttpClient.build().toKtorEngine()
        )
    }

    @SellerFeedbackScope
    @Provides
    fun provideGqlClient(ktorClient: KtorClient): GqlClient {
        return GqlClient(ktorClient)
    }

    @SellerFeedbackScope
    @Provides
    fun provideUploadPolicyRepository(gqlClient: GqlClient): UploadPolicyRepository {
        val uploadPolicyApi = UploadPolicyApiImpl(gqlClient)
        return UploadPolicyRepositoryImpl(uploadPolicyApi)
    }

    @SellerFeedbackScope
    @Provides
    fun provideSubmitFeedbackRepository(gqlClient: GqlClient): SubmitFeedbackRepository {
        return SubmitFeedbackRepositoryImpl(gqlClient)
    }

    @SellerFeedbackScope
    @Provides
    fun provideGetHostPolicyUseCase(uploadPolicyRepository: UploadPolicyRepository): GetHostPolicyUseCase {
        return GetHostPolicyUseCase(uploadPolicyRepository)
    }

    @SellerFeedbackScope
    @Provides
    fun provideSubmitFeedbackUseCase(submitFeedbackRepository: SubmitFeedbackRepository): SubmitFeedbackUseCase {
        return SubmitFeedbackUseCase(submitFeedbackRepository)
    }
}
