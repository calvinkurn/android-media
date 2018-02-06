package com.tokopedia.tkpd.tokocash;

import com.tokopedia.core.drawer2.data.pojo.topcash.Action;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class TokoCashBalanceMapper implements Func1<BalanceTokoCash, TokoCashData> {

    @Override
    public TokoCashData call(BalanceTokoCash balanceTokoCash) {
        if (balanceTokoCash != null) {
            TokoCashData tokoCashData = new TokoCashData();

            if (balanceTokoCash.getActionBalance() != null) {
                Action action = new Action();
                action.setmAppLinks(balanceTokoCash.getActionBalance().getApplinks());
                action.setmText(balanceTokoCash.getActionBalance().getLabelAction());
                action.setRedirectUrl(balanceTokoCash.getActionBalance().getRedirectUrl());
                action.setmVisibility(balanceTokoCash.getActionBalance().getVisibility());
                tokoCashData.setAction(action);
            }
            tokoCashData.setAbTags(balanceTokoCash.getAbTags());
            tokoCashData.setmAppLinks(balanceTokoCash.getApplinks());
            tokoCashData.setBalance(balanceTokoCash.getBalance());
            tokoCashData.setHoldBalance(balanceTokoCash.getHoldBalance());
            tokoCashData.setLink(balanceTokoCash.getLink());
            tokoCashData.setRaw_balance(balanceTokoCash.getRaw_balance());
            tokoCashData.setRawHoldBalance(balanceTokoCash.getRawHoldBalance());
            tokoCashData.setRawThreshold(balanceTokoCash.getRawThreshold());
            tokoCashData.setRawTotalBalance(balanceTokoCash.getRawTotalBalance());
            tokoCashData.setRedirectUrl(balanceTokoCash.getRedirectUrl());
            tokoCashData.setThreshold(balanceTokoCash.getThreshold());
            tokoCashData.setText(balanceTokoCash.getTitleText());
            tokoCashData.setTotalBalance(balanceTokoCash.getTotalBalance());

            return tokoCashData;
        }
        throw new RuntimeException("Error");
    }
}
