package com.tokopedia.mlp.di

import android.content.Context

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.mlp.usecase.MerchantLendingUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@MerchantScope
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

}