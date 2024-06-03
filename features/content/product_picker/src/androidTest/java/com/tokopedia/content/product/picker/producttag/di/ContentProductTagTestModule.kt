package com.tokopedia.content.product.picker.producttag.di

import com.tokopedia.content.product.picker.di.ContentProductTagSampleScope
import com.tokopedia.content.product.picker.ugc.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.product.picker.ugc.util.preference.ProductTagPreference
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
    private val mockProductTagPreference: ProductTagPreference,
) {

    @Provides
    @ContentProductTagSampleScope
    fun provideUserSession(): UserSessionInterface = mockUserSession

    @Provides
    fun bindProductTagAnalytic(): ContentProductTagAnalytic = mockAnalytic

    @Provides
    fun bindProductTagPreference(): ProductTagPreference = mockProductTagPreference

    @Provides
    @ContentProductTagSampleScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}
