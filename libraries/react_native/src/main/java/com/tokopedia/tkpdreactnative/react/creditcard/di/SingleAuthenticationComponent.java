package com.tokopedia.tkpdreactnative.react.creditcard.di;

import com.tokopedia.tkpdreactnative.react.creditcard.domain.CreditCardFingerPrintUseCase;

import dagger.Component;

@SingleAuthenticationScope
@Component(modules = SingleAuthenticationModule.class)
public interface SingleAuthenticationComponent {
    void inject(CreditCardFingerPrintUseCase useCase);
}