package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.module.PaymentModule;
import com.tokopedia.posapp.di.scope.PaymentScope;
import com.tokopedia.posapp.view.activity.OTPActivity;

import dagger.Component;

/**
 * Created by okasurya on 10/10/17.
 */
@PaymentScope
@Component(modules = {PaymentModule.class}, dependencies = AppComponent.class)
public interface PaymentComponent {
    void inject(OTPActivity otpActivity);
}
