package com.tokopedia.tkpd.tokocash;

import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.tokocash.activation.presentation.model.PendingCashback;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class TokoCashPendingCashbackMapper implements Func1<PendingCashback, CashBackData> {

    @Override
    public CashBackData call(PendingCashback pendingCashback) {
        if (pendingCashback != null) {
            CashBackData cashBackData = new CashBackData();
            cashBackData.setAmount(pendingCashback.getAmount());
            cashBackData.setAmountText(pendingCashback.getAmountText());
            return cashBackData;
        }
        throw new RuntimeException("Error");
    }
}
