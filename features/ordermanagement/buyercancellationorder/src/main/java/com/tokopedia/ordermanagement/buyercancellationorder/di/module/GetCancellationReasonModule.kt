package com.tokopedia.ordermanagement.buyercancellationorder.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ordermanagement.buyercancellationorder.di.module.GetCancellationReasonViewModelModule
import com.tokopedia.ordermanagement.buyercancellationorder.di.scope.BuyerCancellationOrderScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module(includes = [GetCancellationReasonViewModelModule::class])
class GetCancellationReasonModule {

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @BuyerCancellationOrderScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @BuyerCancellationOrderScope
    @Provides
    fun provideGetCancellationReasonUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<BuyerGetCancellationReasonData.Data> = GraphqlUseCase(graphqlRepository)

    @BuyerCancellationOrderScope
    @Provides
    fun provideBuyerInstantCancelUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<BuyerInstantCancelData.Data> = GraphqlUseCase(graphqlRepository)

    @BuyerCancellationOrderScope
    @Provides
    fun provideBuyerRequestCancelUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<BuyerRequestCancelData.Data> = GraphqlUseCase(graphqlRepository)
}