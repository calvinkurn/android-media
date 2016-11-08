package com.tokopedia.core.deposit.interactor;

import com.tokopedia.core.deposit.model.SummaryWithdraw;

/**
 * Created by Nisie on 4/11/16.
 */
public interface DepositCacheInteractor {

    void getSummaryDepositCache(GetSummaryDepositCacheListener listener);

    void setSummaryDepositCache(SummaryWithdraw result);

    interface GetSummaryDepositCacheListener {
        void onSuccess(SummaryWithdraw result);

        void onError(Throwable e);
    }
}
