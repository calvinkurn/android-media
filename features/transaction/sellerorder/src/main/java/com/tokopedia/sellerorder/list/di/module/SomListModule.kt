package com.tokopedia.sellerorder.list.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.di.scope.SomListScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 2019-08-27.
 */

@Module
@SomListScope
class SomListModule {

    @Provides
    fun provideGraphQlRepository(@ApplicationContext context: Context): GraphqlRepository {
        GraphqlClient.init(context)
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
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}