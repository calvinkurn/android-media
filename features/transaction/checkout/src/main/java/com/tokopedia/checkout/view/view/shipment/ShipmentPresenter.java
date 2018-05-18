package com.tokopedia.checkout.view.view.shipment;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.toppay.ThanksTopPayData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.EditAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCheckoutButtonModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItemModel;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

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
    private final EditAddressUseCase editAddressUseCase;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;
    private final ChangeShippingAddressUseCase changeShippingAddressUseCase;

    private List<ShipmentCartItemModel> shipmentCartItemModelList;
    private RecipientAddressModel recipientAddressModel;
    private PromoCodeAppliedData promoCodeAppliedData;
    private CartPromoSuggestion cartPromoSuggestion;
    private ShipmentCostModel shipmentCostModel;
    private ShipmentCheckoutButtonModel shipmentCheckoutButtonModel;

    private List<DataCheckoutRequest> dataCheckoutRequestList;
    private List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestDataList;
    private List<DataChangeAddressRequest> changeAddressRequestList;
    private CheckoutData checkoutData;

    @Inject
    public ShipmentPresenter(CompositeSubscription compositeSubscription,
                             CheckoutUseCase checkoutUseCase,
                             GetThanksToppayUseCase getThanksToppayUseCase,
                             CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                             GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                             CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                             EditAddressUseCase editAddressUseCase,
                             CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                             ChangeShippingAddressUseCase changeShippingAddressUseCase) {
        this.compositeSubscription = compositeSubscription;
        this.checkoutUseCase = checkoutUseCase;
        this.getThanksToppayUseCase = getThanksToppayUseCase;
        this.checkPromoCodeCartShipmentUseCase = checkPromoCodeCartShipmentUseCase;
        this.getShipmentAddressFormUseCase = getShipmentAddressFormUseCase;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
        this.editAddressUseCase = editAddressUseCase;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
        this.changeShippingAddressUseCase = changeShippingAddressUseCase;
    }

    @Override
    public void attachView(ShipmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
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
                                        getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown_short));
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
        } else {
            getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
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
                getView().hideLoading();
                getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
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
                e.printStackTrace();
                getView().hideLoading();
                getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
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
                                getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
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
            getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown_short));
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
    public List<ShipmentCartItemModel> getShipmentCartItemModelList() {
        return shipmentCartItemModelList;
    }

    @Override
    public void setShipmentCartItemModelList(List<ShipmentCartItemModel> recipientCartItemList) {
        this.shipmentCartItemModelList = recipientCartItemList;
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
    public void setDataCheckoutRequestList(List<DataCheckoutRequest> dataCheckoutRequestList) {
        this.dataCheckoutRequestList = dataCheckoutRequestList;
    }

    @Override
    public void setPromoCodeCartShipmentRequestData(
            List<CheckPromoCodeCartShipmentRequest.Data> promoCodeCartShipmentRequestData
    ) {
        this.promoCodeCartShipmentRequestDataList = promoCodeCartShipmentRequestData;
    }

    @Override
    public void setDataChangeAddressRequestList(List<DataChangeAddressRequest> dataChangeAddressRequestList) {
        this.changeAddressRequestList = dataChangeAddressRequestList;
    }

    @Override
    public ShipmentCostModel getShipmentCostModel() {
        return shipmentCostModel;
    }

    @Override
    public void setShipmentCostModel(ShipmentCostModel shipmentCostModel) {
        this.shipmentCostModel = shipmentCostModel;
    }

    @Override
    public ShipmentCheckoutButtonModel getShipmentCheckoutButtonModel() {
        return shipmentCheckoutButtonModel;
    }

    @Override
    public void setShipmentCheckoutButtonModel(ShipmentCheckoutButtonModel shipmentCheckoutButtonModel) {
        this.shipmentCheckoutButtonModel = shipmentCheckoutButtonModel;
    }

    @Override
    public void editAddressPinpoint(final String latitude, final String longitude, ShipmentCartItemModel shipmentCartItemModel) {
        RequestParams requestParams = generateEditAddressRequestParams(shipmentCartItemModel, latitude, longitude);
        compositeSubscription.add(
                editAddressUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
                            }

                            @Override
                            public void onNext(String stringResponse) {
                                JSONObject response = null;
                                boolean status;
                                try {
                                    response = new JSONObject(stringResponse);
                                    int statusCode = response.getJSONObject(EditAddressUseCase.RESPONSE_DATA)
                                            .getInt(EditAddressUseCase.RESPONSE_IS_SUCCESS);
                                    status = statusCode == 1;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    status = false;
                                }

                                if (response != null && status) {
                                    getView().renderEditAddressSuccess(latitude, longitude);
                                } else {
                                    getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
                                }
                            }
                        })
        );

    }

    @NonNull
    private RequestParams generateEditAddressRequestParams(ShipmentCartItemModel shipmentCartItemModel,
                                                           String addressLatitude, String addressLongitude) {
        TKPDMapParam<String, String> params = getGeneratedAuthParamNetwork(null);

        String addressId = null;
        String addressName = null;
        String addressStreet = null;
        String postalCode = null;
        String districtId = null;
        String cityId = null;
        String provinceId = null;
        String latitude = null;
        String longitude = null;
        String receiverName = null;
        String receiverPhone = null;

        if (shipmentCartItemModel != null && shipmentCartItemModel instanceof ShipmentMultipleAddressCartItemModel) {
            MultipleAddressItemData multipleAddressItemData =
                    ((ShipmentMultipleAddressCartItemModel) shipmentCartItemModel).getMultipleAddressItemData();
            if (multipleAddressItemData != null) {
                addressId = multipleAddressItemData.getAddressId();
                addressName = multipleAddressItemData.getAddressTitle();
                addressStreet = multipleAddressItemData.getAddressStreet();
                postalCode = multipleAddressItemData.getAddressPostalCode();
                districtId = multipleAddressItemData.getDestinationDistrictId();
                cityId = multipleAddressItemData.getCityId();
                provinceId = multipleAddressItemData.getProvinceId();
                latitude = addressLatitude;
                longitude = addressLongitude;
                receiverName = multipleAddressItemData.getAddressReceiverName();
                receiverPhone = multipleAddressItemData.getRecipientPhoneNumber();
            }
        } else {
            addressId = recipientAddressModel.getId();
            addressName = recipientAddressModel.getAddressName();
            addressStreet = recipientAddressModel.getAddressStreet();
            postalCode = recipientAddressModel.getAddressPostalCode();
            districtId = recipientAddressModel.getDestinationDistrictId();
            cityId = recipientAddressModel.getCityId();
            provinceId = recipientAddressModel.getProvinceId();
            latitude = addressLatitude;
            longitude = addressLongitude;
            receiverName = recipientAddressModel.getRecipientName();
            receiverPhone = recipientAddressModel.getRecipientPhoneNumber();
        }

        params.put(EditAddressUseCase.Params.ADDRESS_ID, addressId);
        params.put(EditAddressUseCase.Params.ADDRESS_NAME, addressName);
        params.put(EditAddressUseCase.Params.ADDRESS_STREET, addressStreet);
        params.put(EditAddressUseCase.Params.POSTAL_CODE, postalCode);
        params.put(EditAddressUseCase.Params.DISTRICT_ID, districtId);
        params.put(EditAddressUseCase.Params.CITY_ID, cityId);
        params.put(EditAddressUseCase.Params.PROVINCE_ID, provinceId);
        params.put(EditAddressUseCase.Params.LATITUDE, latitude);
        params.put(EditAddressUseCase.Params.LONGITUDE, longitude);
        params.put(EditAddressUseCase.Params.RECEIVER_NAME, receiverName);
        params.put(EditAddressUseCase.Params.RECEIVER_PHONE, receiverPhone);

        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(params);

        return requestParams;
    }

    @Override
    public void cancelAutoApplyCoupon() {
        compositeSubscription.add(
                cancelAutoApplyCouponUseCase.createObservable(RequestParams.create())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
                            }

                            @Override
                            public void onNext(String stringResponse) {
                                boolean resultSuccess = false;
                                try {
                                    JSONObject jsonObject = new JSONObject(stringResponse);
                                    resultSuccess = jsonObject.getJSONObject(CancelAutoApplyCouponUseCase.RESPONSE_DATA)
                                            .getBoolean(CancelAutoApplyCouponUseCase.RESPONSE_SUCCESS);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (resultSuccess) {
                                    getView().renderCancelAutoApplyCouponSuccess();
                                } else {
                                    getView().showToastError(getView().getActivity().getString(R.string.default_request_error_unknown));
                                }

                            }
                        })
        );
    }

    @Override
    public void changeShippingAddress() {
        String changeAddressRequestJsonString = new Gson().toJson(changeAddressRequestList);

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("carts", changeAddressRequestJsonString);
        RequestParams requestParam = RequestParams.create();

        TKPDMapParam<String, String> authParam = AuthUtil.generateParamsNetwork(
                getView().getActivity(), param,
                SessionHandler.getLoginID(getView().getActivity()),
                GCMHandler.getRegistrationId(getView().getActivity()));

        requestParam.putAllString(authParam);

        compositeSubscription.add(
                changeShippingAddressUseCase.createObservable(requestParam)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<SetShippingAddressData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(SetShippingAddressData setShippingAddressData) {

                            }
                        })
        );
    }
}
