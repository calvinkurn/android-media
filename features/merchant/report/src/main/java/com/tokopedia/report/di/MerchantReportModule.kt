package com.tokopedia.report.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.mediauploader.di.MediaUploaderModule
import com.tokopedia.report.R
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@MerchantReportScope
@Module(includes = [ViewModelModule::class, MediaUploaderModule::class])
class MerchantReportModule {

    @MerchantReportScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @MerchantReportScope
    @Provides
    @Named("product_report_reason")
    fun getProductReportReasonQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_report_reason)

    @MerchantReportScope
    @Provides
    @Named("product_report_submit")
    fun getProductReportSubmitMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_submit_report)
}