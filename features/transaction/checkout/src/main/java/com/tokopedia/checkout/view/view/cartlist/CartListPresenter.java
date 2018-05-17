package com.tokopedia.checkout.view.view.cartlist;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.transactiondata.entity.request.RemoveCartRequest;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;
import com.tokopedia.checkout.domain.datamodel.DeleteAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.ResetAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.ResetAndShipmentFormCartData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateToSingleAddressShipmentData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetShipmentFormUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartGetShipmentAddressFormUseCase;
import com.tokopedia.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListPresenter implements ICartListPresenter {
    private final ICartListView view;
    private final GetCartListUseCase getCartListUseCase;
    private final CompositeSubscription compositeSubscription;
    private final DeleteCartUseCase deleteCartUseCase;
    private final DeleteCartGetCartListUseCase deleteCartGetCartListUseCase;
    private final UpdateCartGetShipmentAddressFormUseCase updateCartGetShipmentAddressFormUseCase;
    private final GetShipmentAddressFormUseCase getShipmentAddressFormUseCase;
    private final ResetCartGetCartListUseCase resetCartGetCartListUseCase;
    private final ResetCartGetShipmentFormUseCase resetCartGetShipmentFormUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;

    @Inject
    public CartListPresenter(ICartListView cartListView,
                             GetCartListUseCase getCartListUseCase,
                             DeleteCartUseCase deleteCartUseCase,
                             DeleteCartGetCartListUseCase deleteCartGetCartListUseCase,
                             UpdateCartGetShipmentAddressFormUseCase updateCartGetShipmentAddressFormUseCase,
                             GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                             ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                             ResetCartGetShipmentFormUseCase resetCartGetShipmentFormUseCase,
                             CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                             CompositeSubscription compositeSubscription,
                             CartApiRequestParamGenerator cartApiRequestParamGenerator,
                             CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase) {
        this.view = cartListView;
        this.getCartListUseCase = getCartListUseCase;
        this.compositeSubscription = compositeSubscription;
        this.deleteCartUseCase = deleteCartUseCase;
        this.deleteCartGetCartListUseCase = deleteCartGetCartListUseCase;
        this.updateCartGetShipmentAddressFormUseCase = updateCartGetShipmentAddressFormUseCase;
        this.getShipmentAddressFormUseCase = getShipmentAddressFormUseCase;
        this.resetCartGetCartListUseCase = resetCartGetCartListUseCase;
        this.resetCartGetShipmentFormUseCase = resetCartGetShipmentFormUseCase;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
    }

    @Override
    public void processInitialGetCartData() {
        view.renderLoadGetCartData();
        view.disableSwipeRefresh();


        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                view.getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList())
        );
        compositeSubscription.add(getCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(getSubscriberInitialCartListData())
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processDeleteCart(final CartItemData cartItemData, final boolean addWishList) {
        view.showProgressLoading();
        List<Integer> ids = new ArrayList<>();
        ids.add(cartItemData.getOriginData().getCartId());
        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .cartIds(ids)
                .addWishlist(addWishList ? 1 : 0)
                .build();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("params", new Gson().toJson(removeCartRequest));
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DeleteCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                view.getGeneratedAuthParamNetwork(param));
        compositeSubscription.add(
                deleteCartUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberDeleteCart(cartItemData, addWishList))
        );
    }

    @Override
    public void processDeleteAndRefreshCart(List<CartItemData> removedCartItems, boolean addWishList) {
        view.showProgressLoading();
        List<Integer> ids = new ArrayList<>();
        for (CartItemData cartItemData : removedCartItems) {
            ids.add(cartItemData.getOriginData().getCartId());
        }
        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .cartIds(ids)
                .addWishlist(addWishList ? 1 : 0)
                .build();
        TKPDMapParam<String, String> paramDelete = new TKPDMapParam<>();
        paramDelete.put("params", new Gson().toJson(removeCartRequest));

        TKPDMapParam<String, String> paramGetList = new TKPDMapParam<>();
        paramGetList.put("lang", "id");


        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DeleteCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART,
                view.getGeneratedAuthParamNetwork(paramDelete));
        requestParams.putObject(DeleteCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_CART,
                view.getGeneratedAuthParamNetwork(paramGetList));

        compositeSubscription.add(
                deleteCartGetCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberDeleteAndRefreshCart())
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processToShipmentSingleAddress() {
        view.showProgressLoading();
        List<CartItemData> cartItemDataList = view.getCartDataList();
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }
        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put("carts", new Gson().toJson(updateCartRequestList));

        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(UpdateCartGetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                view.getGeneratedAuthParamNetwork(paramUpdate));
        requestParams.putObject(UpdateCartGetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS,
                view.getGeneratedAuthParamNetwork(paramGetShipmentForm));

        compositeSubscription.add(
                updateCartGetShipmentAddressFormUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberToShipmentSingleAddress())
        );
    }


    @SuppressWarnings("deprecation")
    @Override
    public void processToShipmentMultipleAddress(final RecipientAddressModel selectedAddress) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("lang", "id");
        param.put("isReset", "false");
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING, view.getGeneratedAuthParamNetwork(param)
        );
        compositeSubscription.add(
                getCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<CartListData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                view.hideProgressLoading();
                                e.printStackTrace();
                                if (e instanceof UnknownHostException) {
                                    /* Ini kalau ga ada internet */
                                    view.renderErrorNoConnectionToShipmentMultipleAddress(
                                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                                    );
                                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                                    /* Ini kalau timeout */
                                    view.renderErrorTimeoutConnectionToShipmentMultipleAddress(
                                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                                    );
                                } else if (e instanceof ResponseErrorException) {
                                    /* Ini kalau error dari API kasih message error */
                                    view.renderErrorToShipmentMultipleAddress(e.getMessage());
                                } else if (e instanceof ResponseDataNullException) {
                                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                                    view.renderErrorToShipmentMultipleAddress(e.getMessage());
                                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                                    view.renderErrorHttpToShipmentMultipleAddress(e.getMessage());
                                } else if (e instanceof ResponseCartApiErrorException) {
                                    view.renderErrorToShipmentMultipleAddress(e.getMessage());
                                } else {
                                    /* Ini diluar dari segalanya hahahaha */
                                    view.renderErrorHttpToShipmentMultipleAddress(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }
                            }

                            @Override
                            public void onNext(CartListData cartListData) {
                                view.hideProgressLoading();
                                if (!cartListData.isError())
                                    view.renderToShipmentMultipleAddressSuccess(cartListData, selectedAddress);
                                else
                                    view.renderErrorToShipmentMultipleAddress(cartListData.getErrorMessage());
                            }
                        })
        );
    }

    @Override
    public void reCalculateSubTotal(List<CartItemHolderData> dataList) {
        double subtotalPrice = 0;
        int qty = 0;
        for (CartItemHolderData data : dataList) {
            qty = qty + data.getCartItemData().getUpdatedData().getQuantity();
            subtotalPrice = subtotalPrice
                    + (data.getCartItemData().getUpdatedData().getQuantity()
                    * data.getCartItemData().getOriginData().getPricePlan());
        }

        view.renderDetailInfoSubTotal(String.valueOf(qty),
                CurrencyFormatUtil.convertPriceValueToIdrFormat(((int) subtotalPrice), true));
    }

    @Override
    public void processCheckPromoCodeFromSuggestedPromo(String promoCode) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_CODE, promoCode);
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_LANG,
                CheckPromoCodeCartListUseCase.PARAM_PROMO_LANG);
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
                        .subscribe(getSubscriberCheckPromoCodeFromSuggestion())
        );
    }


    @Override
    public void processToShipmentForm() {
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
                        .subscribe(getSubscriberToShipmentForm())
        );
    }


    @Override
    public void processResetAndRefreshCartData() {
        view.renderLoadGetCartData();
        view.disableSwipeRefresh();
        TKPDMapParam<String, String> paramResetCart = new TKPDMapParam<>();
        paramResetCart.put("lang", "id");
        paramResetCart.put("step", "4");

        TKPDMapParam<String, String> paramGetCart = new TKPDMapParam<>();
        paramGetCart.put("lang", "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ResetCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART,
                view.getGeneratedAuthParamNetwork(paramResetCart));
        requestParams.putObject(ResetCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_CART,
                view.getGeneratedAuthParamNetwork(paramGetCart));

        compositeSubscription.add(
                resetCartGetCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberResetRefreshCart())
        );
    }


    @Override
    public void processResetThenToShipmentForm() {
        view.showProgressLoading();
        TKPDMapParam<String, String> paramResetCart = new TKPDMapParam<>();
        paramResetCart.put("lang", "id");
        paramResetCart.put("step", "4");

        TKPDMapParam<String, String> paramShipmentForm = new TKPDMapParam<>();
        paramShipmentForm.put("lang", "id");


        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ResetCartGetShipmentFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART,
                view.getGeneratedAuthParamNetwork(paramResetCart));
        requestParams.putObject(ResetCartGetShipmentFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_FORM,
                view.getGeneratedAuthParamNetwork(paramShipmentForm));

        compositeSubscription.add(
                resetCartGetShipmentFormUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberResetCartToShipmentForm())
        );
    }

    @NonNull
    private Subscriber<ResetAndShipmentFormCartData> getSubscriberResetCartToShipmentForm() {
        return new Subscriber<ResetAndShipmentFormCartData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorToShipmentForm(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpToShipmentForm(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }

            }

            @Override
            public void onNext(ResetAndShipmentFormCartData data) {
                view.hideProgressLoading();
                if (data.getResetCartData().isSuccess() && !data.getCartShipmentAddressFormData().isError()) {
                    view.renderToShipmentFormSuccess(data.getCartShipmentAddressFormData());
                } else {
                    String messageError = !data.getCartShipmentAddressFormData().getErrorMessage().isEmpty()
                            ? data.getCartShipmentAddressFormData().getErrorMessage()
                            : data.getResetCartData().getMessage();
                    view.renderErrorToShipmentForm(messageError);
                }
            }
        };
    }

    @NonNull
    private Subscriber<CartListData> getSubscriberInitialCartListData() {
        return new Subscriber<CartListData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderLoadGetCartDataFinish();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionInitialGetCartListData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionInitialGetCartListData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
            /* Ini Http error, misal 403, 500, 404,
             code http errornya bisa diambil
             e.getErrorCode */
                    view.renderErrorHttpInitialGetCartListData(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpInitialGetCartListData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartListData cartListData) {
                view.renderLoadGetCartDataFinish();
                if (cartListData.getCartItemDataList().isEmpty()) {
                    view.renderEmptyCartData();
                } else {
                    view.renderInitialGetCartListDataSuccess(cartListData);
                }

            }
        };
    }

    @NonNull
    private Subscriber<DeleteCartData> getSubscriberDeleteCart(final CartItemData cartItemData, final boolean addWishList) {
        return new Subscriber<DeleteCartData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionActionDeleteCartData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionActionDeleteCartData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpActionDeleteCartData(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpActionDeleteCartData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(DeleteCartData deleteCartData) {
                view.hideProgressLoading();
                if (deleteCartData.isSuccess())
                    view.renderActionDeleteCartDataSuccess(
                            cartItemData, deleteCartData.getMessage(), addWishList
                    );
                else
                    view.renderErrorActionDeleteCartData(deleteCartData.getMessage());
            }
        };
    }

    @NonNull
    private Subscriber<DeleteAndRefreshCartListData> getSubscriberDeleteAndRefreshCart() {
        return new Subscriber<DeleteAndRefreshCartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionActionDeleteCartData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionActionDeleteCartData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
    /* Ini Http error, misal 403, 500, 404,
     code http errornya bisa diambil
     e.getErrorCode */
                    view.renderErrorHttpActionDeleteCartData(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorActionDeleteCartData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpActionDeleteCartData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                view.hideProgressLoading();
                view.renderLoadGetCartDataFinish();
                if (deleteAndRefreshCartListData.getDeleteCartData().isSuccess()
                        && deleteAndRefreshCartListData.getCartListData() != null) {
                    if (deleteAndRefreshCartListData.getCartListData().getCartItemDataList().isEmpty()) {
                        view.renderEmptyCartData();
                    } else {
                        view.renderInitialGetCartListDataSuccess(deleteAndRefreshCartListData.getCartListData());
                    }
                } else {
                    view.renderErrorActionDeleteCartData(
                            deleteAndRefreshCartListData.getDeleteCartData().getMessage()
                    );
                }
            }
        };
    }

    @NonNull
    private Subscriber<UpdateToSingleAddressShipmentData> getSubscriberToShipmentSingleAddress() {
        return new Subscriber<UpdateToSingleAddressShipmentData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof HttpErrorException) {
            /* Ini Http error, misal 403, 500, 404,
             code http errornya bisa diambil
             e.getErrorCode */
                    view.renderErrorHttpToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorToShipmentForm(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpToShipmentForm(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }

            }

            @Override
            public void onNext(UpdateToSingleAddressShipmentData data) {
                view.hideProgressLoading();
                if (data.getUpdateCartData().isSuccess() && !data.getShipmentAddressFormData().isError()) {
                    if (data.getShipmentAddressFormData().getGroupAddress() == null
                            || data.getShipmentAddressFormData().getGroupAddress().isEmpty()) {
                        view.renderNoRecipientAddressShipmentForm(data.getShipmentAddressFormData());
                    } else {
                        view.renderToShipmentFormSuccess(data.getShipmentAddressFormData());
                    }
                } else {
                    String messageError = !data.getShipmentAddressFormData().getErrorMessage().isEmpty()
                            ? data.getShipmentAddressFormData().getErrorMessage()
                            : data.getUpdateCartData().getMessage();
                    view.renderErrorToShipmentForm(messageError);
                }
            }
        };
    }

    @NonNull
    private Subscriber<CartShipmentAddressFormData> getSubscriberToShipmentForm() {
        return new Subscriber<CartShipmentAddressFormData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionToShipmentForm(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorToShipmentForm(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpToShipmentForm(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorToShipmentForm(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpToShipmentForm(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                view.hideProgressLoading();
                if (cartShipmentAddressFormData.isError()) {
                    view.renderErrorToShipmentForm(cartShipmentAddressFormData.getErrorMessage());
                } else {
                    view.renderToShipmentFormSuccess(cartShipmentAddressFormData);
                }
            }
        };
    }

    @NonNull
    private Subscriber<ResetAndRefreshCartListData> getSubscriberResetRefreshCart() {
        return new Subscriber<ResetAndRefreshCartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderLoadGetCartDataFinish();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionInitialGetCartListData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionInitialGetCartListData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    view.renderErrorHttpInitialGetCartListData(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorInitialGetCartListData(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpInitialGetCartListData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(ResetAndRefreshCartListData resetAndRefreshCartListData) {
                view.renderLoadGetCartDataFinish();
                if (!resetAndRefreshCartListData.getResetCartData().isSuccess()) {
                    view.renderErrorInitialGetCartListData(resetAndRefreshCartListData.getResetCartData().getMessage());
                } else {
                    if (resetAndRefreshCartListData.getCartListData().getCartItemDataList().isEmpty()) {
                        view.renderEmptyCartData();
                    } else {
                        view.renderInitialGetCartListDataSuccess(resetAndRefreshCartListData.getCartListData());
                    }
                }

            }

        };
    }

    @NonNull
    private Subscriber<CheckPromoCodeCartListResult> getSubscriberCheckPromoCodeFromSuggestion() {
        return new Subscriber<CheckPromoCodeCartListResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
            }

            @Override
            public void onNext(CheckPromoCodeCartListResult promoCodeCartListData) {
                view.hideProgressLoading();
                if (!promoCodeCartListData.isError())
                    view.renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                else
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(promoCodeCartListData.getErrorMessage());
            }
        };
    }

    @Override
    public void processCancelAutoApply() {
        compositeSubscription.add(cancelAutoApplyCouponUseCase.createObservable(RequestParams.create())
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
                        view.renderCancelAutoApplyCouponError();
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
                            view.renderCancelAutoApplyCouponSuccess();
                        } else {
                            view.renderCancelAutoApplyCouponError();
                        }
                    }
                })
        );
    }
}
