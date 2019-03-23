package com.tokopedia.tokocash.pendingcashback.data;

import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class PendingCashbackMapper implements Func1<PendingCashbackEntity, PendingCashback> {

    @Inject
    public PendingCashbackMapper() {
    }

    @Override
    public PendingCashback call(PendingCashbackEntity pendingCashbackEntity) {
        if (pendingCashbackEntity != null) {
            int amount = 0;
            try {
                amount = Integer.parseInt(pendingCashbackEntity.getBalance());
            } catch (NumberFormatException ignored) { }
            PendingCashback pendingCashback = new PendingCashback();
            pendingCashback.setAmount(amount);
            pendingCashback.setAmountText(pendingCashbackEntity.getBalanceText());
            return pendingCashback;
        }
        throw new RuntimeException("Error");
    }
}
