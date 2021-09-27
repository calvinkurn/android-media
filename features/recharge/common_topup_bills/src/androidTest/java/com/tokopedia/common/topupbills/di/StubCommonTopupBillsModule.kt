package com.tokopedia.common.topupbills.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.data.source.ContactDataSource
import com.tokopedia.common.topupbills.data.source.ContactDataSourceStub
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class StubCommonTopupBillsModule {

    @CommonTopupBillsScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

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

    @CommonTopupBillsScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }


    @CommonTopupBillsScope
    @Provides
    fun provideContactDataSource(): ContactDataSource {
        return ContactDataSourceStub()
    }
}