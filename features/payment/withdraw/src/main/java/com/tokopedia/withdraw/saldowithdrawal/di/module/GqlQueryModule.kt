package com.tokopedia.withdraw.saldowithdrawal.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_JOIN_REKE_PREM_TNC_QUERY
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_GET_BANK_ACCOUNT
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_GET_REKENING_PREMIUM
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_SUBMIT_WITHDRAWAL
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_VALIDATE_POP_UP_WITHDRAWAL
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_WITHDRAWAL_BANNER
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_QUERY_GET_BANK_ACCOUNT)
    fun provideGetBankQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.swd_query_get_bank_data)

    @Provides
    @Named(GQL_QUERY_WITHDRAWAL_BANNER)
    fun provideGetRekeningBannerQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.swd_query_get_rekening_banner)

    @Provides
    @Named(GQL_QUERY_GET_REKENING_PREMIUM)
    fun provideGetRekeningPremiumData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.swd_query_check_premium_account)

    @Provides
    @Named(GQL_QUERY_VALIDATE_POP_UP_WITHDRAWAL)
    fun provideValidatePopUpWithdrawal(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.swd_query_popup)

    @Provides
    @Named(GQL_QUERY_SUBMIT_WITHDRAWAL)
    fun provideSubmitWithdrawalQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.swd_query_otp_withdrawal)

    @Provides
    @Named(GQL_JOIN_REKE_PREM_TNC_QUERY)
    fun provideJoinRekeingPremiumTncQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.swd_query_join_reke_prem_tnc)


}

