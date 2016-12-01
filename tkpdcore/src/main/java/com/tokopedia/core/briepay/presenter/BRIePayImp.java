package com.tokopedia.core.briepay.presenter;

import android.app.DialogFragment;

/**
 * Created by Default05 on 1/12/2016.
 */
public class BRIePayImp implements BRIePayInterface {

    private BRIePayConnectorView briLogic;

    public BRIePayImp(BRIePayConnectorView brIePayConnectorView){
        briLogic = brIePayConnectorView;
    }

    @Override
    public void canceleBRIPayment(DialogFragment dialogFragment) {
        briLogic.onCancelBRI(dialogFragment);
    }

    @Override
    public void verifyeBRIPayment(String tid) {
        briLogic.onVerifyBRI(tid);
    }
}
