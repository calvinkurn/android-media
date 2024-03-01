package com.tokopedia.tokopedianow.shoppinglist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.tokopedianow.shoppinglist.di.scope.ShoppingListScope
import com.tokopedia.tokopedianow.shoppinglist.helper.ResourceStringProvider
import com.tokopedia.tokopedianow.shoppinglist.helper.ResourceStringProviderImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ShoppingListModule(
    private val context: Context
) {
    @ShoppingListScope
    @Provides
    fun provideContext() : Context = context

    @ShoppingListScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ShoppingListScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface = UserSession(context)

    @ShoppingListScope
    @Provides
    fun provideGetCoroutineSingleRecommendationUseCase(
        @ApplicationContext context: Context,
        coroutineGqlRepository: GraphqlRepository
    ): GetSingleRecommendationUseCase = GetSingleRecommendationUseCase(context, coroutineGqlRepository)

    @ShoppingListScope
    @Provides
    fun provideResourceStringProvider(context: Context): ResourceStringProvider = ResourceStringProviderImpl(context)
}
