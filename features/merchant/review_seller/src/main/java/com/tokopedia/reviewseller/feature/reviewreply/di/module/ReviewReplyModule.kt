package com.tokopedia.reviewseller.feature.reviewreply.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.reviewseller.common.util.GQL_GET_TEMPLATE_LIST
import com.tokopedia.reviewseller.feature.reviewreply.di.scope.ReviewReplyScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ReviewReplyScope
@Module(includes = [ReviewReplyViewModelModule::class])
class ReviewReplyModule {

    @ReviewReplyScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ReviewReplyScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ReviewReplyScope
    @Provides
    @Named(GQL_GET_TEMPLATE_LIST)
    fun getReviewTemplateList(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_review_template_list)
    }
}