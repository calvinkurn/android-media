package com.tokopedia.checkout.view.view.shipment;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.toppay.ThanksTopPayData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

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

    private final CheckoutUseCase checkoutUseCase;
    private final CompositeSubscription compositeSubscription;
    private final GetThanksToppayUseCase getThanksToppayUseCase;
    private final CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase;
    private final GetShipmentAddressFormUseCase getShipmentAddressFormUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;

    private List<ShipmentCartItem> shipmentCartItemList;
    private RecipientAddressModel recipientAddressModel;
    private PromoCodeAppliedData promoCodeAppliedData;
    private CartPromoSuggestion cartPromoSuggestion;
    private ShipmentCostModel shipmentCostModel;
    private List<DataCheckoutRequest> dataCheckoutRequestList;
    private List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestDataList;
    private CheckoutData checkoutData;

    @Inject
    public ShipmentPresenter(CompositeSubscription compositeSubscription,
                             CheckoutUseCase checkoutUseCase,
                             GetThanksToppayUseCase getThanksToppayUseCase,
                             CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                             GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                             CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase) {
        this.compositeSubscription = compositeSubscription;
        this.checkoutUseCase = checkoutUseCase;
        this.getThanksToppayUseCase = getThanksToppayUseCase;
        this.checkPromoCodeCartShipmentUseCase = checkPromoCodeCartShipmentUseCase;
        this.getShipmentAddressFormUseCase = getShipmentAddressFormUseCase;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
    }

    @Override
    public void attachView(ShipmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
        checkoutUseCase.unsubscribe();
        getThanksToppayUseCase.unsubscribe();
        checkPromoCodeCartShipmentUseCase.unsubscribe();
        getShipmentAddressFormUseCase.unsubscribe();
        checkPromoCodeCartListUseCase.unsubscribe();
    }

    @Override
    public void processCheckShipmentPrepareCheckout() {
        getView().showLoading();
        com.tokopedia.abstraction.common.utils.TKPDMapParam<String, String> paramGetShipmentForm = new com.tokopedia.abstraction.common.utils.TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                getGeneratedAuthParamNetwork(paramGetShipmentForm));

        compositeSubscription.add(
                getShipmentAddressFormUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(
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
                                        for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                                            if (groupAddress.isError() || groupAddress.isWarning())
                                                isEnableCheckout = false;
                                            for (GroupShop groupShop : groupAddress.getGroupShop()) {
                                                if (groupShop.isError() || groupShop.isWarning())
                                                    isEnableCheckout = false;
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
                                }
                        )
        );
    }

    private TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return originParams == null
                ?
                AuthUtil.generateParamsNetwork(
                        getView().getActivity(), SessionHandler.getLoginID(getView().getActivity()),
                        GCMHandler.getRegistrationId(getView().getActivity())
                )
                :
                AuthUtil.generateParamsNetwork(
                        getView().getActivity(), originParams,
                        SessionHandler.getLoginID(getView().getActivity()),
                        GCMHandler.getRegistrationId(getView().getActivity())
                );
    }

    @Override
    public void processCheckout() {
        CheckoutRequest checkoutRequest = generateCheckoutRequest(
                promoCodeAppliedData != null && promoCodeAppliedData.getPromoCode() != null ?
                        promoCodeAppliedData.getPromoCode() : "", 0
        );
        if (checkoutRequest != null) {
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
    public void checkPromoShipment() {
        CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest =
                new CheckPromoCodeCartShipmentRequest.Builder()
                        .promoCode(promoCodeAppliedData.getPromoCode())
                        .data(promoCodeCartShipmentRequestDataList)
                        .build();

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(CheckPromoCodeCartShipmentUseCase.PARAM_CARTS,
                new Gson().toJson(checkPromoCodeCartShipmentRequest));
        param.put(CheckPromoCodeCartShipmentUseCase.PARAM_PROMO_LANG,
                CheckPromoCodeCartShipmentUseCase.PARAM_VALUE_LANG_ID);
        param.put(CheckPromoCodeCartShipmentUseCase.PARAM_PROMO_SUGGESTED,
                CheckPromoCodeCartShipmentUseCase.PARAM_VALUE_NOT_SUGGESTED);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartShipmentUseCase.PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO,
                getGeneratedAuthParamNetwork(param));

        compositeSubscription.add(
                checkPromoCodeCartShipmentUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<CheckPromoCodeCartShipmentResult>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().renderErrorCheckPromoShipmentData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                            }

                            @Override
                            public void onNext(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
                                if (!checkPromoCodeCartShipmentResult.isError()) {
                                    getView().renderCheckPromoShipmentDataSuccess(checkPromoCodeCartShipmentResult);
                                } else {
                                    getView().renderErrorCheckPromoShipmentData(checkPromoCodeCartShipmentResult.getErrorMessage());
                                }
                            }
                        })
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

    @Override
    public void processCheckPromoCodeFromSuggestedPromo(String promoCode) {
        getView().showLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("promo_code", promoCode);
        param.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO,
                getGeneratedAuthParamNetwork(param));
        compositeSubscription.add(
                checkPromoCodeCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<CheckPromoCodeCartListResult>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().hideLoading();
                            }

                            @Override
                            public void onNext(CheckPromoCodeCartListResult
                                                       promoCodeCartListData) {
                                getView().hideLoading();
                                if (!promoCodeCartListData.isError()) {
                                    getView().renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                                } else {
                                    getView().renderErrorCheckPromoCodeFromSuggestedPromo(promoCodeCartListData.getErrorMessage());
                                }
                            }
                        })
        );
    }

    private CheckoutRequest generateCheckoutRequest(String promoCode, int isDonation) {
        if (dataCheckoutRequestList == null) {
            // Show error cant checkout
            return null;
        }

        return new CheckoutRequest.Builder()
                .promoCode(promoCode)
                .isDonation(isDonation)
                .data(dataCheckoutRequestList)
                .build();
    }

    @Override
    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
    }

    @Override
    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
    }

    @Override
    public List<ShipmentCartItem> getShipmentCartItemList() {
        return shipmentCartItemList;
    }

    @Override
    public void setShipmentCartItemList(List<ShipmentCartItem> recipientCartItemList) {
        this.shipmentCartItemList = recipientCartItemList;
    }

    @Override
    public PromoCodeAppliedData getPromoCodeAppliedData() {
        return promoCodeAppliedData;
    }

    @Override
    public void setPromoCodeAppliedData(PromoCodeAppliedData promoCodeAppliedData) {
        this.promoCodeAppliedData = promoCodeAppliedData;
    }

    @Override
    public CartPromoSuggestion getCartPromoSuggestion() {
        return cartPromoSuggestion;
    }

    @Override
    public void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        this.cartPromoSuggestion = cartPromoSuggestion;
    }

    @Override
    public CheckoutData getCheckoutData() {
        return checkoutData;
    }

    @Override
    public void setCheckoutData(CheckoutData checkoutData) {
        this.checkoutData = checkoutData;
    }

    @Override
    public List<DataCheckoutRequest> getDataCheckoutRequestList() {
        return dataCheckoutRequestList;
    }

    @Override
    public void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList) {
        this.dataCheckoutRequestList = dataCheckoutRequestList;
    }

    @Override
    public List<CheckPromoCodeCartShipmentRequest.Data> getPromoCodeCartShipmentRequestData() {
        return promoCodeCartShipmentRequestDataList;
    }

    @Override
    public void setPromoCodeCartShipmentRequestData(
            List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestData
    ) {
        this.promoCodeCartShipmentRequestDataList = promoCodeCartShipmentRequestData;
    }

    @Override
    public ShipmentCostModel getShipmentCostModel() {
        return shipmentCostModel;
    }

    @Override
    public void setShipmentCostModel(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;
    }
}
