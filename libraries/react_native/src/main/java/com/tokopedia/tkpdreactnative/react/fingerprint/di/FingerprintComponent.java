package com.tokopedia.tkpdreactnative.react.fingerprint.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.FingerPrintUIHelper;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.FingerprintDialogConfirmation;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.SingleAuthPaymentDialog;

import dagger.Component;

/**
 * Created by zulfikarrahman on 4/2/18.
 */

@FingerprintScope
@Component(modules = FingerprintModule.class, dependencies = BaseAppComponent.class)
public interface FingerprintComponent {
    void inject(FingerPrintUIHelper fingerPrintUIHelper);
    void inject(FingerprintDialogConfirmation fingerprintDialogConfirmation);
    void inject(SingleAuthPaymentDialog singleAuthPaymentDialog);
}
