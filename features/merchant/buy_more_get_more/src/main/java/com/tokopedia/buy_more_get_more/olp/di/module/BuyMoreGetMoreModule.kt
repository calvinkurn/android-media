package com.tokopedia.buy_more_get_more.olp.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.buy_more_get_more.olp.di.scope.BuyMoreGetMoreScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class BuyMoreGetMoreModule {

    @BuyMoreGetMoreScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @BuyMoreGetMoreScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @BuyMoreGetMoreScope
    @Provides
    fun provideAddToCart(
        graphqlRepository: GraphqlRepository,
        addToCartDataMapper: AddToCartDataMapper,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): AddToCartUseCase {
        return AddToCartUseCase(graphqlRepository, addToCartDataMapper, chosenAddressRequestHelper)
    }

    @BuyMoreGetMoreScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @BuyMoreGetMoreScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper = PermissionCheckerHelper()
}
