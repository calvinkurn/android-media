package com.tokopedia.saldodetails.interactor;

import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;

public interface DepositCacheInteractor {

    /*void getSummaryDepositCache(GetSummaryDepositCacheListener listener);

    void setSummaryDepositCache(GqlDepositSummaryResponse result);*/

    /*void setUsableBuyerSaldoBalanceCache(GqlSaldoBalanceResponse gqlSaldoBalanceResponse);

    void getUsableBuyerSaldoBalanceCache(GetUsableSaldoBalanceCacheListener listener);

    void setUsableSellerSaldoBalanceCache(GqlSaldoBalanceResponse gqlSaldoBalanceResponse);

    void getUsableSellerSaldoBalanceCache(GetUsableSaldoBalanceCacheListener listener);*/

    /*interface GetSummaryDepositCacheListener {
        void onSuccess(GqlDepositSummaryResponse result);

        void onError(Throwable e);
    }*/

    interface GetUsableSaldoBalanceCacheListener {
        void onSuccess(GqlSaldoBalanceResponse result);

        void onError(Throwable e);
    }
}
