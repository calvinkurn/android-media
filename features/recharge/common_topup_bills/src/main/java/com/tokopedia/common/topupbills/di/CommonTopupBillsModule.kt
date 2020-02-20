package com.tokopedia.common.topupbills.di

import android.content.Context
import com.google.android.gms.common.internal.service.Common
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by resakemal on 12/08/19.
 */
@Module
class CommonTopupBillsModule {

    @CommonTopupBillsScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @CommonTopupBillsScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @CommonTopupBillsScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @CommonTopupBillsScope
    @Provides
    fun provideCommonTopupBillsAnalytics(): CommonTopupBillsAnalytics {
        return CommonTopupBillsAnalytics()
    }

    @CommonTopupBillsScope
    @Provides
    fun provideDigitalCheckVoucherUseCase(@ApplicationContext context: Context): DigitalCheckVoucherUseCase {
        return DigitalCheckVoucherUseCase(context, GraphqlUseCase())
    }

}