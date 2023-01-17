package com.tokopedia.tkpd.feed_component.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.feedplus.view.di.FeedContainerScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
@Module
class FeedContainerTestModule(
    private val mockUserSession: UserSessionInterface,
    private val mockRepo: FeedPlusRepository
) {

    @Provides
    fun provideGraphQlRepository(@ApplicationContext context: Context): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideFeedDynamicTabsUseCase(@ApplicationContext context: Context, gqlRepo: GraphqlRepository) =
        GraphqlUseCase<FeedTabs.Response>(gqlRepo).apply {
            setTypeClass(FeedTabs.Response::class.java)
            setGraphqlQuery(GraphqlHelper.loadRawString(context.resources, R.raw.query_feed_tabs))
        }


    @Provides
    @FeedContainerScope
    fun provideUserSessionInterface(): UserSessionInterface = mockUserSession

    @Provides
    @FeedContainerScope
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @FeedContainerScope
    fun provideFeedPlusRepository(): FeedPlusRepository = mockRepo
}
