package com.tokopedia.mlp.di

import android.content.Context

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.mlp.usecase.MerchantLendingUseCase
import com.tokopedia.mlp.usecase.MerchantLendingUseCaseSPUpdate
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class MerchantLendingUseCaseModule(var context: Context) {

    @Provides
    @MerchantScope
    fun provideContext(): Context {
        return context
    }


    @MerchantScope
    @Provides
    fun providesGraphQlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }


    @MerchantScope
    @Provides
    fun getMerchantLendingUsecase(context: Context, graphqlUseCase: GraphqlUseCase): MerchantLendingUseCase {
        return MerchantLendingUseCase(context, graphqlUseCase)
    }

    @MerchantScope
    @Provides
    fun providesSPUseCase(context: Context, graphqlUseCase: GraphqlUseCase): MerchantLendingUseCaseSPUpdate {
        return MerchantLendingUseCaseSPUpdate(context, graphqlUseCase)
    }
}
