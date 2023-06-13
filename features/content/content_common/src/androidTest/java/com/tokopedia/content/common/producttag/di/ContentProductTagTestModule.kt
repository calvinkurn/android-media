package com.tokopedia.content.common.producttag.di

import com.tokopedia.content.common.di.ContentProductTagSampleScope
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
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
    private val mockUserSession: UserSessionInterface,
    private val mockAnalytic: ContentProductTagAnalytic,
) {

    @Provides
    @ContentProductTagSampleScope
    fun provideUserSession(): UserSessionInterface = mockUserSession

    @Provides
    fun bindProductTagAnalytic(): ContentProductTagAnalytic = mockAnalytic

    @Provides
    @ContentProductTagSampleScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}