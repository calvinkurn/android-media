package com.tokopedia.saldodetails.interactor;

import com.tokopedia.saldodetails.response.model.SummaryWithdraw;

public interface DepositCacheInteractor {

    void getSummaryDepositCache(GetSummaryDepositCacheListener listener);

    void setSummaryDepositCache(SummaryWithdraw result);

    interface GetSummaryDepositCacheListener {
        void onSuccess(SummaryWithdraw result);

        void onError(Throwable e);
    }
}
