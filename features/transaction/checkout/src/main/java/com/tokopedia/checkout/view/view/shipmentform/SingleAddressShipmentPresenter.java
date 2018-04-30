package com.tokopedia.checkout.view.view.shipmentform;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.transaction.common.constant.PickupPointParamConstant;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class SingleAddressShipmentPresenter implements ISingleAddressShipmentPresenter {

    private final ICartSingleAddressView view;
    private final CompositeSubscription compositeSubscription;
    private final GetShipmentAddressFormUseCase getShipmentAddressFormUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;

    @Inject
    public SingleAddressShipmentPresenter(ICartSingleAddressView view,
                                          CompositeSubscription compositeSubscription,
                                          GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                          CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase) {
        this.view = view;
        this.compositeSubscription = compositeSubscription;
        this.getShipmentAddressFormUseCase = getShipmentAddressFormUseCase;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
    }

    Subscriber<CheckPromoCodeCartShipmentResult> getSubscriberCheckPromoShipment() {
        return new Subscriber<CheckPromoCodeCartShipmentResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderErrorCheckPromoShipmentData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
                if (!checkPromoCodeCartShipmentResult.isError()) {
                    view.renderCheckPromoShipmentDataSuccess(checkPromoCodeCartShipmentResult);
                } else {
                    view.renderErrorCheckPromoShipmentData(checkPromoCodeCartShipmentResult.getErrorMessage());
                }
            }
        };
    }

    void processCheckShipmentPrepareCheckout() {
        view.showProgressLoading();
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                view.getGeneratedAuthParamNetwork(paramGetShipmentForm));

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
                                        view.hideProgressLoading();
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
                                            view.renderCheckShipmentPrepareCheckoutSuccess();
                                        } else {
                                            view.renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                                                    cartShipmentAddressFormData
                                            );
                                        }
                                    }
                                }
                        )
        );
    }

    @Override
    public void processCheckPromoCodeFromSuggestedPromo(String promoCode) {
        view.showLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_CODE, promoCode);
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_LANG,
                CheckPromoCodeCartListUseCase.PARAM_VALUE_LANG_ID);
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_SUGGESTED,
                CheckPromoCodeCartListUseCase.PARAM_VALUE_SUGGESTED);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO,
                view.getGeneratedAuthParamNetwork(param));

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
                                view.hideLoading();
                            }

                            @Override
                            public void onNext(CheckPromoCodeCartListResult
                                                       promoCodeCartListData) {
                                view.hideLoading();
                                if (!promoCodeCartListData.isError()) {
                                    view.renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                                } else {
                                    view.renderErrorCheckPromoCodeFromSuggestedPromo(promoCodeCartListData.getErrorMessage());
                                }
                            }
                        })
        );
    }

    @Override
    public HashMap<String, String> generatePickupPointParams(RecipientAddressModel addressAdapterData) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PickupPointParamConstant.PARAM_DISTRICT_ID,
                String.valueOf(addressAdapterData.getDestinationDistrictId()));
        params.put(PickupPointParamConstant.PARAM_PAGE, PickupPointParamConstant.DEFAULT_PAGE);
        params.put(PickupPointParamConstant.PARAM_TOKEN, addressAdapterData.getTokenPickup() != null ? addressAdapterData.getTokenPickup() : "");
        params.put(PickupPointParamConstant.PARAM_UT, addressAdapterData.getUnixTime());

        return params;
    }
}