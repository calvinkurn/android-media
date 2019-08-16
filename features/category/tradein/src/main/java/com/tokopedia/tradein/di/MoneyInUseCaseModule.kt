package com.tokopedia.tradein.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tradein.usecase.GetMoneyInAddressUseCase
import com.tokopedia.tradein.usecase.GetMoneyInCourierUseCase
import com.tokopedia.tradein.usecase.GetMoneyInScheduleOptionUseCase
import com.tokopedia.tradein.usecase.MoneyInCheckoutMutationUseCase
import dagger.Module
import dagger.Provides

@MoneyInModuleScope
@Module
class MoneyInUseCaseModule {

    @MoneyInModuleScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @MoneyInModuleScope
    @Provides
    fun provideRxGQLUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @MoneyInModuleScope
    @Provides
    fun getMoneyInAddressUseCase(context: Context, graphqlUseCase: GraphqlUseCase): GetMoneyInAddressUseCase {
        return GetMoneyInAddressUseCase(context, graphqlUseCase)
    }

    @MoneyInModuleScope
    @Provides
    fun getMoneyInCourierUseCase(context: Context, graphqlUseCase: GraphqlUseCase): GetMoneyInCourierUseCase {
        return GetMoneyInCourierUseCase(context, graphqlUseCase)
    }

    @MoneyInModuleScope
    @Provides
    fun getMoneyInScheduleOptionUseCase(context: Context, graphqlUseCase: GraphqlUseCase): GetMoneyInScheduleOptionUseCase {
        return GetMoneyInScheduleOptionUseCase(context, graphqlUseCase)
    }

    @MoneyInModuleScope
    @Provides
    fun moneyInCheckoutMutationUseCase(context: Context, graphqlUseCase: GraphqlUseCase): MoneyInCheckoutMutationUseCase {
        return MoneyInCheckoutMutationUseCase(context, graphqlUseCase)
    }

}