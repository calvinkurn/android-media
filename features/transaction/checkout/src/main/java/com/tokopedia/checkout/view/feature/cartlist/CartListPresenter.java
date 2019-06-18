package com.tokopedia.checkout.view.feature.cartlist;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.DeleteAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.ResetAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.WholesalePrice;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.feature.cartlist.subscriber.CheckPromoFirstStepAfterClashSubscriber;
import com.tokopedia.checkout.view.feature.cartlist.subscriber.ClearCacheAutoApplyAfterClashSubscriber;
import com.tokopedia.checkout.view.feature.cartlist.subscriber.ClearCacheAutoApplySubscriber;
import com.tokopedia.checkout.view.feature.cartlist.subscriber.GetRecentViewSubscriber;
import com.tokopedia.checkout.view.feature.cartlist.subscriber.GetRecommendationSubscriber;
import com.tokopedia.checkout.view.feature.cartlist.subscriber.GetWishlistSubscriber;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsGqlUseCase;
import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceActionField;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCheckout;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceProductCartMapData;
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException;
import com.tokopedia.transactiondata.entity.request.RemoveCartRequest;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.transactiondata.entity.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

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
    private static final String PARAM_GLOBAL = "global";
    private static final String PARAM_MERCHANT = "merchant";

    public static final int ITEM_CHECKED_ALL_WITHOUT_CHANGES = 0;
    public static final int ITEM_CHECKED_ALL_WITH_CHANGES = 1;
    public static final int ITEM_CHECKED_PARTIAL_SHOP = 3;
    public static final int ITEM_CHECKED_PARTIAL_ITEM = 4;
    public static final int ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM = 5;
    public static final String CART_SRC = "cart";
    public static final String ITEM_REQUEST = "5";

    private ICartListView view;
    private final GetCartListUseCase getCartListUseCase;
    private final CompositeSubscription compositeSubscription;
    private final DeleteCartUseCase deleteCartUseCase;
    private final DeleteCartGetCartListUseCase deleteCartGetCartListUseCase;
    private final UpdateCartUseCase updateCartUseCase;
    private final ResetCartGetCartListUseCase resetCartGetCartListUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final UpdateAndReloadCartUseCase updateAndReloadCartUseCase;
    private final CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase;
    private final CheckPromoStackingCodeMapper checkPromoStackingCodeMapper;
    private final TopAdsGqlUseCase topAdsUseCase;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;
    private final UserSessionInterface userSessionInterface;
    private final GetRecentViewUseCase getRecentViewUseCase;
    private final GetWishlistUseCase getWishlistUseCase;
    private final GetRecommendationUseCase getRecommendationUseCase;
    private final AddToCartUseCase addToCartUseCase;
    private CartListData cartListData;
    private boolean hasPerformChecklistChange;

    @Inject
    public CartListPresenter(GetCartListUseCase getCartListUseCase,
                             DeleteCartUseCase deleteCartUseCase,
                             DeleteCartGetCartListUseCase deleteCartGetCartListUseCase,
                             UpdateCartUseCase updateCartUseCase,
                             ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                             CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase,
                             CheckPromoStackingCodeMapper checkPromoStackingCodeMapper,
                             CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                             CompositeSubscription compositeSubscription,
                             CartApiRequestParamGenerator cartApiRequestParamGenerator,
                             AddWishListUseCase addWishListUseCase,
                             RemoveWishListUseCase removeWishListUseCase,
                             UpdateAndReloadCartUseCase updateAndReloadCartUseCase,
                             UserSessionInterface userSessionInterface,
                             TopAdsGqlUseCase topAdsUseCase,
                             ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                             GetRecentViewUseCase getRecentViewUseCase,
                             GetWishlistUseCase getWishlistUseCase,
                             GetRecommendationUseCase getRecommendationUseCase,
                             AddToCartUseCase addToCartUseCase) {
        this.getCartListUseCase = getCartListUseCase;
        this.compositeSubscription = compositeSubscription;
        this.deleteCartUseCase = deleteCartUseCase;
        this.deleteCartGetCartListUseCase = deleteCartGetCartListUseCase;
        this.updateCartUseCase = updateCartUseCase;
        this.resetCartGetCartListUseCase = resetCartGetCartListUseCase;
        this.checkPromoStackingCodeUseCase = checkPromoStackingCodeUseCase;
        this.checkPromoStackingCodeMapper = checkPromoStackingCodeMapper;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.updateAndReloadCartUseCase = updateAndReloadCartUseCase;
        this.userSessionInterface = userSessionInterface;
        this.topAdsUseCase = topAdsUseCase;
        this.clearCacheAutoApplyStackUseCase = clearCacheAutoApplyStackUseCase;
        this.getRecentViewUseCase = getRecentViewUseCase;
        this.getWishlistUseCase = getWishlistUseCase;
        this.getRecommendationUseCase = getRecommendationUseCase;
        this.addToCartUseCase = addToCartUseCase;
    }

    @Override
    public void attachView(ICartListView view) {
        this.view = view;
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
        if (clearCacheAutoApplyStackUseCase != null) {
            clearCacheAutoApplyStackUseCase.unsubscribe();
        }
        if (checkPromoStackingCodeUseCase != null) {
            checkPromoStackingCodeUseCase.unsubscribe();
        }
        if (getRecentViewUseCase != null) {
            getRecentViewUseCase.unsubscribe();
        }
        if (getWishlistUseCase != null) {
            getWishlistUseCase.unsubscribe();
        }
        if (getRecommendationUseCase != null) {
            getRecommendationUseCase.unsubscribe();
        }
        if (addToCartUseCase != null) {
            addToCartUseCase.unsubscribe();
        }
        view = null;
    }

    @Override
    public CartListData getCartListData() {
        return cartListData;
    }

    @Override
    public void setCartListData(CartListData cartListData) {
        this.cartListData = cartListData;
    }

    @Override
    public void processInitialGetCartData(String cartId, boolean initialLoad) {
        if (initialLoad) {
            view.renderLoadGetCartData();
        } else {
            view.showProgressLoading();
        }

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                view.getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList(null))
        );
        requestParams.putString(GetCartListUseCase.PARAM_SELECTED_CART_ID, cartId);

        compositeSubscription.add(getCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(getSubscriberInitialCartListData(initialLoad))
        );
    }

    @Override
    public void processDeleteAndRefreshCart(List<CartItemData> allCartItemData, List<CartItemData> removedCartItems,
                                            ArrayList<String> appliedPromoOnDeletedProductList, boolean addWishList) {
        view.showProgressLoading();
        boolean removeAllItem = allCartItemData.size() == removedCartItems.size();

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

        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : allCartItemData) {
            if (!data.isError()) {
                updateCartRequestList.add(new UpdateCartRequest.Builder()
                        .cartId(data.getOriginData().getCartId())
                        .notes(data.getUpdatedData().getRemark())
                        .quantity(data.getUpdatedData().getQuantity())
                        .build());
            }
        }
        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put(UpdateCartUseCase.PARAM_CARTS, new Gson().toJson(updateCartRequestList));

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DeleteCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART,
                view.getGeneratedAuthParamNetwork(paramDelete));
        requestParams.putObject(DeleteCartGetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_CART,
                view.getGeneratedAuthParamNetwork(paramGetList));
        requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                view.getGeneratedAuthParamNetwork(paramUpdate));
        requestParams.putBoolean(DeleteCartGetCartListUseCase.PARAM_IS_DELETE_ALL_DATA, allCartItemData.size() == removedCartItems.size());
        requestParams.putObject(DeleteCartGetCartListUseCase.PARAM_TO_BE_REMOVED_PROMO_CODES, appliedPromoOnDeletedProductList);

        compositeSubscription.add(deleteCartGetCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(getSubscriberDeleteAndRefreshCart(removeAllItem, appliedPromoOnDeletedProductList)));
    }

    @Override
    public void processToUpdateCartData(List<CartItemData> cartItemDataList) {
        view.showProgressLoading();
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
    public void processUpdateCartDataPromoMerchant(List<CartItemData> cartItemDataList, ShopGroupData shopGroupData) {
        view.showProgressLoading();
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
                        .subscribe(getSubscriberUpdateCartPromoMerchant(shopGroupData))
        );
    }

    @Override
    public void processUpdateCartDataPromoStacking(List<CartItemData> cartItemDataList, PromoStackingData promoStackingData, int goToDetail) {
        view.showProgressLoading();
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
                        .subscribe(getSubscriberUpdateCartPromoGlobal(promoStackingData, goToDetail))
        );
    }

    private Subscriber<UpdateCartData> getSubscriberUpdateCartPromoMerchant(ShopGroupData shopGroupData) {
        return new Subscriber<UpdateCartData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (view != null) {
                    view.hideProgressLoading();
                    String errorMessage = e.getMessage();
                    if (!(e instanceof CartResponseErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                    }
                    view.showToastMessageRed(errorMessage);
                    processInitialGetCartData(view.getCartId(), cartListData == null);
                }
            }

            @Override
            public void onNext(UpdateCartData data) {
                if (view != null) {
                    view.hideProgressLoading();
                    if (!data.isSuccess()) {
                        view.showToastMessageRed(data.getMessage());
                    } else {
                        view.showMerchantVoucherListBottomsheet(shopGroupData);
                    }
                }
            }
        };
    }

    private Subscriber<UpdateCartData> getSubscriberUpdateCartPromoGlobal(PromoStackingData promoStackingData, int stateGoTo) {
        return new Subscriber<UpdateCartData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (view != null) {
                    view.hideProgressLoading();
                    String errorMessage = e.getMessage();
                    if (!(e instanceof CartResponseErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                    }
                    view.showToastMessageRed(errorMessage);
                    processInitialGetCartData(view.getCartId(), cartListData == null);
                }
            }

            @Override
            public void onNext(UpdateCartData data) {
                if (view != null) {
                    view.hideProgressLoading();
                    if (!data.isSuccess()) {
                        view.showToastMessageRed(data.getMessage());
                    } else {
                        if (stateGoTo == CartFragment.GO_TO_LIST) {
                            view.goToCouponList();
                        } else {
                            view.goToDetailPromoStacking(promoStackingData);
                        }
                    }
                }
            }
        };
    }

    @Override
    public void processToUpdateAndReloadCartData() {
        List<CartItemData> cartItemDataList = view.getAllCartDataList();
        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();
        for (CartItemData data : cartItemDataList) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(data.getOriginData().getCartId())
                    .notes(data.getUpdatedData().getRemark())
                    .quantity(data.getUpdatedData().getQuantity())
                    .build());
        }
        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put(UpdateAndReloadCartUseCase.PARAM_CARTS, new Gson().toJson(updateCartRequestList));

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(UpdateAndReloadCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                view.getGeneratedAuthParamNetwork(paramUpdate));
        requestParams.putObject(
                UpdateAndReloadCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_CART,
                view.getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList(null))
        );

        compositeSubscription.add(
                updateAndReloadCartUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<UpdateAndRefreshCartListData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null) {
                                    view.hideProgressLoading();
                                    String errorMessage = e.getMessage();
                                    if (!(e instanceof CartResponseErrorException)) {
                                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                                    }
                                    view.showToastMessageRed(errorMessage);
                                }
                            }

                            @Override
                            public void onNext(UpdateAndRefreshCartListData updateAndRefreshCartListData) {
                                if (view != null) {
                                    view.hideProgressLoading();
                                    if (updateAndRefreshCartListData.getCartListData() != null) {
                                        CartListPresenter.this.cartListData = updateAndRefreshCartListData.getCartListData();
                                        view.renderLoadGetCartDataFinish();
                                        view.renderInitialGetCartListDataSuccess(cartListData);
                                    }
                                }
                            }
                        })
        );

    }

    @Override
    public void reCalculateSubTotal(List<CartShopHolderData> dataList) {
        double totalCashback = 0;
        double totalPrice = 0;
        int totalItemQty = 0;
        int errorProductCount = 0;

        // Collect all Cart Item, if has no error and selected
        List<CartItemHolderData> allCartItemDataList = new ArrayList<>();
        for (CartShopHolderData cartShopHolderData : dataList) {
            if (cartShopHolderData.getShopGroupData().getCartItemDataList() != null) {
                if (!cartShopHolderData.getShopGroupData().isError()) {
                    if (cartShopHolderData.isAllSelected() || cartShopHolderData.isPartialSelected()) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                            if (!cartItemHolderData.getCartItemData().isError()) {
                                if (cartItemHolderData.isSelected()) {
                                    allCartItemDataList.add(cartItemHolderData);
                                }
                            } else {
                                errorProductCount++;
                            }
                        }
                    }
                } else {
                    errorProductCount += cartShopHolderData.getShopGroupData().getCartItemDataList().size();
                }
            }
        }

        // Set cart item parent id if current value is 0
        for (int i = 0; i < allCartItemDataList.size(); i++) {
            CartItemData cartItemData = allCartItemDataList.get(i).getCartItemData();
            if (cartItemData != null && cartItemData.getOriginData() != null) {
                if (cartItemData.getOriginData().getParentId().equals("0")) {
                    cartItemData.getOriginData().setParentId(String.valueOf(i + 1));
                }
            }
        }

        // Calculate total price, total item, and wholesale price (if any)
        Map<String, Double> cashbackWholesalePriceMap = new HashMap<>();
        Map<String, Double> subtotalWholesalePriceMap = new HashMap<>();
        Map<String, CartItemData> cartItemParentIdMap = new HashMap<>();
        for (CartItemHolderData data : allCartItemDataList) {
            if (data.getCartItemData().getOriginData() != null) {
                String parentId = data.getCartItemData().getOriginData().getParentId();
                String productId = data.getCartItemData().getOriginData().getProductId();
                int itemQty = data.getCartItemData().getUpdatedData().getQuantity();
                totalItemQty += itemQty;
                if (!TextUtils.isEmpty(parentId) && !parentId.equals("0")) {
                    for (CartItemHolderData dataForQty : allCartItemDataList) {
                        if (!productId.equals(dataForQty.getCartItemData().getOriginData().getProductId()) &&
                                parentId.equals(dataForQty.getCartItemData().getOriginData().getParentId()) &&
                                dataForQty.getCartItemData().getOriginData().getPricePlan() ==
                                        data.getCartItemData().getOriginData().getPricePlan()) {
                            itemQty += dataForQty.getCartItemData().getUpdatedData().getQuantity();
                        }
                    }
                }

                List<WholesalePrice> wholesalePrices = data.getCartItemData().getOriginData().getWholesalePrice();
                boolean hasCalculateWholesalePrice = false;
                if (wholesalePrices != null && wholesalePrices.size() > 0) {
                    double subTotalWholesalePrice = 0;
                    double itemCashback = 0;
                    for (WholesalePrice wholesalePrice : wholesalePrices) {
                        if (itemQty >= wholesalePrice.getQtyMin()) {
                            subTotalWholesalePrice = itemQty * wholesalePrice.getPrdPrc();
                            hasCalculateWholesalePrice = true;
                            String wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    wholesalePrice.getPrdPrc(), false);
                            data.getCartItemData().getOriginData().setWholesalePriceFormatted(wholesalePriceFormatted);
                            break;
                        }
                    }
                    if (!hasCalculateWholesalePrice) {
                        if (itemQty > wholesalePrices.get(wholesalePrices.size() - 1).getPrdPrc()) {
                            subTotalWholesalePrice = itemQty * wholesalePrices.get(wholesalePrices.size() - 1).getPrdPrc();
                            String wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    wholesalePrices.get(wholesalePrices.size() - 1).getPrdPrc(), false);
                            data.getCartItemData().getOriginData().setWholesalePriceFormatted(wholesalePriceFormatted);
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
                            totalCashback = totalCashback + itemCashback;
                        }
                        totalPrice = totalPrice + itemPrice;
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
                                totalCashback = totalCashback + itemCashback;
                            }
                            totalPrice = totalPrice + itemPrice;
                            data.getCartItemData().getOriginData().setWholesalePriceFormatted(null);
                        }
                    }
                }
            }
        }

        if (!subtotalWholesalePriceMap.isEmpty()) {
            for (Map.Entry<String, Double> item : subtotalWholesalePriceMap.entrySet()) {
                totalPrice += item.getValue();
            }
        }

        if (!cashbackWholesalePriceMap.isEmpty()) {
            for (Map.Entry<String, Double> item : cashbackWholesalePriceMap.entrySet()) {
                totalCashback += item.getValue();
            }
        }

        String totalPriceString = "-";
        if (totalPrice > 0) {
            totalPriceString = CurrencyFormatUtil.convertPriceValueToIdrFormat(((long) totalPrice), false);
        }
        view.updateCashback(totalCashback);
        boolean selectAllItem = view.getAllCartDataList().size() == allCartItemDataList.size() + errorProductCount &&
                allCartItemDataList.size() > 0;
        view.renderDetailInfoSubTotal(String.valueOf(totalItemQty), totalPriceString, selectAllItem);

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
    private Subscriber<CartListData> getSubscriberInitialCartListData(boolean initialLoad) {
        return new Subscriber<CartListData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (view != null) {
                    if (!initialLoad) {
                        view.hideProgressLoading();
                    }
                    view.renderLoadGetCartDataFinish();
                    String errorMessage = e.getMessage();
                    if (!(e instanceof CartResponseErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                    }
                    view.renderErrorInitialGetCartListData(errorMessage);
                    view.stopTrace();
                }
            }

            @Override
            public void onNext(CartListData cartListData) {
                if (view != null) {
                    if (!initialLoad) {
                        view.hideProgressLoading();
                    }
                    CartListPresenter.this.cartListData = cartListData;
                    view.renderLoadGetCartDataFinish();
                    view.renderInitialGetCartListDataSuccess(cartListData);
                    view.stopTrace();
                }
            }
        };
    }

    @NonNull
    private Subscriber<DeleteAndRefreshCartListData> getSubscriberDeleteAndRefreshCart(boolean removeAllItem, ArrayList<String> appliedPromoOnDeletedProductList) {
        return new Subscriber<DeleteAndRefreshCartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (view != null) {
                    view.hideProgressLoading();
                    e.printStackTrace();
                    if (!removeAllItem) {
                        String errorMessage = e.getMessage();
                        if (!(e instanceof CartResponseErrorException)) {
                            errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                        }
                        view.showToastMessageRed(errorMessage);
                    } else {
                        processInitialGetCartData(view.getCartId(), cartListData == null);
                    }
                }
            }

            @Override
            public void onNext(DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                if (view != null) {
                    view.hideProgressLoading();
                    view.renderLoadGetCartDataFinish();
                    if (!removeAllItem) {
                        if (deleteAndRefreshCartListData.getDeleteCartData().isSuccess()
                                && deleteAndRefreshCartListData.getCartListData() != null) {
                            if (deleteAndRefreshCartListData.getCartListData().getShopGroupDataList().isEmpty()) {
                                processInitialGetCartData(view.getCartId(), cartListData == null);
                            } else {
                                CartListPresenter.this.cartListData = deleteAndRefreshCartListData.getCartListData();
                                view.renderInitialGetCartListDataSuccess(deleteAndRefreshCartListData.getCartListData());
                                view.onDeleteCartDataSuccess();
                            }
                        } else {
                            view.showToastMessageRed(
                                    deleteAndRefreshCartListData.getDeleteCartData().getMessage()
                            );
                        }
                    } else {
                        processInitialGetCartData(view.getCartId(), cartListData == null);
                    }
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
                if (view != null) {
                    e.printStackTrace();
                    view.hideProgressLoading();
                    String errorMessage = e.getMessage();
                    if (!(e instanceof CartResponseErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                    }
                    view.showToastMessageRed(errorMessage);
                    processInitialGetCartData(view.getCartId(), cartListData == null);
                }
            }

            @Override
            public void onNext(UpdateCartData data) {
                if (view != null) {
                    view.hideProgressLoading();
                    if (!data.isSuccess()) {
                        view.renderErrorToShipmentForm(data.getMessage());
                    } else {
                        int checklistCondition = getChecklistCondition();
                        view.renderToShipmentFormSuccess(
                                generateCheckoutDataAnalytics(cartItemDataList),
                                isCheckoutProductEligibleForCashOnDelivery(cartItemDataList),
                                checklistCondition);
                    }
                }
            }
        };
    }

    private boolean isCheckoutProductEligibleForCashOnDelivery(List<CartItemData> cartItemDataList) {
        double totalAmount = 0;
        double maximalTotalAmountEligible = 1000000;
        for (CartItemData cartItemData : cartItemDataList) {
            double itemPriceAmount = cartItemData.getOriginData().getPricePlan()
                    * cartItemData.getUpdatedData().getQuantity();
            totalAmount = totalAmount + itemPriceAmount;
            if (!cartItemData.getOriginData().isCod())
                return false;
        }
        return totalAmount <= maximalTotalAmountEligible;
    }

    private int getChecklistCondition() {
        int checklistCondition = ITEM_CHECKED_ALL_WITHOUT_CHANGES;
        List<CartShopHolderData> cartShopHolderDataList = view.getAllShopDataList();

        if (cartShopHolderDataList.size() == 1) {
            for (CartItemHolderData cartItemHolderData : cartShopHolderDataList.get(0).getShopGroupData().getCartItemDataList()) {
                if (!cartItemHolderData.isSelected()) {
                    checklistCondition = ITEM_CHECKED_PARTIAL_ITEM;
                    break;
                }
            }
        } else if (cartShopHolderDataList.size() > 1) {
            int allSelectedItemShopCount = 0;
            boolean selectPartialShopAndItem = false;
            for (CartShopHolderData cartShopHolderData : cartShopHolderDataList) {
                if (cartShopHolderData.isAllSelected()) {
                    allSelectedItemShopCount++;
                } else {
                    int selectedItem = 0;
                    for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                        if (!cartItemHolderData.isSelected()) {
                            selectedItem++;
                        }
                    }
                    if (!selectPartialShopAndItem && selectedItem != cartShopHolderData.getShopGroupData().getCartItemDataList().size()) {
                        selectPartialShopAndItem = true;
                    }
                }
            }
            if (selectPartialShopAndItem) {
                checklistCondition = ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM;
            } else if (allSelectedItemShopCount < cartShopHolderDataList.size()) {
                checklistCondition = ITEM_CHECKED_PARTIAL_SHOP;
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
                if (view != null) {
                    view.hideProgressLoading();
                    e.printStackTrace();
                    view.renderLoadGetCartDataFinish();
                    String errorMessage = e.getMessage();
                    if (!(e instanceof CartResponseErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                    }
                    view.renderErrorInitialGetCartListData(errorMessage);
                    view.stopTrace();
                }
            }

            @Override
            public void onNext(ResetAndRefreshCartListData resetAndRefreshCartListData) {
                if (view != null) {
                    view.hideProgressLoading();
                    view.renderLoadGetCartDataFinish();
                    if (resetAndRefreshCartListData.getCartListData() == null) {
                        view.renderErrorInitialGetCartListData(resetAndRefreshCartListData.getResetCartData().getMessage());
                        view.stopTrace();
                    } else {
                        view.renderInitialGetCartListDataSuccess(resetAndRefreshCartListData.getCartListData());
                        view.stopTrace();
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
                if (view != null) {
                    view.hideProgressLoading();
                    String errorMessage = e.getMessage();
                    if (!(e instanceof CartResponseErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                    }
                    view.showToastMessageRed(errorMessage);
                }
            }

            @Override
            public void onNext(PromoCodeCartListData promoCodeCartListData) {
                if (view != null) {
                    view.hideProgressLoading();
                    view.renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                    if (!promoCodeCartListData.isError())
                        view.renderCheckPromoCodeFromSuggestedPromoSuccess(promoCodeCartListData);
                    else if (!isAutoApply)
                        view.showToastMessageRed(promoCodeCartListData.getErrorMessage());
                }
            }
        };
    }

    @Override
    public void processCancelAutoApplyPromoStack(int shopIndex, ArrayList<String> promoCodeList, boolean ignoreAPIResponse) {
        if (promoCodeList.size() > 0) {
            if (!ignoreAPIResponse) {
                view.showProgressLoading();
            }
            clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), promoCodeList);
            clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), new ClearCacheAutoApplySubscriber(view, this, shopIndex, ignoreAPIResponse));
        }
    }

    @Override
    public void processCancelAutoApplyPromoStackAfterClash(ArrayList<String> oldPromoList, ArrayList<ClashingVoucherOrderUiModel> newPromoList, String type) {
        view.showProgressLoading();
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), oldPromoList);
        clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), new ClearCacheAutoApplyAfterClashSubscriber(view, this, newPromoList, type));
    }

    @Override
    public void processApplyPromoStackAfterClash(ArrayList<ClashingVoucherOrderUiModel> newPromoList, String type) {
        Promo promo = view.generateCheckPromoFirstStepParam();
        promo.setCodes(new ArrayList<>());
        if (promo.getOrders() != null) {
            for (Order order : promo.getOrders()) {
                order.setCodes(new ArrayList<>());
            }
        }

        // New promo list is array, but it will always be 1 item
        if (newPromoList != null && newPromoList.size() > 0) {
            ClashingVoucherOrderUiModel model = newPromoList.get(0);
            if (TextUtils.isEmpty(model.getUniqueId())) {
                // This promo is global promo
                ArrayList<String> codes = new ArrayList<>();
                codes.add(model.getCode());
                promo.setCodes(codes);

                CurrentApplyCode currentApplyCode = new CurrentApplyCode();
                if (!model.getCode().isEmpty()) {
                    currentApplyCode.setCode(model.getCode());
                    currentApplyCode.setType(PARAM_GLOBAL);
                }
                promo.setCurrentApplyCode(currentApplyCode);
            } else {
                // This promo is merchant/logistic promo
                if (promo.getOrders() != null) {
                    for (Order order : promo.getOrders()) {
                        if (model.getUniqueId().equals(order.getUniqueId())) {
                            ArrayList<String> codes = new ArrayList<>();
                            codes.add(model.getCode());
                            order.setCodes(codes);

                            CurrentApplyCode currentApplyCode = new CurrentApplyCode();
                            if (!model.getCode().isEmpty()) {
                                currentApplyCode.setCode(model.getCode());
                                currentApplyCode.setType(type);
                            }
                            promo.setCurrentApplyCode(currentApplyCode);
                            break;
                        }
                    }
                }
            }
            view.showProgressLoading();
            checkPromoStackingCodeUseCase.setParams(promo);
            checkPromoStackingCodeUseCase.execute(RequestParams.create(),
                    new CheckPromoFirstStepAfterClashSubscriber(view, this, checkPromoStackingCodeMapper, type));
        }
    }

    @Override
    public void processAddToWishlist(String productId, String userId, WishListActionListener listener) {
        addWishListUseCase.createObservable(productId, userId, listener);
    }

    @Override
    public void processRemoveFromWishlist(String productId, String userId, WishListActionListener listener) {
        removeWishListUseCase.createObservable(productId, userId, listener);
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
        enhancedECommerceProductCartMapData.setDimension45(String.valueOf(cartItemData.getOriginData().getCartId()));
        enhancedECommerceProductCartMapData.setProductName(cartItemData.getOriginData().getProductName());
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(cartItemData.getOriginData().getProductId()));
        enhancedECommerceProductCartMapData.setPrice(String.valueOf(cartItemData.getOriginData().getPricePlanInt()));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(cartItemData.getOriginData().getCategoryForAnalytics())
                ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                : cartItemData.getOriginData().getCategoryForAnalytics());
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setQty(cartItemData.getUpdatedData().getQuantity());
        enhancedECommerceProductCartMapData.setShopId(cartItemData.getOriginData().getShopId());
        enhancedECommerceProductCartMapData.setShopType(cartItemData.getOriginData().getShopType());
        enhancedECommerceProductCartMapData.setShopName(cartItemData.getOriginData().getShopName());
        enhancedECommerceProductCartMapData.setCategoryId(cartItemData.getOriginData().getCategoryId());
        enhancedECommerceProductCartMapData.setAttribution(
                TextUtils.isEmpty(cartItemData.getOriginData().getTrackerAttribution())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : cartItemData.getOriginData().getTrackerAttribution()
        );
        enhancedECommerceProductCartMapData.setDimension38(
                TextUtils.isEmpty(cartItemData.getOriginData().getTrackerAttribution())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : cartItemData.getOriginData().getTrackerAttribution()
        );
        enhancedECommerceProductCartMapData.setListName(
                TextUtils.isEmpty(cartItemData.getOriginData().getTrackerListName())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : cartItemData.getOriginData().getTrackerListName()
        );
        enhancedECommerceProductCartMapData.setDimension40(
                TextUtils.isEmpty(cartItemData.getOriginData().getTrackerListName())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : cartItemData.getOriginData().getTrackerListName()
        );
        return enhancedECommerceProductCartMapData;
    }

    private EnhancedECommerceProductCartMapData getCheckoutEnhancedECommerceProductCartMapData(CartItemData cartItemData) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setDimension80(
                TextUtils.isEmpty(cartItemData.getOriginData().getTrackerAttribution())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : cartItemData.getOriginData().getTrackerAttribution()
        );
        enhancedECommerceProductCartMapData.setDimension45(String.valueOf(cartItemData.getOriginData().getCartId()));
        enhancedECommerceProductCartMapData.setDimension54(cartItemData.isFulfillment());
        enhancedECommerceProductCartMapData.setProductName(cartItemData.getOriginData().getProductName());
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(cartItemData.getOriginData().getProductId()));
        enhancedECommerceProductCartMapData.setPrice(String.valueOf(cartItemData.getOriginData().getPricePlanInt()));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(cartItemData.getOriginData().getCategory())
                ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                : cartItemData.getOriginData().getCategory());
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setQty(cartItemData.getUpdatedData().getQuantity());
        enhancedECommerceProductCartMapData.setShopId(cartItemData.getOriginData().getShopId());
        enhancedECommerceProductCartMapData.setShopType(cartItemData.getOriginData().getShopType());
        enhancedECommerceProductCartMapData.setShopName(cartItemData.getOriginData().getShopName());
        enhancedECommerceProductCartMapData.setCategoryId(cartItemData.getOriginData().getCategoryId());
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
                    = getCheckoutEnhancedECommerceProductCartMapData(cartItemData);
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
    public boolean dataHasChanged() {
        boolean hasChanges = false;
        for (CartItemData cartItemData : view.getAllCartDataList()) {
            if (cartItemData.getUpdatedData().getQuantity() != cartItemData.getOriginData().getOriginalQty() ||
                    !cartItemData.getUpdatedData().getRemark().equals(cartItemData.getOriginData().getOriginalRemark())) {
                hasChanges = true;
                break;
            }
        }
        if (hasChanges) {
            for (CartItemData cartItemData : view.getAllCartDataList()) {
                cartItemData.getOriginData().setOriginalQty(cartItemData.getUpdatedData().getQuantity());
                cartItemData.getOriginData().setOriginalRemark(cartItemData.getUpdatedData().getRemark());
            }
        }
        return hasChanges;
    }

    @Override
    public void processGetRecentViewData() {
        try {
            int userId = Integer.parseInt(userSessionInterface.getUserId());
            getRecentViewUseCase.createObservable(userId, new GetRecentViewSubscriber(view, this));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processGetWishlistData() {
        getWishlistUseCase.createObservable(new GetWishlistSubscriber(view, this));
    }

    @Override
    public void processGetRecommendationData(int page) {
        RequestParams requestParam = getRecommendationUseCase.getRecomParams(
                page, "recom_widget", "cart");
        getRecommendationUseCase.execute(requestParam, new GetRecommendationSubscriber(view, this));
    }

    @Override
    public void processAddToCart(String productId, String shopId, int minOrder) {
        try {
            view.showProgressLoading();
            AddToCartRequest addToCartRequest = new AddToCartRequest.Builder()
                    .productId(Integer.parseInt(productId))
                    .notes("")
                    .quantity(minOrder)
                    .shopId(Integer.parseInt(shopId))
                    .build();

            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(AddToCartUseCase.PARAM_ADD_TO_CART, addToCartRequest);

            addToCartUseCase.createObservable(requestParams)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddToCartDataResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (view != null) {
                                view.hideProgressLoading();
                                String errorMessage = e.getMessage();
                                if (!(e instanceof CartResponseErrorException)) {
                                    errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                                }
                                view.showToastMessageRed(errorMessage);
                            }
                        }

                        @Override
                        public void onNext(AddToCartDataResponse addToCartDataResponse) {
                            if (view != null) {
                                view.hideProgressLoading();
                                if (addToCartDataResponse.getSuccess() == 1) {
                                    processInitialGetCartData("0", false);
                                    view.showToastMessageGreen(addToCartDataResponse.getMessage().get(0));
                                } else {
                                    view.showToastMessageRed(addToCartDataResponse.getMessage().get(0));
                                }
                            }
                        }
                    });
        } catch (NumberFormatException e) {
            e.printStackTrace();
            view.hideProgressLoading();
        }
    }
}
