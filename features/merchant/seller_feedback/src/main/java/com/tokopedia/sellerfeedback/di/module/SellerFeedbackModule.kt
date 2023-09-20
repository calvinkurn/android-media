package com.tokopedia.sellerfeedback.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql.core.GqlClient
import com.tokopedia.gql.engine
import com.tokopedia.gql.ktor.KtorClient
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.feedback.data.repository.SubmitFeedbackRepository
import com.tokopedia.seller.feedback.data.repository.SubmitFeedbackRepositoryImpl
import com.tokopedia.seller.feedback.domain.SubmitFeedbackUseCase
import com.tokopedia.sellerfeedback.di.scope.SellerFeedbackScope
import com.tokopedia.shared.data.UploadPolicyApiImpl
import com.tokopedia.shared.data.repository.UploadPolicyRepository
import com.tokopedia.shared.data.repository.UploadPolicyRepositoryImpl
import com.tokopedia.shared.domain.GetHostPolicyUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

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
    fun provideGqlClient(): GqlClient {
        val gqlUrl = "https://gql.tokopedia.com/"
        val ktorClient = KtorClient(baseUrl = gqlUrl, engine = engine)
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
