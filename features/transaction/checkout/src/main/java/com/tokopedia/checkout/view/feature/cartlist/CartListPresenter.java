package com.tokopedia.checkout.view.feature.cartlist;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.DeleteAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.ResetAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.WholesalePrice;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceActionField;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCheckout;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceProductCartMapData;
import com.tokopedia.transactiondata.apiservice.CartHttpErrorException;
import com.tokopedia.transactiondata.apiservice.CartResponseDataNullException;
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException;
import com.tokopedia.transactiondata.entity.request.RemoveCartRequest;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListPresenter implements ICartListPresenter {
    private static final float PERCENTAGE = 100.0f;

    private static final String PARAM_PARAMS = "params";
    private static final String PARAM_LANG = "lang";
    private static final String PARAM_STEP = "step";

    public static final int ITEM_CHECKED_ALL_WITHOUT_CHANGES = 0;
    public static final int ITEM_CHECKED_ALL_WITH_CHANGES = 1;
    public static final int ITEM_CHECKED_PARTIAL_SHOP = 3;
    public static final int ITEM_CHECKED_PARTIAL_ITEM = 4;
    public static final int ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM = 5;

    private final ICartListView view;
    private final GetCartListUseCase getCartListUseCase;
    private final CompositeSubscription compositeSubscription;
    private final DeleteCartUseCase deleteCartUseCase;
    private final DeleteCartGetCartListUseCase deleteCartGetCartListUseCase;
    private final UpdateCartUseCase updateCartUseCase;
    private final ResetCartGetCartListUseCase resetCartGetCartListUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private CartListData cartListData;
    private boolean hasPerformChecklistChange;

    @Inject
    public CartListPresenter(ICartListView cartListView,
                             GetCartListUseCase getCartListUseCase,
                             DeleteCartUseCase deleteCartUseCase,
                             DeleteCartGetCartListUseCase deleteCartGetCartListUseCase,
                             UpdateCartUseCase updateCartUseCase,
                             ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                             CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                             CompositeSubscription compositeSubscription,
                             CartApiRequestParamGenerator cartApiRequestParamGenerator,
                             CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                             AddWishListUseCase addWishListUseCase,
                             RemoveWishListUseCase removeWishListUseCase) {
        this.view = cartListView;
        this.getCartListUseCase = getCartListUseCase;
        this.compositeSubscription = compositeSubscription;
        this.deleteCartUseCase = deleteCartUseCase;
        this.deleteCartGetCartListUseCase = deleteCartGetCartListUseCase;
        this.updateCartUseCase = updateCartUseCase;
        this.resetCartGetCartListUseCase = resetCartGetCartListUseCase;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
    }

    @Override
    public void attachView(ICartListView view) {

    }

    @Override
    public void detachView() {
        compositeSubscription.unsubscribe();
        if (addWishListUseCase != null) {
            addWishListUseCase.unsubscribe();
        }
        if (removeWishListUseCase != null) {
            removeWishListUseCase.unsubscribe();
        }
    }

    @Override
    public CartListData getCartListData() {
        return cartListData;
    }

    @Override
    public void processInitialGetCartData() {
        view.renderLoadGetCartData();

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                view.getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList(null))
        );
        compositeSubscription.add(getCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
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
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
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
        paramDelete.put(PARAM_PARAMS, new Gson().toJson(removeCartRequest));

        TKPDMapParam<String, String> paramGetList = new TKPDMapParam<>();
        paramGetList.put(PARAM_LANG, "id");

        List<CartItemData> cartItemDataList = view.getSelectedCartDataList();
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }
        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put(UpdateCartUseCase.PARAM_CARTS, new Gson().toJson(updateCartRequestList));

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DeleteCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART,
                view.getGeneratedAuthParamNetwork(paramDelete));
        requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                view.getGeneratedAuthParamNetwork(paramUpdate));
        requestParams.putObject(DeleteCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_CART,
                view.getGeneratedAuthParamNetwork(paramGetList));

        compositeSubscription.add(
                deleteCartGetCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(getSubscriberDeleteAndRefreshCart())
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processToUpdateCartData() {
        view.showProgressLoading();
        List<CartItemData> cartItemDataList = view.getSelectedCartDataList();
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }
        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put(UpdateCartUseCase.PARAM_CARTS, new Gson().toJson(updateCartRequestList));

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                view.getGeneratedAuthParamNetwork(paramUpdate));

        compositeSubscription.add(
                updateCartUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(getSubscriberToShipmentSingleAddress(cartItemDataList))
        );
    }

    @Override
    public void reCalculateSubTotal(List<CartShopHolderData> dataList) {
        double totalCashback = 0;
        double totalPrice = 0;
        int totalItemQty = 0;
        for (CartShopHolderData cartShopHolderData : dataList) {
            if (cartShopHolderData.isAllSelected() || cartShopHolderData.isPartialSelected()) {
                for (int i = 0; i < cartShopHolderData.getShopGroupData().getCartItemDataList().size(); i++) {
                    CartItemHolderData cartItemHolderData = cartShopHolderData.getShopGroupData().getCartItemDataList().get(i);
                    if (cartItemHolderData.isSelected() && !cartItemHolderData.getCartItemData().isError()) {
                        CartItemData cartItemData = cartShopHolderData.getShopGroupData().getCartItemDataList().get(i).getCartItemData();
                        if (cartItemData != null && cartItemData.getOriginData() != null) {
                            if (cartItemData.getOriginData().getParentId().equals("0")) {
                                cartItemData.getOriginData().setParentId(String.valueOf(i + 1));
                            }
                        }
                    }
                }
                double totalCashbackPerShop = 0;
                double totalPricePerShop = 0;
                int totalItemQtyPerShop = 0;
                Map<String, Double> cashbackWholesalePriceMap = new HashMap<>();
                Map<String, Double> subtotalWholesalePriceMap = new HashMap<>();
                Map<String, CartItemData> cartItemParentIdMap = new HashMap<>();
                for (CartItemHolderData data : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    if (data.isSelected() && !data.getCartItemData().isError()) {
                        if (data.getCartItemData().getOriginData() != null) {
                            String parentId = data.getCartItemData().getOriginData().getParentId();
                            String productId = data.getCartItemData().getOriginData().getProductId();
                            int itemQty = data.getCartItemData().getUpdatedData().getQuantity();
                            if (!TextUtils.isEmpty(parentId) && !parentId.equals("0")) {
                                for (CartItemHolderData dataForQty : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                                    if (!productId.equals(dataForQty.getCartItemData().getOriginData().getProductId()) &&
                                            parentId.equals(dataForQty.getCartItemData().getOriginData().getParentId()) &&
                                            dataForQty.getCartItemData().getOriginData().getPricePlan() ==
                                                    data.getCartItemData().getOriginData().getPricePlan()) {
                                        itemQty += dataForQty.getCartItemData().getUpdatedData().getQuantity();
                                    }
                                }
                            }

                            totalItemQtyPerShop = totalItemQtyPerShop + 1;
                            List<WholesalePrice> wholesalePrices = data.getCartItemData().getOriginData().getWholesalePrice();
                            boolean hasCalculateWholesalePrice = false;
                            if (wholesalePrices != null && wholesalePrices.size() > 0) {
                                double subTotalWholesalePrice = 0;
                                double itemCashback = 0;
                                for (WholesalePrice wholesalePrice : wholesalePrices) {
                                    if (itemQty >= wholesalePrice.getQtyMin()) {
                                        subTotalWholesalePrice = itemQty * wholesalePrice.getPrdPrc();
                                        hasCalculateWholesalePrice = true;
                                        data.getCartItemData().getOriginData().setWholesalePriceFormatted(wholesalePrice.getPrdPrcFmt());
                                        break;
                                    }
                                }
                                if (!hasCalculateWholesalePrice) {
                                    if (itemQty > wholesalePrices.get(wholesalePrices.size() - 1).getPrdPrc()) {
                                        subTotalWholesalePrice = itemQty * wholesalePrices.get(wholesalePrices.size() - 1).getPrdPrc();
                                        data.getCartItemData().getOriginData().setWholesalePriceFormatted(wholesalePrices.get(wholesalePrices.size() - 1).getPrdPrcFmt());
                                    } else {
                                        subTotalWholesalePrice = itemQty * data.getCartItemData().getOriginData().getPricePlan();
                                        data.getCartItemData().getOriginData().setWholesalePriceFormatted(null);
                                    }
                                }
                                if (data.getCartItemData().getOriginData().isCashBack()) {
                                    String cashbackPercentageString = data.getCartItemData().getOriginData().getProductCashBack().replace("%", "");
                                    double cashbackPercentage = Double.parseDouble(cashbackPercentageString);
                                    itemCashback = cashbackPercentage / PERCENTAGE * subTotalWholesalePrice;
                                }
                                if (!subtotalWholesalePriceMap.containsKey(parentId)) {
                                    subtotalWholesalePriceMap.put(parentId, subTotalWholesalePrice);
                                }
                                if (!cashbackWholesalePriceMap.containsKey(parentId)) {
                                    cashbackWholesalePriceMap.put(parentId, itemCashback);
                                }
                            } else {
                                if (!cartItemParentIdMap.containsKey(data.getCartItemData().getOriginData().getParentId())) {
                                    double itemPrice = itemQty * data.getCartItemData().getOriginData().getPricePlan();
                                    if (data.getCartItemData().getOriginData().isCashBack()) {
                                        String cashbackPercentageString = data.getCartItemData().getOriginData().getProductCashBack().replace("%", "");
                                        double cashbackPercentage = Double.parseDouble(cashbackPercentageString);
                                        double itemCashback = cashbackPercentage / PERCENTAGE * itemPrice;
                                        totalCashbackPerShop = totalCashbackPerShop + itemCashback;
                                    }
                                    totalPricePerShop = totalPricePerShop + itemPrice;
                                    data.getCartItemData().getOriginData().setWholesalePriceFormatted(null);
                                    cartItemParentIdMap.put(data.getCartItemData().getOriginData().getParentId(), data.getCartItemData());
                                } else {
                                    CartItemData calculatedHolderData = cartItemParentIdMap.get(data.getCartItemData().getOriginData().getParentId());
                                    if (calculatedHolderData.getOriginData().getPricePlan() != data.getCartItemData().getOriginData().getPricePlan()) {
                                        double itemPrice = itemQty * data.getCartItemData().getOriginData().getPricePlan();
                                        if (data.getCartItemData().getOriginData().isCashBack()) {
                                            String cashbackPercentageString = data.getCartItemData().getOriginData().getProductCashBack().replace("%", "");
                                            double cashbackPercentage = Double.parseDouble(cashbackPercentageString);
                                            double itemCashback = cashbackPercentage / PERCENTAGE * itemPrice;
                                            totalCashbackPerShop = totalCashbackPerShop + itemCashback;
                                        }
                                        totalPricePerShop = totalPricePerShop + itemPrice;
                                        data.getCartItemData().getOriginData().setWholesalePriceFormatted(null);
                                    }
                                }
                            }
                        }
                    }
                }


                if (!subtotalWholesalePriceMap.isEmpty()) {
                    for (Map.Entry<String, Double> item : subtotalWholesalePriceMap.entrySet()) {
                        totalPricePerShop += item.getValue();
                    }
                }

                if (!cashbackWholesalePriceMap.isEmpty()) {
                    for (Map.Entry<String, Double> item : cashbackWholesalePriceMap.entrySet()) {
                        totalCashbackPerShop += item.getValue();
                    }
                }

                totalCashback += totalCashbackPerShop;
                totalPrice += totalPricePerShop;
                totalItemQty += totalItemQtyPerShop;
            }
        }

        String totalPriceString = "-";
        if (totalPrice > 0) {
            totalPriceString = CurrencyFormatUtil.convertPriceValueToIdrFormat(((long) totalPrice), false);
        }
        view.renderDetailInfoSubTotal(String.valueOf(totalItemQty), totalPriceString);
        view.updateCashback(totalCashback);

    }

    @Override
    public void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isAutoApply) {
        view.showProgressLoading();

        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : view.getSelectedCartDataList()) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }

        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put(CheckPromoCodeCartListUseCase.PARAM_CARTS, new Gson().toJson(updateCartRequestList));

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_CODE, promoCode);
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_LANG,
                CheckPromoCodeCartListUseCase.PARAM_PROMO_LANG);
        param.put(CheckPromoCodeCartListUseCase.PARAM_PROMO_SUGGESTED,
                CheckPromoCodeCartListUseCase.PARAM_VALUE_SUGGESTED);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                view.getGeneratedAuthParamNetwork(paramUpdate));
        requestParams.putObject(CheckPromoCodeCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO,
                view.getGeneratedAuthParamNetwork(param));

        compositeSubscription.add(
                checkPromoCodeCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(getSubscriberCheckPromoCodeFromSuggestion(isAutoApply))
        );
    }

    @Override
    public void processResetAndRefreshCartData() {
        view.renderLoadGetCartData();
        view.showProgressLoading();
        TKPDMapParam<String, String> paramResetCart = new TKPDMapParam<>();
        paramResetCart.put(PARAM_LANG, "id");
        paramResetCart.put(PARAM_STEP, "4");

        TKPDMapParam<String, String> paramGetCart = new TKPDMapParam<>();
        paramGetCart.put(PARAM_LANG, "id");

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ResetCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART,
                view.getGeneratedAuthParamNetwork(paramResetCart));
        requestParams.putObject(ResetCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_CART,
                view.getGeneratedAuthParamNetwork(paramGetCart));

        compositeSubscription.add(
                resetCartGetCartListUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(getSubscriberResetRefreshCart())
        );
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
                handleErrorinitCartList(e);
            }

            @Override
            public void onNext(CartListData cartListData) {
                CartListPresenter.this.cartListData = cartListData;
                view.renderLoadGetCartDataFinish();
                if (cartListData.getShopGroupDataList().isEmpty()) {
                    view.renderEmptyCartData(cartListData);
                } else {
                    view.disableSwipeRefresh();
                    view.renderInitialGetCartListDataSuccess(cartListData);
                }
            }
        };
    }

    private void handleErrorinitCartList(Throwable e) {
        if (e instanceof UnknownHostException) {
            view.renderErrorNoConnectionInitialGetCartListData(
                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
            );
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            view.renderErrorTimeoutConnectionInitialGetCartListData(
                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            );
        } else if (e instanceof CartResponseErrorException) {
            view.renderErrorInitialGetCartListData(e.getMessage());
        } else if (e instanceof CartResponseDataNullException) {
            view.renderErrorInitialGetCartListData(e.getMessage());
        } else if (e instanceof CartHttpErrorException) {
            view.renderErrorHttpInitialGetCartListData(e.getMessage());
        } else if (e instanceof ResponseCartApiErrorException) {
            view.renderErrorInitialGetCartListData(e.getMessage());
        } else {
            view.renderErrorHttpInitialGetCartListData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    private void handleErrorCartList(Throwable e) {
        if (e instanceof UnknownHostException) {
            view.renderErrorNoConnectionActionDeleteCartData(
                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
            );
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            view.renderErrorTimeoutConnectionActionDeleteCartData(
                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            );
        } else if (e instanceof CartResponseErrorException) {
            view.renderErrorActionDeleteCartData(e.getMessage());
        } else if (e instanceof CartResponseDataNullException) {
            view.renderErrorActionDeleteCartData(e.getMessage());
        } else if (e instanceof CartHttpErrorException) {
            view.renderErrorHttpActionDeleteCartData(e.getMessage());
        } else if (e instanceof ResponseCartApiErrorException) {
            view.renderErrorActionDeleteCartData(e.getMessage());
        } else {
            view.renderErrorHttpActionDeleteCartData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
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
                handleErrorCartList(e);
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
                view.hideProgressLoading();
                e.printStackTrace();
                handleErrorCartList(e);
            }

            @Override
            public void onNext(DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                view.hideProgressLoading();
                view.renderLoadGetCartDataFinish();
                if (deleteAndRefreshCartListData.getDeleteCartData().isSuccess()
                        && deleteAndRefreshCartListData.getCartListData() != null) {
                    if (deleteAndRefreshCartListData.getCartListData().getShopGroupDataList().isEmpty()) {
                        processInitialGetCartData();
                    } else {
                        CartListPresenter.this.cartListData = deleteAndRefreshCartListData.getCartListData();
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
    private Subscriber<UpdateCartData> getSubscriberToShipmentSingleAddress(List<CartItemData> cartItemDataList) {
        return new Subscriber<UpdateCartData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    view.showToastMessageRed(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    view.showToastMessageRed(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof CartResponseErrorException) {
                    view.showToastMessageRed(e.getMessage());
                } else if (e instanceof CartResponseDataNullException) {
                    view.showToastMessageRed(e.getMessage());
                } else if (e instanceof CartHttpErrorException) {
                    view.showToastMessageRed(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.showToastMessageRed(e.getMessage());
                } else {
                    view.showToastMessageRed(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
                processInitialGetCartData();
            }

            @Override
            public void onNext(UpdateCartData data) {
                view.hideProgressLoading();
                if (!data.isSuccess()) {
                    view.renderErrorToShipmentForm(data.getMessage());
                } else {
                    int checklistCondition = getChecklistCondition();
                    view.renderToShipmentFormSuccess(generateCheckoutDataAnalytics(cartItemDataList), checklistCondition);
                }
            }
        };
    }

    private int getChecklistCondition() {
        int checklistCondition = ITEM_CHECKED_ALL_WITHOUT_CHANGES;
        List<CartShopHolderData> cartShopHolderDataList = view.getAllCartDataList();
        for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
            if (cartShopHolderData.isPartialSelected() || !cartShopHolderData.isAllSelected()) {
                checklistCondition = ITEM_CHECKED_PARTIAL_SHOP;
                break;
            }
        }

        for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
            for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                if (!cartItemHolderData.isSelected()) {
                    if (checklistCondition == ITEM_CHECKED_PARTIAL_SHOP) {
                        checklistCondition = ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM;
                    } else {
                        checklistCondition = ITEM_CHECKED_PARTIAL_ITEM;
                    }
                    break;
                }
            }
        }

        if (checklistCondition == ITEM_CHECKED_ALL_WITHOUT_CHANGES && hasPerformChecklistChange) {
            checklistCondition = ITEM_CHECKED_ALL_WITH_CHANGES;
        }
        return checklistCondition;
    }

    @NonNull
    private Subscriber<ResetAndRefreshCartListData> getSubscriberResetRefreshCart() {
        return new Subscriber<ResetAndRefreshCartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                e.printStackTrace();
                view.renderLoadGetCartDataFinish();
                handleErrorinitCartList(e);
            }

            @Override
            public void onNext(ResetAndRefreshCartListData resetAndRefreshCartListData) {
                view.hideProgressLoading();
                view.renderLoadGetCartDataFinish();
                if (resetAndRefreshCartListData.getCartListData() == null) {
                    view.renderErrorInitialGetCartListData(resetAndRefreshCartListData.getResetCartData().getMessage());
                } else {
                    view.disableSwipeRefresh();
                    if (resetAndRefreshCartListData.getCartListData().getShopGroupDataList().isEmpty()) {
                        view.renderEmptyCartData(resetAndRefreshCartListData.getCartListData());
                    } else {
                        view.renderInitialGetCartListDataSuccess(resetAndRefreshCartListData.getCartListData());
                    }
                }
            }
        };
    }

    @NonNull
    private Subscriber<PromoCodeCartListData> getSubscriberCheckPromoCodeFromSuggestion(boolean isAutoApply) {
        return new Subscriber<PromoCodeCartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof UnknownHostException) {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof CartResponseErrorException) {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(e.getMessage());
                } else if (e instanceof CartResponseDataNullException) {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(e.getMessage());
                } else if (e instanceof CartHttpErrorException) {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(e.getMessage());
                } else {
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                    );
                }
            }

            @Override
            public void onNext(PromoCodeCartListData promoCodeCartListData) {
                view.hideProgressLoading();
                if (!promoCodeCartListData.isError())
                    view.renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                else if (!isAutoApply)
                    view.renderErrorCheckPromoCodeFromSuggestedPromo(promoCodeCartListData.getErrorMessage());
            }
        };
    }

    @Override
    public void processCancelAutoApply() {
        compositeSubscription.add(cancelAutoApplyCouponUseCase.createObservable(RequestParams.create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
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

    @Override
    public void processAddToWishlist(String productId, String userId, WishListActionListener listener) {
        view.showProgressLoading();
        addWishListUseCase.createObservable(productId, userId, listener);
    }

    @Override
    public void processRemoveFromWishlist(String productId, String userId, WishListActionListener listener) {
        view.showProgressLoading();
        removeWishListUseCase.createObservable(productId, userId, listener);
    }

    @Override
    public ProductPass generateProductPassProductDetailPage(CartItemData.OriginData originData) {
        return ProductPass.Builder.aProductPass()
                .setProductId(originData.getProductId())
                .setProductImage(originData.getProductImage())
                .setProductName(originData.getProductName())
                .setProductPrice(originData.getPriceFormatted())
                .build();
    }

    @Override
    public Map<String, Object> generateCartDataAnalytics(CartItemData removedCartItem, String enhancedECommerceAction) {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        return generateCartDataAnalytics(cartItemDataList, enhancedECommerceAction);
    }

    @Override
    public Map<String, Object> generateCartDataAnalytics(List<CartItemData> cartItemDataList, String enhancedECommerceAction) {

        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();

        for (CartItemData cartItemData : cartItemDataList) {
            EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = getEnhancedECommerceProductCartMapData(cartItemData);
            enhancedECommerceCartMapData.addProduct(enhancedECommerceProductCartMapData.getProduct());
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        enhancedECommerceCartMapData.setAction(enhancedECommerceAction);

        return enhancedECommerceCartMapData.getCartMap();
    }

    @NonNull
    private EnhancedECommerceProductCartMapData getEnhancedECommerceProductCartMapData(CartItemData cartItemData) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setCartId(String.valueOf(cartItemData.getOriginData().getCartId()));
        enhancedECommerceProductCartMapData.setProductName(cartItemData.getOriginData().getProductName());
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(cartItemData.getOriginData().getProductId()));
        enhancedECommerceProductCartMapData.setPrice(String.valueOf(cartItemData.getOriginData().getPricePlanInt()));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(cartItemData.getOriginData().getCategoryForAnalytics())
                ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                : cartItemData.getOriginData().getCategoryForAnalytics());
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setQty(String.valueOf(cartItemData.getUpdatedData().getQuantity()));
        enhancedECommerceProductCartMapData.setShopId(cartItemData.getOriginData().getShopId());
        enhancedECommerceProductCartMapData.setShopType(cartItemData.getOriginData().getShopType());
        enhancedECommerceProductCartMapData.setShopName(cartItemData.getOriginData().getShopName());
        enhancedECommerceProductCartMapData.setCategoryId(cartItemData.getOriginData().getCategoryId());
        enhancedECommerceProductCartMapData.setAttribution(
                TextUtils.isEmpty(cartItemData.getOriginData().getTrackerAttribution())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : cartItemData.getOriginData().getTrackerAttribution()
        );
        enhancedECommerceProductCartMapData.setListName(
                TextUtils.isEmpty(cartItemData.getOriginData().getTrackerListName())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : cartItemData.getOriginData().getTrackerListName()
        );
        return enhancedECommerceProductCartMapData;
    }

    private Map<String, Object> generateCheckoutDataAnalytics(List<CartItemData> cartItemDataList) {
        Map<String, Object> checkoutMapData = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setStep(EnhancedECommerceActionField.STEP_1);
        enhancedECommerceActionField.setOption(EnhancedECommerceActionField.OPTION_CART_PAGE_LOADED);

        EnhancedECommerceCheckout enhancedECommerceCheckout = new EnhancedECommerceCheckout();
        for (CartItemData cartItemData : cartItemDataList) {
            EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData
                    = getEnhancedECommerceProductCartMapData(cartItemData);
            enhancedECommerceCheckout.addProduct(enhancedECommerceProductCartMapData.getProduct());
        }
        enhancedECommerceCheckout.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        enhancedECommerceCheckout.setActionField(enhancedECommerceActionField.getActionFieldMap());

        checkoutMapData.put(EnhancedECommerceCheckout.KEY_CHECKOUT, enhancedECommerceCheckout.getCheckoutMap());

        return checkoutMapData;
    }

    @Override
    public void setHasPerformChecklistChange() {
        hasPerformChecklistChange = true;
    }

    @Override
    public boolean hasPerformChecklistChange() {
        return hasPerformChecklistChange;
    }
}
