package com.tokopedia.checkout.view.view.shipment;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.toppay.ThanksTopPayData;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Irfan Khoirul on 24/04/18.
 */

public class ShipmentPresenter extends BaseDaggerPresenter<ShipmentContract.View>
        implements ShipmentContract.Presenter {

    private final ICartListInteractor cartListInteractor;
    private final CheckoutUseCase checkoutUseCase;
    private final CompositeSubscription compositeSubscription;
    private final GetThanksToppayUseCase getThanksToppayUseCase;
    private final CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase;

    @Inject
    public ShipmentPresenter(ICartListInteractor cartListInteractor,
                             CompositeSubscription compositeSubscription,
                             CheckoutUseCase checkoutUseCase,
                             GetThanksToppayUseCase getThanksToppayUseCase,
                             CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase) {
        this.cartListInteractor = cartListInteractor;
        this.compositeSubscription = compositeSubscription;
        this.checkoutUseCase = checkoutUseCase;
        this.getThanksToppayUseCase = getThanksToppayUseCase;
        this.checkPromoCodeCartShipmentUseCase = checkPromoCodeCartShipmentUseCase;
    }

    @Override
    public void attachView(ShipmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void processCheckShipmentPrepareCheckout() {
        getView().showLoading();
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");
        cartListInteractor.getShipmentForm(
                new Subscriber<CartShipmentAddressFormData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().hideLoading();
                    }

                    @Override
                    public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                        boolean isEnableCheckout = true;
                        getView().hideLoading();
                        for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                            if (groupAddress.isError() || groupAddress.isWarning()) {
                                isEnableCheckout = false;
                            }
                            for (GroupShop groupShop : groupAddress.getGroupShop()) {
                                if (groupShop.isError() || groupShop.isWarning()) {
                                    isEnableCheckout = false;
                                }
                            }
                        }
                        if (isEnableCheckout) {
                            getView().renderCheckShipmentPrepareCheckoutSuccess();
                        } else {
                            getView().renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                                    cartShipmentAddressFormData
                            );
                        }
                    }
                }, AuthUtil.generateParamsNetwork(MainApplication.getAppContext(), paramGetShipmentForm)
        );
    }

    @Override
    public void processCheckout(CheckoutRequest checkoutRequest) {
        getView().showLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckoutUseCase.PARAM_CARTS, checkoutRequest);
        compositeSubscription.add(
                checkoutUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberCheckoutCart())
        );
    }

    @Override
    public void processVerifyPayment(String transactionId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetThanksToppayUseCase.PARAM_TRANSACTION_ID, transactionId);
        compositeSubscription.add(
                getThanksToppayUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberThanksTopPay())
        );
    }

    @Override
    public void checkPromoShipment(Subscriber<CheckPromoCodeCartShipmentResult> subscriber,
                                   CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartShipmentUseCase.PARAM_CARTS, checkPromoCodeCartShipmentRequest);
        compositeSubscription.add(
                checkPromoCodeCartShipmentUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @NonNull
    private Subscriber<ThanksTopPayData> getSubscriberThanksTopPay() {
        return new Subscriber<ThanksTopPayData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ThanksTopPayData thanksTopPayData) {
                getView().renderThanksTopPaySuccess("Pembayaran Berhasil");
            }
        };
    }

    @NonNull
    private Subscriber<CheckoutData> getSubscriberCheckoutCart() {
        return new Subscriber<CheckoutData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                e.printStackTrace();

            }

            @Override
            public void onNext(CheckoutData checkoutData) {
                getView().hideLoading();
                if (!checkoutData.isError()) {
                    getView().renderCheckoutCartSuccess(checkoutData);
                } else {
                    getView().renderCheckoutCartError(checkoutData.getErrorMessage());
                }
            }
        };
    }

}
