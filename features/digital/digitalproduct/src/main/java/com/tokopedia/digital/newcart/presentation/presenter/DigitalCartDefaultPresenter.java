package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.digital.newcart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDefaultContract;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

public class DigitalCartDefaultPresenter extends DigitalBaseCartPresenter<DigitalCartDefaultContract.View> implements DigitalCartDefaultContract.Presenter {

    @Inject
    public DigitalCartDefaultPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                       DigitalAnalytics digitalAnalytics,
                                       DigitalModuleRouter digitalModuleRouter,
                                       ICartDigitalInteractor cartDigitalInteractor,
                                       UserSession userSession,
                                       DigitalCheckoutUseCase digitalCheckoutUseCase,
                                       DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase,
                                       DigitalPostPaidLocalCache digitalPostPaidLocalCache) {
        super(digitalAddToCartUseCase,
                digitalAnalytics,
                digitalModuleRouter,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase,
                digitalInstantCheckoutUseCase,
                digitalPostPaidLocalCache);
    }
}
