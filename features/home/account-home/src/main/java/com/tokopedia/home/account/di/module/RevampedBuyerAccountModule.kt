package com.tokopedia.home.account.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.R
import com.tokopedia.home.account.di.scope.BuyerAccountScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class RevampedBuyerAccountModule {

    @BuyerAccountScope
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @BuyerAccountScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @BuyerAccountScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.NEW_QUERY_BUYER_ACCOUNT_HOME)
    fun provideRawQueryUpdatePhoneEmail(@BuyerAccountScope context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.new_query_buyer_account_home)

    @BuyerAccountScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_USER_REWARDSHORCUT)
    fun provideRawQueryShortcut(@BuyerAccountScope context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_user_rewardshorcut)
}