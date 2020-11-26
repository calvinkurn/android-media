package com.tokopedia.settingbank.di

import android.content.Context
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
    fun provideRawGetUserBankAccount(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_user_bank_account_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_ADD_BANK_ACCOUNT)
    fun provideRawAddBankAccount(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_add_bank_account_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_GET_ADD_BANK_TNC)
    fun provideAddBankAccountTNC(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_add_bank_tnc_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_GET_BANK_LIST)
    fun provideGetBankList(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_bank_list_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_CHECK_BANK_ACCOUNT)
    fun provideCheckAccountNumber(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_check_account_number_v2)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_DELETE_BANK_ACCOUNT)
    fun provideDeleteBankQuery(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_delete_bank_account)

    @SettingBankScope
    @Provides
    @IntoMap
    @StringKey(QUERY_GET_KYC_RESPONSE)
    fun provideGetKYCInfoQuery(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_bank_kyc_check_v2)

}
