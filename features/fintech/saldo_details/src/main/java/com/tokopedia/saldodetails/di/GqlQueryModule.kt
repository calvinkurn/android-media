package com.tokopedia.saldodetails.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @SaldoDetailsScope
    @Provides
    @Named(MERCHANT_SALDO_DETAIL_QUERY)
    fun provideMerchantSaldoDetailRawQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_get_merchant_saldo_details)
    }

    @SaldoDetailsScope
    @Provides
    @Named(MERCHANT_CREDIT_DETAIL_QUERY)
    fun provideMerchantCreditDetailRawQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_get_merchant_credit_details)
    }

    @SaldoDetailsScope
    @Provides
    @Named(MERCHANT_SALDO_BALANCE_QUERY)
    fun provideMerchantSaldoBalanceRawQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_saldo_balance)
    }

    @SaldoDetailsScope
    @Provides
    @Named(SALDO_WITHDRAWAL_TICKER_QUERY)
    fun provideSaldoWithdrawalTickerRawQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_withdrawal_ticker)
    }

    @SaldoDetailsScope
    @Provides
    @Named(MERCHANT_CREDIT_LATE_COUNT_QUERY)
    fun provideLateCountQuery(@ApplicationContext context: Context): String{
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_get_merchant_latecount)
    }

    @SaldoDetailsScope
    @Provides
    @Named(Companion.UPDATE_MERCHANT_SALDO_STATUS)
    fun provideUpdateSaldoStatusQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_set_saldo_status)
    }

    @SaldoDetailsScope
    @Provides
    @Named(DEPOSITE_DETAIL_FOR_ALL_QUERY)
    fun provideDepositDetailForAll(@ApplicationContext context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources,
                com.tokopedia.saldodetails.R.raw.query_deposit_details_for_all)
    }

    @SaldoDetailsScope
    @Provides
    @Named(DEPOSITE_ALL_TRANSACTION_QUERY)
    fun provideDepositeAllTransation(@ApplicationContext context: Context): String{
        return  GraphqlHelper.loadRawString(context.resources,
                com.tokopedia.saldodetails.R.raw.query_deposit_all_transaction)
    }



    companion object {
        const val SALDO_WITHDRAWAL_TICKER_QUERY = "saldo_withdrawal_query"
        const val MERCHANT_SALDO_BALANCE_QUERY = "merchant_saldo_balance_query"
        const val MERCHANT_SALDO_DETAIL_QUERY = "merchant saldo detail query"
        const val MERCHANT_CREDIT_DETAIL_QUERY = "merchant credit detail query"
        const val MERCHANT_CREDIT_LATE_COUNT_QUERY   = "merchant credit late count query"
        const val UPDATE_MERCHANT_SALDO_STATUS = "update merchant saldo status"
        const val DEPOSITE_DETAIL_FOR_ALL_QUERY = "query_deposit_details_for_all"
        const val DEPOSITE_ALL_TRANSACTION_QUERY = "query_deposit_all_transaction"
    }
}