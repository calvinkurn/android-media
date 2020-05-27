package com.tokopedia.buyerorder.others;

import dagger.Component;

@SingleAuthenticationScope
@Component(modules = SingleAuthenticationModule.class)
public interface SingleAuthenticationComponent {
    void inject(CreditCardFingerPrintUseCase useCase);
}