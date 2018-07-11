package com.tokopedia.posapp.payment.di;

import com.tokopedia.posapp.di.component.PosAppComponent;
import com.tokopedia.posapp.payment.otp.view.activity.OTPActivity;

import dagger.Component;

/**
 * Created by okasurya on 10/10/17.
 */
@PaymentScope
@Component(modules = {PaymentModule.class}, dependencies = PosAppComponent.class)
public interface PaymentComponent {
    void inject(OTPActivity otpActivity);
}
