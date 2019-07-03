package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.domain.interactor.RechargePushEventRecommendationUseCase;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.constants.DigitalCartCrossSellingType;
import com.tokopedia.digital.newcart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDefaultContract;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

public class DigitalCartDefaultPresenter extends DigitalBaseCartPresenter<DigitalCartDefaultContract.View> implements DigitalCartDefaultContract.Presenter {

    private RechargePushEventRecommendationUseCase rechargePushEventRecommendationUseCase;

    @Inject
    public DigitalCartDefaultPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                       DigitalAnalytics digitalAnalytics,
                                       DigitalModuleRouter digitalModuleRouter,
                                       ICartDigitalInteractor cartDigitalInteractor,
                                       UserSession userSession,
                                       DigitalCheckoutUseCase digitalCheckoutUseCase,
                                       DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase,
                                       DigitalPostPaidLocalCache digitalPostPaidLocalCache,
                                       RechargePushEventRecommendationUseCase rechargePushEventRecommendationUseCase) {
        super(digitalAddToCartUseCase,
                digitalAnalytics,
                digitalModuleRouter,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase,
                digitalInstantCheckoutUseCase,
                digitalPostPaidLocalCache);

        this.rechargePushEventRecommendationUseCase = rechargePushEventRecommendationUseCase;
    }

    @Override
    protected void renderCrossSellingCart(CartDigitalInfoData cartDigitalInfoData) {
        super.renderCrossSellingCart(cartDigitalInfoData);
        switch (cartDigitalInfoData.getCrossSellingType()) {
            case DigitalCartCrossSellingType.DEALS:
                getView().inflateDealsPage(cartDigitalInfoData, getView().getCartPassData());
                break;
            case DigitalCartCrossSellingType.MYBILLS:
                getView().inflateMyBillsSubscriptionPage(cartDigitalInfoData, getView().getCartPassData());
                break;
            default:
                getView().showCartView();
                getView().hideFullPageLoading();
                renderBaseCart(cartDigitalInfoData);
                break;
        }
    }

    public void trackRechargePushEventRecommendation(int categoryId, String actionType) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParam(categoryId, actionType), null);
    }
}
