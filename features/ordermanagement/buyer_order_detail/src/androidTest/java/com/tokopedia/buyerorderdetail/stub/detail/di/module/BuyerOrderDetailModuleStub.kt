package com.tokopedia.buyerorderdetail.stub.detail.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.RuntimeTypeAdapterFactory
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailGson
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailScope
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.stub.common.user.UserSessionStub
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class BuyerOrderDetailModuleStub {

    @BuyerOrderDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSessionStub(context)

    @BuyerOrderDetailScope
    @Provides
    @Named(BuyerOrderDetailMiscConstant.DAGGER_ATC_QUERY_NAME)
    fun provideAtcMultiQuery(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi
        )

    @BuyerOrderDetailScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
        MultiRequestGraphqlUseCase(graphqlRepository)

    @Provides
    @BuyerOrderDetailScope
    @BuyerOrderDetailGson
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(GetBuyerOrderDetailRequestState::class.java, GetBuyerOrderDetailRequestState::type.name, true)
                    .registerSubtype(GetBuyerOrderDetailRequestState.Requesting::class.java)
                    .registerSubtype(GetBuyerOrderDetailRequestState.Success::class.java)
                    .registerSubtype(GetBuyerOrderDetailRequestState.Error::class.java)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(GetOrderResolutionRequestState::class.java, GetOrderResolutionRequestState::type.name, true)
                    .registerSubtype(GetOrderResolutionRequestState.Requesting::class.java)
                    .registerSubtype(GetOrderResolutionRequestState.Success::class.java)
                    .registerSubtype(GetOrderResolutionRequestState.Error::class.java)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(GetP0DataRequestState::class.java, GetP0DataRequestState::type.name, true)
                    .registerSubtype(GetP0DataRequestState.Idle::class.java)
                    .registerSubtype(GetP0DataRequestState.Requesting::class.java)
                    .registerSubtype(GetP0DataRequestState.Success::class.java)
                    .registerSubtype(GetP0DataRequestState.Error::class.java)
            )
            .create()
    }
}
