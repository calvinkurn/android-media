package com.tokopedia.buyerorderdetail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class BuyerOrderDetailModule {

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @BuyerOrderDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)

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
    fun provideMultiRequestGraphqlUseCase(
        graphqlRepository: GraphqlRepository
    ): MultiRequestGraphqlUseCase = MultiRequestGraphqlUseCase(graphqlRepository)
}
