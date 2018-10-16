package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.digital.cart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.util.DigitalAnalytics;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDefaultContract;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

public class DigitalCartDefaultPresenter extends DigitalBaseCartPresenter<DigitalCartDefaultContract.View> implements DigitalCartDefaultContract.Presenter {
    private DigitalAnalytics digitalAnalytics;

    @Inject
    public DigitalCartDefaultPresenter(DigitalAnalytics digitalAnalytics,
                                       DigitalModuleRouter digitalModuleRouter,
                                       ICartDigitalInteractor cartDigitalInteractor,
                                       UserSession userSession,
                                       DigitalCheckoutUseCase digitalCheckoutUseCase) {
        super(digitalAnalytics, digitalModuleRouter, cartDigitalInteractor, userSession, digitalCheckoutUseCase);
        this.digitalAnalytics = digitalAnalytics;
    }
}
