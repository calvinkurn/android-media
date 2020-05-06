package com.tokopedia.withdraw.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.constant.WithdrawalDomainConstant.GQL_QUERY_GET_BANK_ACCOUNT
import com.tokopedia.withdraw.constant.WithdrawalDomainConstant.GQL_QUERY_GET_REKENING_PREMIUM
import com.tokopedia.withdraw.constant.WithdrawalDomainConstant.GQL_QUERY_VALIDATE_POP_UP_WITHDRAWAL
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_QUERY_GET_BANK_ACCOUNT)
    fun provideGetBankQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_bank_data)

    @Provides
    @Named(GQL_QUERY_GET_REKENING_PREMIUM)
    fun provideGetRekeningPremiumData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_check_premium_account)

    @Provides
    @Named(GQL_QUERY_VALIDATE_POP_UP_WITHDRAWAL)
    fun provideValidatePopUpWithdrawal(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_popup)


}

