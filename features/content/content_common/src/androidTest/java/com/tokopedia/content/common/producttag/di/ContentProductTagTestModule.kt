package com.tokopedia.content.common.producttag.di

import com.tokopedia.content.common.di.ContentProductTagSampleScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
@Module
class ContentProductTagTestModule(
    private val mockUserSession: UserSessionInterface
) {

    @Provides
    @ContentProductTagSampleScope
    fun provideUserSession(): UserSessionInterface = mockUserSession

    @Provides
    @ContentProductTagSampleScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}