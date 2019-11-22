package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.common.RechargeAnalytics;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.common_digital.cart.constant.DigitalCartCrossSellingType;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDefaultContract;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

public class DigitalCartDefaultPresenter extends DigitalBaseCartPresenter<DigitalCartDefaultContract.View> implements DigitalCartDefaultContract.Presenter {

    @Inject
    public DigitalCartDefaultPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                       DigitalAnalytics digitalAnalytics,
                                       RechargeAnalytics rechargeAnalytics,
                                       DigitalModuleRouter digitalModuleRouter,
                                       ICartDigitalInteractor cartDigitalInteractor,
                                       UserSession userSession,
                                       DigitalCheckoutUseCase digitalCheckoutUseCase,
                                       DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase) {
        super(digitalAddToCartUseCase,
                digitalAnalytics,
                rechargeAnalytics,
                digitalModuleRouter,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase,
                digitalInstantCheckoutUseCase);
    }

    @Override
    protected void renderCrossSellingCart(CartDigitalInfoData cartDigitalInfoData) {
        super.renderCrossSellingCart(cartDigitalInfoData);
        DigitalSubscriptionParams subParams = getView().getDigitalSubscriptionParams();
        switch (cartDigitalInfoData.getCrossSellingType()) {
            case DigitalCartCrossSellingType.DEALS:
                getView().inflateDealsPage(cartDigitalInfoData, getView().getCartPassData());
                break;
            case DigitalCartCrossSellingType.MYBILLS:
                subParams.setSubscribed(false);
                getView().inflateMyBillsSubscriptionPage(
                        cartDigitalInfoData,
                        getView().getCartPassData(),
                        subParams);
                break;
            case DigitalCartCrossSellingType.SUBSCRIBED:
                subParams.setSubscribed(true);
                getView().inflateMyBillsSubscriptionPage(cartDigitalInfoData,
                        getView().getCartPassData(),
                        subParams);
                break;
            default:
                getView().showCartView();
                getView().hideFullPageLoading();
                renderBaseCart(cartDigitalInfoData);
                renderPostPaidPopUp(cartDigitalInfoData);
                break;
        }
    }
}
