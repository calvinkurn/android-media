package com.tokopedia.saldodetails.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.presenter.SaldoHoldInfoPresenter
import com.tokopedia.saldodetails.usecase.GetHoldInfoUsecase
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@Module
class SaldoDetailsModule {

    @SaldoDetailsScope
    @Provides
    internal fun providesUserSession(@SaldoDetailsScope context: Context): UserSession {
        return UserSession(context)
    }

    @SaldoDetailsScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SaldoDetailsScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @SaldoDetailsScope
    @Provides
    fun saldoHoldInfoPresenter(usecase: GetHoldInfoUsecase): SaldoHoldInfoPresenter {
        return SaldoHoldInfoPresenter(usecase)
    }

}
