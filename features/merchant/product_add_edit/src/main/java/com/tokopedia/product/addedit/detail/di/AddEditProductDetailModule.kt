package com.tokopedia.product.addedit.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.domain.interactor.GetSearchShopProductUseCase
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIVERSE_SEARCH_QUERY
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module(includes = [AddEditProductDetailViewModelModule::class])
@AddEditProductDetailScope
class AddEditProductDetailModule {

    @AddEditProductDetailScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @AddEditProductDetailScope
    @Provides
    @Named(UNIVERSE_SEARCH_QUERY)
    fun provideUniverseSearchQuery(
            @ApplicationContext context: Context
    ):String = GraphqlHelper.loadRawString(context.resources, R.raw.gql_universe_search)

    @AddEditProductDetailScope
    @Provides
    fun provideGetSearchShopProductUseCase(graphqlUseCase: MultiRequestGraphqlUseCase,
                                           @Named(UNIVERSE_SEARCH_QUERY) gqlQuery: String): GetSearchShopProductUseCase {
        return GetSearchShopProductUseCase(graphqlUseCase, gqlQuery)
    }

    @AddEditProductDetailScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}