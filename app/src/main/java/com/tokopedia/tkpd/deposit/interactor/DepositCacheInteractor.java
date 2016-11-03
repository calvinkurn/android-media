package com.tokopedia.tkpd.deposit.interactor;

import com.tokopedia.tkpd.deposit.model.SummaryWithdraw;

import org.json.JSONObject;

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
