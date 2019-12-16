package com.tokopedia.rechargegeneral.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.topupbills.di.CommonTopupBillsScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class RechargeGeneralModule {

    @RechargeGeneralScope
    @Provides
    fun provideGetProductUseCase(graphqlRepository: GraphqlRepository): GetProductUseCase = GetProductUseCase(graphqlRepository)

}
