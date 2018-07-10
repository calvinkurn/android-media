package com.tokopedia.paymentmanagementsystem.uploadproofpayment.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

import com.tokopedia.paymentmanagementsystem.uploadproofpayment.view.UploadProofPaymentFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@UploadProofPaymentScope
@Component(modules = UploadProofPaymentModule.class, dependencies = BaseAppComponent.class)
public interface UploadProofPaymentComponent {
    void inject(UploadProofPaymentFragment uploadProofPaymentFragment);
}
