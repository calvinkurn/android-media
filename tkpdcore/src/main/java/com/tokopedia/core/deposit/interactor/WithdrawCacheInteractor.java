package com.tokopedia.core.deposit.interactor;

import com.tokopedia.core.deposit.model.WithdrawForm;

/**
 * Created by Nisie on 4/13/16.
 */
public interface WithdrawCacheInteractor {
    void getWithdrawFormCache(GetWithdrawFormCacheListener listener);

    void setWithdrawFormCache(WithdrawForm result);

    interface GetWithdrawFormCacheListener {
        void onSuccess(WithdrawForm result);

        void onError(Throwable e);
    }
}
