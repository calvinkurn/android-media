package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.cart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.cart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.util.DigitalAnalytics;
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

    @Override
    protected void renderCrossSellingCart(CartDigitalInfoData cartDigitalInfoData) {
        super.renderCrossSellingCart(cartDigitalInfoData);
        switch (cartDigitalInfoData.getCrossSellingType()) {
            case 1:
                getView().inflateDealsPage(cartDigitalInfoData, getView().getCartPassData());
                break;
            case 2:
                getView().inflateMyBillsSubscriptionPage(cartDigitalInfoData, getView().getCartPassData());
                break;
            default:
                getView().showCartView();
                getView().hideFullPageLoading();
                renderBaseCart(cartDigitalInfoData);
                break;
        }
    }
}
