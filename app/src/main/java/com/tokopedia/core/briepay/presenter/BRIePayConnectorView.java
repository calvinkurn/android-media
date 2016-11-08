package com.tokopedia.core.briepay.presenter;

import android.app.DialogFragment;

/**
 * Created by Default05 on 1/12/2016.
 */
public interface BRIePayConnectorView {

    void onVerifyBRI(String tid);
    void onCancelBRI(DialogFragment dialogFragment);

}
