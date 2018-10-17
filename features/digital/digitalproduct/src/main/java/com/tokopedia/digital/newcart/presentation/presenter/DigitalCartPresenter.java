package com.tokopedia.digital.newcart.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Attributes;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.Field;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartContract;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DigitalCartPresenter extends BaseDaggerPresenter<DigitalCartContract.View> implements DigitalCartContract.Presenter {
    private DigitalCheckoutUseCase digitalCheckoutUseCase;
    private DigitalAddToCartUseCase digitalAddToCartUseCase;
    private DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase;
    private DigitalRouter digitalRouter;
    private UserSession userSession;
    private ICartDigitalInteractor cartDigitalInteractor;

    @Inject
    public DigitalCartPresenter(DigitalCheckoutUseCase digitalCheckoutUseCase,
                                DigitalAddToCartUseCase digitalAddToCartUseCase,
                                DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase,
                                DigitalRouter digitalRouter,
                                UserSession userSession, ICartDigitalInteractor cartDigitalInteractor) {
        this.digitalCheckoutUseCase = digitalCheckoutUseCase;
        this.digitalAddToCartUseCase = digitalAddToCartUseCase;
        this.digitalInstantCheckoutUseCase = digitalInstantCheckoutUseCase;
        this.digitalRouter = digitalRouter;
        this.userSession = userSession;
        this.cartDigitalInteractor = cartDigitalInteractor;
    }

    @Override
    public void onViewCreated() {
        getView().hideContent();
        getView().showLoading();
        RequestParams requestParams = digitalAddToCartUseCase.createRequestParams(
                getRequestBodyAtcDigital(), getView().getIdemPotencyKey());
        digitalAddToCartUseCase.execute(requestParams, getSubscriberAddToCart());
    }

    @Override
    public void processPatchOtpCart(String categoryId) {
        CheckoutDataParameter checkoutDataParameter = getView().getCheckoutData();
        RequestBodyOtpSuccess requestBodyOtpSuccess = new RequestBodyOtpSuccess();
        requestBodyOtpSuccess.setType("cart");
        requestBodyOtpSuccess.setId(checkoutDataParameter.getCartId());
        com.tokopedia.digital.cart.data.entity.requestbody.otpcart.Attributes attributes =
                new com.tokopedia.digital.cart.data.entity.requestbody.otpcart.Attributes();
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        requestBodyOtpSuccess.setAttributes(attributes);

        Map<String, String> paramGetCart = new HashMap<>();
        paramGetCart.put("category_id", categoryId);
        cartDigitalInteractor.patchCartOtp(
                requestBodyOtpSuccess,
                getView().getGeneratedAuthParamNetwork(userSession.getUserId(), userSession.getDeviceId(), paramGetCart),
                getSubscriberCartInfo()
        );
    }


    @NonNull
    private Subscriber<CartDigitalInfoData> getSubscriberCartInfo() {
        return new Subscriber<CartDigitalInfoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else {
                    getView().closeViewWithMessageAlert(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().setCartDigitalInfo(cartDigitalInfoData);
                renderCart(cartDigitalInfoData);
            }
        };
    }

    @NonNull
    private RequestBodyAtcDigital getRequestBodyAtcDigital() {
        RequestBodyAtcDigital requestBodyAtcDigital = new RequestBodyAtcDigital();
        List<Field> fieldList = new ArrayList<>();
        String clientNumber = getView().getClientNumber();
        if (clientNumber != null && !clientNumber.isEmpty()) {
            Field field = new Field();
            field.setName("client_number");
            field.setValue(clientNumber);
            fieldList.add(field);
        }
        Attributes attributes = new Attributes();
        attributes.setDeviceId(5);
        attributes.setInstantCheckout(getView().isInstantCheckout());
        attributes.setIpAddress(DeviceUtil.getLocalIpAddress());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setUserId(Integer.parseInt(userSession.getUserId()));
        attributes.setProductId(getView().getProductId());
        attributes.setFields(fieldList);
        if (GlobalConfig.isSellerApp()) {
            attributes.setReseller(true);
        }
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        attributes.setShowSubscribeFlag(true);
        attributes.setThankyouNative(true);
        attributes.setThankyouNativeNew(true);
        requestBodyAtcDigital.setType("add_cart");
        requestBodyAtcDigital.setAttributes(attributes);
        return requestBodyAtcDigital;
    }

    private Subscriber<CartDigitalInfoData> getSubscriberAddToCart() {
        return new Subscriber<CartDigitalInfoData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    getView().closeViewWithMessageAlert(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().closeViewWithMessageAlert(e.getMessage());
                } else {
                    getView().closeViewWithMessageAlert(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartDigitalInfoData cartDigitalInfoData) {
                getView().showContent();
                getView().hideLoading();
                getView().setCartDigitalInfo(cartDigitalInfoData);
                if (cartDigitalInfoData.getAttributes().isNeedOtp()) {
                    getView().interruptRequestTokenVerification();
                } else {
                    renderCart(cartDigitalInfoData);
                }
            }
        };
    }

    private void renderCart(CartDigitalInfoData cartDigitalInfoData) {
        if (getView().getCheckoutPassData().getInstantCheckout().equals("1") &&  !cartDigitalInfoData.isForceRenderCart()) {
//            processToInstantCheckout();
        } else {
            switch (cartDigitalInfoData.getAttributes().getCrossSellingType()) {
                case 1:
                    break;
                default:
                    getView().inflateDefaultCartPage(cartDigitalInfoData);
                    break;
            }
        }
    }
}
