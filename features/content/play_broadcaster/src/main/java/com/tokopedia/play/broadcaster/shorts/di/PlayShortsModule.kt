package com.tokopedia.play.broadcaster.shorts.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Module
class PlayShortsModule(
    private val activityContext: Context
) {

    @Provides
    @PlayShortsScope
    fun provideActivityContext() = activityContext

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @PlayShortsScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
