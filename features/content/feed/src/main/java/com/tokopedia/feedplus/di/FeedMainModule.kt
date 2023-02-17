package com.tokopedia.feedplus.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@Module
class FeedMainModule(private val activityContext: Context) {

    @Provides
    @FeedMainScope
    fun provideActivityContext(): Context = activityContext

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

}
