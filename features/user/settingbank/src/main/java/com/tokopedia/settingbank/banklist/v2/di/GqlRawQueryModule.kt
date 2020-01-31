package com.tokopedia.settingbank.banklist.v2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.settingbank.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@SettingBankScope
@Module
class GqlRawQueryModule {

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_GET_USER_BANK_ACCOUNTS)
    fun provideRawGetUserBankAccount(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_user_bank_account_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_ADD_BANK_ACCOUNT)
    fun provideRawAddBankAccount(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_add_bank_account_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_MAKE_ACCOUNT_PRIMARY)
    fun provideRawMakeAccountPrimary(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_make_account_primary_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_GET_ADD_BANK_TNC)
    fun provideAddBankAccountTNC(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_add_bank_tnc_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_GET_BANK_LIST)
    fun provideGetBankList(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_bank_list_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_CHECK_BANK_ACCOUNT)
    fun provideCheckAccountNumber(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_check_account_number_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_DELETE_BANK_ACCOUNT)
    fun provideDeleteBankQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_delete_bank_account)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_GET_KYC_RESPONSE)
    fun provideGetKYCInfoQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_bank_kyc_check_v2)

}
