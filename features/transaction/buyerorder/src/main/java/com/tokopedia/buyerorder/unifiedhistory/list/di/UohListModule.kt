package com.tokopedia.buyerorder.unifiedhistory.list.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by fwidjaja on 10/11/20.
 */
@Module
class UohListModule (val context: Context) {
    @UohListScope
    @Provides
    fun provideContext(): Context = context
    
    @UohListScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @UohListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @UohListScope
    @Provides
    fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @UohListScope
    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
    }
}