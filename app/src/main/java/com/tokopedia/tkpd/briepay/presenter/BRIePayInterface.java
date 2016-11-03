package com.tokopedia.tkpd.briepay.presenter;

import android.app.DialogFragment;

/**
 * Created by Default05 on 1/5/2016.
 */
public interface BRIePayInterface {

    void canceleBRIPayment(DialogFragment dialogFragment);
    void verifyeBRIPayment(String tid);


}
