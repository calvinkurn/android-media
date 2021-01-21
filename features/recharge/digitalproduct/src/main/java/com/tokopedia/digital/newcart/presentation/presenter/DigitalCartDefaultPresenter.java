package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.common_digital.common.RechargeAnalytics;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.newcart.constant.DigitalCartCrossSellingType;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDefaultContract;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.presentation.usecase.DigitalAddToCartUseCase;
import com.tokopedia.digital.newcart.presentation.usecase.DigitalGetCartUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

public class DigitalCartDefaultPresenter extends DigitalBaseCartPresenter<DigitalCartDefaultContract.View> implements DigitalCartDefaultContract.Presenter {

    @Inject
    public DigitalCartDefaultPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                       DigitalGetCartUseCase digitalGetCartUseCase,
                                       DigitalAnalytics digitalAnalytics,
                                       RechargeAnalytics rechargeAnalytics,
                                       ICartDigitalInteractor cartDigitalInteractor,
                                       UserSessionInterface userSession,
                                       DigitalCheckoutUseCase digitalCheckoutUseCase) {
        super(digitalAddToCartUseCase,
                digitalGetCartUseCase,
                digitalAnalytics,
                rechargeAnalytics,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase);
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
