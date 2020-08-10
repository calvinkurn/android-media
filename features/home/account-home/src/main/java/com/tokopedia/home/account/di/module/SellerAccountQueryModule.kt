package com.tokopedia.home.account.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.R
import com.tokopedia.home.account.di.scope.SellerAccountScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@SellerAccountScope
@Module
class SellerAccountQueryModule {

    @SellerAccountScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_SELLER_ACCOUNT_HOME)
    fun provideQuerySellerAccount(@SellerAccountScope context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_seller_account_home)
    }

    @SellerAccountScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_TOP_ADS)
    fun provideQueryGqlDeposit(@SellerAccountScope context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_deposit)
    }

    @SellerAccountScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.NEW_QUERY_SALDO_BALANCE)
    fun provideQuerySaldoBalance(@SellerAccountScope context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.new_query_saldo_balance)
    }

    @SellerAccountScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_SHOP_LOCATION)
    fun provideQueryShopLocation(@SellerAccountScope context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_shop_location)
    }
}