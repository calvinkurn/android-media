package com.tokopedia.purchase_platform.features.cart.view;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode;
import com.tokopedia.promocheckout.common.data.entity.request.Order;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceRecomProductCartMapData;
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.RemoveInsuranceData;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceData;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceDataCart;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProduct;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductItems;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShopItems;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase;
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase;
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase;
import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator;
import com.tokopedia.purchase_platform.features.cart.data.model.request.RemoveCartRequest;
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest;
import com.tokopedia.purchase_platform.features.cart.domain.model.DeleteAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.ResetAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.WholesalePriceData;
import com.tokopedia.purchase_platform.features.cart.domain.model.voucher.PromoCodeCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.DeleteCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.GetCartListSimplifiedUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.AddToCartSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.CheckPromoFirstStepAfterClashSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.ClearCacheAutoApplyAfterClashSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.ClearCacheAutoApplySubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetInsuranceCartSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetRecentViewSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetRecommendationSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetRemoveMacroInsuranceProductSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetSubscriberUpdateInsuranceProductData;
import com.tokopedia.purchase_platform.features.cart.view.subscriber.GetWishlistSubscriber;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
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
    private final GetCartListSimplifiedUseCase getCartListSimplifiedUseCase;
    private final CompositeSubscription compositeSubscription;
    private final DeleteCartListUseCase deleteCartListUseCase;
    private final UpdateCartUseCase updateCartUseCase;
    private final CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final UpdateAndReloadCartUseCase updateAndReloadCartUseCase;
    private final CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase;
    private final CheckPromoStackingCodeMapper checkPromoStackingCodeMapper;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;
    private final GetInsuranceCartUseCase getInsuranceCartUseCase;
    private final RemoveInsuranceProductUsecase removeInsuranceProductUsecase;
    private final UpdateInsuranceProductDataUsecase updateInsuranceProductDataUsecase;
    private final UserSessionInterface userSessionInterface;
    private final GetRecentViewUseCase getRecentViewUseCase;
    private final GetWishlistUseCase getWishlistUseCase;
    private final GetRecommendationUseCase getRecommendationUseCase;
    private final AddToCartUseCase addToCartUseCase;
    private CartListData cartListData;
    private boolean hasPerformChecklistChange;
    private boolean insuranceChecked = true;

    @Inject
    public CartListPresenter(GetCartListSimplifiedUseCase getCartListSimplifiedUseCase,
                             DeleteCartListUseCase deleteCartListUseCase,
                             UpdateCartUseCase updateCartUseCase,
                             CheckPromoStackingCodeUseCase checkPromoStackingCodeUseCase,
                             CheckPromoStackingCodeMapper checkPromoStackingCodeMapper,
                             CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                             CompositeSubscription compositeSubscription,
                             CartApiRequestParamGenerator cartApiRequestParamGenerator,
                             AddWishListUseCase addWishListUseCase,
                             RemoveWishListUseCase removeWishListUseCase,
                             UpdateAndReloadCartUseCase updateAndReloadCartUseCase,
                             UserSessionInterface userSessionInterface,
                             ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                             GetRecentViewUseCase getRecentViewUseCase,
                             GetWishlistUseCase getWishlistUseCase,
                             GetRecommendationUseCase getRecommendationUseCase,
                             AddToCartUseCase addToCartUseCase,
                             GetInsuranceCartUseCase getInsuranceCartUseCase,
                             RemoveInsuranceProductUsecase removeInsuranceProductUsecase,
                             UpdateInsuranceProductDataUsecase updateInsuranceProductDataUsecase) {
        this.getCartListSimplifiedUseCase = getCartListSimplifiedUseCase;
        this.compositeSubscription = compositeSubscription;
        this.deleteCartListUseCase = deleteCartListUseCase;
        this.updateCartUseCase = updateCartUseCase;
        this.checkPromoStackingCodeUseCase = checkPromoStackingCodeUseCase;
        this.checkPromoStackingCodeMapper = checkPromoStackingCodeMapper;
        this.checkPromoCodeCartListUseCase = checkPromoCodeCartListUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.updateAndReloadCartUseCase = updateAndReloadCartUseCase;
        this.userSessionInterface = userSessionInterface;
        this.clearCacheAutoApplyStackUseCase = clearCacheAutoApplyStackUseCase;
        this.getRecentViewUseCase = getRecentViewUseCase;
        this.getWishlistUseCase = getWishlistUseCase;
        this.getRecommendationUseCase = getRecommendationUseCase;
        this.addToCartUseCase = addToCartUseCase;
        this.getInsuranceCartUseCase = getInsuranceCartUseCase;
        this.removeInsuranceProductUsecase = removeInsuranceProductUsecase;
        this.updateInsuranceProductDataUsecase = updateInsuranceProductDataUsecase;

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
        if (getInsuranceCartUseCase != null) {
            getInsuranceCartUseCase.unsubscribe();
        }
        if (removeInsuranceProductUsecase != null) {
            removeInsuranceProductUsecase.unsubscribe();
        }
        if (updateInsuranceProductDataUsecase != null) {
            updateInsuranceProductDataUsecase.unsubscribe();
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
    public void processInitialGetCartData(String cartId, boolean initialLoad, boolean isLoadingTypeRefresh) {
        if (initialLoad) {
            view.renderLoadGetCartData();
        } else if (!isLoadingTypeRefresh) {
            view.showProgressLoading();
        }

        compositeSubscription.add(getCartListSimplifiedUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(getSubscriberInitialCartListData(initialLoad))
        );
    }

    @Override
    public void getInsuranceTechCart() {
        getInsuranceCartUseCase.execute(new GetInsuranceCartSubscriber(view));
    }

    @Override
    public void setAllInsuranceProductsChecked(ArrayList<InsuranceCartShops> insuranceCartShopsArrayList, boolean isChecked) {
        for (InsuranceCartShops insuranceCartShops : insuranceCartShopsArrayList) {
            if (insuranceCartShops != null &&
                    insuranceCartShops.getShopItemsList() != null &&
                    insuranceCartShops.getShopItemsList().size() > 0 &&
                    insuranceCartShops.getShopItemsList().get(0) != null &&
                    insuranceCartShops.getShopItemsList().get(0).getDigitalProductList() != null &&
                    insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().size() > 0 &&
                    insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0) != null) {

                insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0).setOptIn(isChecked);
            }
        }
    }

    @Override
    public void processDeleteCartInsurance(ArrayList<InsuranceCartDigitalProduct> insuranceCartShopsArrayList, boolean showToaster) {

        if (insuranceCartShopsArrayList != null &&
                !insuranceCartShopsArrayList.isEmpty()) {
            view.showProgressLoading();
            ArrayList<String> cartIdList = new ArrayList<>();
            ArrayList<RemoveInsuranceData> removeInsuranceDataArrayList = new ArrayList<>();
            List<Long> productIdArrayList = new ArrayList<>();
            for (InsuranceCartDigitalProduct insuranceCartDigitalProduct : insuranceCartShopsArrayList) {
                long shopid;
                long productId;
                try {
                    shopid = Long.parseLong(insuranceCartDigitalProduct.getShopId());
                } catch (Exception e) {
                    shopid = 0;
                }

                try {
                    productId = Long.parseLong(insuranceCartDigitalProduct.getProductId());
                } catch (Exception e) {
                    productId = 0;
                }
                Long cartId = insuranceCartDigitalProduct.getCartItemId();
                cartIdList.add(String.valueOf(cartId));
                RemoveInsuranceData removeInsuranceData = new RemoveInsuranceData(cartId, shopid, productId);
                removeInsuranceDataArrayList.add(removeInsuranceData);
                productIdArrayList.add(productId);
            }


            removeInsuranceProductUsecase.setRequestParams(removeInsuranceDataArrayList, "cart", String.valueOf(Build.VERSION.SDK_INT), cartIdList);
            removeInsuranceProductUsecase.execute(new GetRemoveMacroInsuranceProductSubscriber(view, productIdArrayList, showToaster));
        }
    }

    @Override
    public void updateInsuranceProductData(InsuranceCartShops insuranceCartShops,
                                           ArrayList<UpdateInsuranceProductApplicationDetails> updateInsuranceProductApplicationDetailsArrayList) {
        view.showProgressLoading();

        ArrayList<UpdateInsuranceDataCart> cartIdList = new ArrayList<>();
        ArrayList<UpdateInsuranceData> updateInsuranceDataArrayList = new ArrayList<>();
        ArrayList<UpdateInsuranceProductItems> updateInsuranceProductItemsArrayList = new ArrayList<>();

        Long shopid = insuranceCartShops.getShopId();

        long productId = 0;

        if (insuranceCartShops.getShopItemsList() != null && !insuranceCartShops.getShopItemsList().isEmpty()) {
            for (InsuranceCartShopItems insuranceCartShopItems : insuranceCartShops.getShopItemsList()) {


                ArrayList<UpdateInsuranceProduct> updateInsuranceProductArrayList = new ArrayList<>();
                for (InsuranceCartDigitalProduct insuranceCartDigitalProduct : insuranceCartShopItems.getDigitalProductList()) {

                    if (!insuranceCartDigitalProduct.isProductLevel()) {

                        Long cartId = insuranceCartShopItems.getDigitalProductList().get(0).getCartItemId();
                        productId = insuranceCartShopItems.getProductId();
                        cartIdList.add(new UpdateInsuranceDataCart(String.valueOf(cartId), 1));

                        UpdateInsuranceProduct updateInsuranceProduct =
                                new UpdateInsuranceProduct(insuranceCartDigitalProduct.getDigitalProductId(),
                                        insuranceCartDigitalProduct.getCartItemId(),
                                        insuranceCartDigitalProduct.getTypeId(),
                                        updateInsuranceProductApplicationDetailsArrayList);
                        updateInsuranceProductArrayList.add(updateInsuranceProduct);

                        UpdateInsuranceProductItems updateInsuranceProductItems = new UpdateInsuranceProductItems(insuranceCartShopItems.getProductId(), 1, updateInsuranceProductArrayList);
                        updateInsuranceProductItemsArrayList.add(updateInsuranceProductItems);
                        UpdateInsuranceData updateInsuranceData = new UpdateInsuranceData(shopid, updateInsuranceProductItemsArrayList);
                        updateInsuranceDataArrayList.add(updateInsuranceData);

                    }
                }
            }
        }


        updateInsuranceProductDataUsecase.setRequestParams(updateInsuranceDataArrayList, "cart", String.valueOf(Build.VERSION.SDK_INT), cartIdList);
        updateInsuranceProductDataUsecase.execute(new GetSubscriberUpdateInsuranceProductData(view, this, productId));
    }

    @Override
    public void processDeleteCartItem(List<CartItemData> allCartItemData,
                                      List<CartItemData> removedCartItems,
                                      ArrayList<String> appliedPromoOnDeletedProductList, boolean addWishList, boolean removeInsurance) {
        view.showProgressLoading();
        boolean removeAllItem = allCartItemData.size() == removedCartItems.size();

        List<Integer> toBeDeletedCartIds = new ArrayList<>();
        for (CartItemData cartItemData : removedCartItems) {
            toBeDeletedCartIds.add(cartItemData.getOriginData().getCartId());
        }
        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .cartIds(toBeDeletedCartIds)
                .addWishlist(addWishList ? 1 : 0)
                .build();
        TKPDMapParam<String, String> paramDelete = new TKPDMapParam<>();
        paramDelete.put(PARAM_PARAMS, new Gson().toJson(removeCartRequest));

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DeleteCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART,
                view.getGeneratedAuthParamNetwork(paramDelete));
        requestParams.putBoolean(DeleteCartListUseCase.PARAM_IS_DELETE_ALL_DATA, removeAllItem);
        requestParams.putObject(DeleteCartListUseCase.PARAM_TO_BE_REMOVED_PROMO_CODES, appliedPromoOnDeletedProductList);

        compositeSubscription.add(deleteCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(getSubscriberDeleteAndRefreshCart(toBeDeletedCartIds, removedCartItems, removeAllItem, removeInsurance)));
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
    public void processUpdateCartDataPromoMerchant
            (List<CartItemData> cartItemDataList, ShopGroupAvailableData shopGroupAvailableData) {
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
                        .subscribe(getSubscriberUpdateCartPromoMerchant(shopGroupAvailableData))
        );
    }

    @Override
    public void processUpdateCartDataPromoStacking(List<CartItemData> cartItemDataList,
                                                   PromoStackingData promoStackingData,
                                                   int goToDetail) {
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

    private Subscriber<UpdateCartData> getSubscriberUpdateCartPromoMerchant(ShopGroupAvailableData shopGroupAvailableData) {
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
                    processInitialGetCartData(view.getCartId(), cartListData == null, false);
                }
            }

            @Override
            public void onNext(UpdateCartData data) {
                if (view != null) {
                    view.hideProgressLoading();
                    if (!data.isSuccess()) {
                        view.showToastMessageRed(data.getMessage());
                    } else {
                        view.showMerchantVoucherListBottomsheet(shopGroupAvailableData);
                    }
                }
            }
        };
    }

    private Subscriber<UpdateCartData> getSubscriberUpdateCartPromoGlobal(PromoStackingData promoStackingData,
                                                                          int stateGoTo) {
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
                    processInitialGetCartData(view.getCartId(), cartListData == null, false);
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
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (CartItemData data : view.getAllAvailableCartDataList()) {
            if (!data.isError()) {
                cartItemDataList.add(data);
            }
        }
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
    public void reCalculateSubTotal(List<CartShopHolderData> dataList, ArrayList<InsuranceCartShops> insuranceCartShopsArrayList) {
        double totalCashback = 0;
        double totalPrice = 0;
        int totalItemQty = 0;
        int errorProductCount = 0;

        // Collect all Cart Item, if has no error and selected
        List<CartItemHolderData> allCartItemDataList = new ArrayList<>();
        for (CartShopHolderData cartShopHolderData : dataList) {
            if (cartShopHolderData.getShopGroupAvailableData().getCartItemDataList() != null) {
                if (!cartShopHolderData.getShopGroupAvailableData().isError()) {
                    if (cartShopHolderData.isAllSelected() || cartShopHolderData.isPartialSelected()) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
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
                    errorProductCount += cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().size();
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

                List<WholesalePriceData> wholesalePriceDataList = data.getCartItemData().getOriginData().getWholesalePriceData();
                boolean hasCalculateWholesalePrice = false;
                if (wholesalePriceDataList != null && wholesalePriceDataList.size() > 0) {
                    double subTotalWholesalePrice = 0;
                    double itemCashback = 0;
                    for (WholesalePriceData wholesalePriceData : wholesalePriceDataList) {
                        if (itemQty >= wholesalePriceData.getQtyMin()) {
                            subTotalWholesalePrice = itemQty * wholesalePriceData.getPrdPrc();
                            hasCalculateWholesalePrice = true;
                            String wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    wholesalePriceData.getPrdPrc(), false);
                            data.getCartItemData().getOriginData().setWholesalePriceFormatted(wholesalePriceFormatted);
                            break;
                        }
                    }
                    if (!hasCalculateWholesalePrice) {
                        if (itemQty > wholesalePriceDataList.get(wholesalePriceDataList.size() - 1).getPrdPrc()) {
                            subTotalWholesalePrice = itemQty * wholesalePriceDataList.get(wholesalePriceDataList.size() - 1).getPrdPrc();
                            String wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                    wholesalePriceDataList.get(wholesalePriceDataList.size() - 1).getPrdPrc(), false);
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

        insuranceChecked = true;
        for (InsuranceCartShops insuranceCartShops : insuranceCartShopsArrayList) {

            if (insuranceCartShops != null &&
                    insuranceCartShops.getShopItemsList() != null &&
                    insuranceCartShops.getShopItemsList().get(0) != null &&
                    insuranceCartShops.getShopItemsList().size() > 0 &&
                    insuranceCartShops.getShopItemsList().get(0).getDigitalProductList().get(0) != null) {

                for (InsuranceCartShopItems insuranceCartShopItems : insuranceCartShops.getShopItemsList()) {
                    for (InsuranceCartDigitalProduct insuranceCartDigitalProduct : insuranceCartShopItems.getDigitalProductList()) {
                        if (insuranceCartDigitalProduct.getOptIn()) {
                            totalPrice += insuranceCartDigitalProduct.getPricePerProduct();
                            totalItemQty += 1;
                        } else {
                            insuranceChecked = false;
                        }
                    }
                }
            }
        }

        String totalPriceString = "-";
        if (totalPrice > 0) {
            totalPriceString = CurrencyFormatUtil.convertPriceValueToIdrFormat(((long) totalPrice), false);
        }
        view.updateCashback(totalCashback);
        boolean selectAllItem = view.getAllAvailableCartDataList().size() == allCartItemDataList.size() + errorProductCount &&
                allCartItemDataList.size() > 0 && insuranceChecked;
        boolean unselectAllItem = allCartItemDataList.size() == 0;
        view.renderDetailInfoSubTotal(String.valueOf(totalItemQty), totalPriceString, selectAllItem, unselectAllItem, dataList.isEmpty());

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
                    view.stopCartPerformanceTrace();
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
                    view.stopCartPerformanceTrace();
                }
            }
        };
    }

    @NonNull
    private Subscriber<DeleteAndRefreshCartListData> getSubscriberDeleteAndRefreshCart(List<Integer> toBeDeletedCartIds,
                                                                                       List<CartItemData> removedCartItems,
                                                                                       boolean removeAllItems,
                                                                                       boolean removeInsurance) {
        return new Subscriber<DeleteAndRefreshCartListData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (view != null) {
                    view.hideProgressLoading();
                    e.printStackTrace();
                    String errorMessage = e.getMessage();
                    if (!(e instanceof CartResponseErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(view.getActivity(), e);
                    }
                    view.showToastMessageRed(errorMessage);
                }
            }

            @Override
            public void onNext(DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                if (view != null) {
                    view.hideProgressLoading();
                    view.renderLoadGetCartDataFinish();

                    if (deleteAndRefreshCartListData.getDeleteCartData().isSuccess()) {

                        if (removeInsurance) {
                            processDeleteCartInsurance(view.getInsuranceCartShopData(), false);
                        }

                        if (removeAllItems) {
                            processInitialGetCartData(view.getCartId(), false, false);
                        } else {
                            view.onDeleteCartDataSuccess(toBeDeletedCartIds);
                        }

                    } else {
                        view.showToastMessageRed(
                                deleteAndRefreshCartListData.getDeleteCartData().getMessage()
                        );
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
                    view.renderErrorToShipmentForm(errorMessage);
                    processInitialGetCartData(view.getCartId(), cartListData == null, false);
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
                                generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_1),
                                cartItemDataList,
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
            for (CartItemHolderData cartItemHolderData : cartShopHolderDataList.get(0).getShopGroupAvailableData().getCartItemDataList()) {
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
                    for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupAvailableData().getCartItemDataList()) {
                        if (!cartItemHolderData.isSelected()) {
                            selectedItem++;
                        }
                    }
                    if (!selectPartialShopAndItem && selectedItem != cartShopHolderData.getShopGroupAvailableData().getCartItemDataList().size()) {
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
                    view.stopCartPerformanceTrace();
                }
            }

            @Override
            public void onNext(ResetAndRefreshCartListData resetAndRefreshCartListData) {
                if (view != null) {
                    view.hideProgressLoading();
                    view.renderLoadGetCartDataFinish();
                    if (resetAndRefreshCartListData.getCartListData() == null) {
                        view.renderErrorInitialGetCartListData(resetAndRefreshCartListData.getResetCartData().getMessage());
                        view.stopCartPerformanceTrace();
                    } else {
                        view.renderInitialGetCartListDataSuccess(resetAndRefreshCartListData.getCartListData());
                        view.stopCartPerformanceTrace();
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
    public void processCancelAutoApplyPromoStackAfterClash(ArrayList<String> oldPromoList,
                                                           ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                                           String type) {
        view.showProgressLoading();
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), oldPromoList);
        clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), new ClearCacheAutoApplyAfterClashSubscriber(view, this, newPromoList, type));
    }

    @Override
    public void processApplyPromoStackAfterClash(ArrayList<ClashingVoucherOrderUiModel> newPromoList,
                                                 String type) {
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

    @Override
    public Map<String, Object> generateRecommendationDataAnalytics(List<CartRecommendationItemHolderData> cartRecommendationItemHolderDataList, boolean isEmptyCart) {
        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();

        int position = 1;
        for (CartRecommendationItemHolderData cartRecommendationItemHolderData : cartRecommendationItemHolderDataList) {
            EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = getEnhancedECommerceProductRecommendationMapData(cartRecommendationItemHolderData.getRecommendationItem(), isEmptyCart, position);
            enhancedECommerceCartMapData.addImpression(enhancedECommerceProductCartMapData.getProduct());
            position++;
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        return enhancedECommerceCartMapData.getCartMap();
    }

    @Override
    public Map<String, Object> generateWishlistDataImpressionAnalytics(List<CartWishlistItemHolderData> cartWishlistItemHolderDataList, boolean isEmptyCart) {
        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();

        int position = 0;
        for (CartWishlistItemHolderData cartWishlistItemHolderData : cartWishlistItemHolderDataList) {
            EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = getProductWishlistImpressionMapData(cartWishlistItemHolderData, isEmptyCart, position);
            enhancedECommerceCartMapData.addImpression(enhancedECommerceProductCartMapData.getProduct());
            position++;
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        return enhancedECommerceCartMapData.getCartMap();
    }

    @Override
    public Map<String, Object> generateRecentViewDataImpressionAnalytics(List<CartRecentViewItemHolderData> cartRecentViewItemHolderDataList, boolean isEmptyCart) {
        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();

        int position = 0;
        for (CartRecentViewItemHolderData cartRecentViewItemHolderData : cartRecentViewItemHolderDataList) {
            EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = getProductRecentViewImpressionMapData(cartRecentViewItemHolderData, isEmptyCart, position);
            enhancedECommerceCartMapData.addImpression(enhancedECommerceProductCartMapData.getProduct());
            position++;
        }

        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        return enhancedECommerceCartMapData.getCartMap();
    }

    @Override
    public Map<String, Object> generateRecommendationDataOnClickAnalytics(RecommendationItem cartRecommendation, boolean isEmptyCart, int position) {
//        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();
        EnhancedECommerceEmptyCartData enhancedECommerceProductCartMapData = getEnhancedECommerceProductRecommendationOnClickMapData(cartRecommendation, isEmptyCart, position);
//        enhancedECommerceCartMapData.addClick(enhancedECommerceProductCartMapData.getData());
//        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        return enhancedECommerceProductCartMapData.getData();
    }

    @NonNull
    private EnhancedECommerceProductCartMapData getEnhancedECommerceProductRecommendationMapData(RecommendationItem recommendationItem, boolean isEmptyCart, int position) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(recommendationItem.getProductId()));
        enhancedECommerceProductCartMapData.setProductName(recommendationItem.getName());
        enhancedECommerceProductCartMapData.setPrice(recommendationItem.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(recommendationItem.getCategoryBreadcrumbs())
                ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                : recommendationItem.getCategoryBreadcrumbs());
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setListName(getActionFieldListStr(isEmptyCart, recommendationItem));
        enhancedECommerceProductCartMapData.setPosition(String.valueOf(position));
        if (recommendationItem.isFreeOngkirActive()) {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR);
        } else {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        }
        return enhancedECommerceProductCartMapData;
    }

    @NonNull
    private EnhancedECommerceEmptyCartData getEnhancedECommerceProductRecommendationOnClickMapData(RecommendationItem recommendationItem, boolean isEmptyCart, int position) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(recommendationItem.getProductId()));
        enhancedECommerceProductCartMapData.setProductName(recommendationItem.getName());
        enhancedECommerceProductCartMapData.setPrice(recommendationItem.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(recommendationItem.getCategoryBreadcrumbs())
                ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                : recommendationItem.getCategoryBreadcrumbs());
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
//        enhancedECommerceProductCartMapData.setListName(getActionFieldListStr(isEmptyCart, recommendationItem));
        enhancedECommerceProductCartMapData.setPosition(String.valueOf(position));
        enhancedECommerceProductCartMapData.setAttribution(EnhancedECommerceProductCartMapData.RECOMMENDATION_ATTRIBUTION);
        if (recommendationItem.isFreeOngkirActive()) {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR);
        } else {
            enhancedECommerceProductCartMapData.setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        }
        List<Map<String, Object>> productsData = new ArrayList<>();
        productsData.add(enhancedECommerceProductCartMapData.getProduct());
        return getEnhancedECommerceOnClickEmptyCartData(productsData, getActionFieldListStr(isEmptyCart, recommendationItem));
//        return enhancedECommerceProductCartMapData;
    }

    @NonNull
    private EnhancedECommerceEmptyCartData getEnhancedECommerceOnClickEmptyCartData(List<Map<String, Object>> productsData, String valueSectionName) {
        EnhancedECommerceEmptyCartActionFieldData enhancedECommerceEmptyCartActionFieldData =
                new EnhancedECommerceEmptyCartActionFieldData();
        enhancedECommerceEmptyCartActionFieldData.setList(valueSectionName);

        EnhancedECommerceEmptyCartClickData enhancedECommerceEmptyCartClickData =
                new EnhancedECommerceEmptyCartClickData();
        enhancedECommerceEmptyCartClickData.setActionField(enhancedECommerceEmptyCartActionFieldData.getData());
        enhancedECommerceEmptyCartClickData.setProducts(productsData);

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = new EnhancedECommerceEmptyCartData();
        enhancedECommerceEmptyCart.setClickData(enhancedECommerceEmptyCartClickData.getData());
        return enhancedECommerceEmptyCart;
    }

    private String getActionFieldListStr(boolean isCartEmpty, RecommendationItem recommendationItem) {
        String listName;
        if (isCartEmpty) {
            listName = EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART;
        } else {
            listName = EnhancedECommerceActionField.LIST_CART_RECOMMENDATION;
        }
        listName += recommendationItem.getRecommendationType();
        if (recommendationItem.isTopAds()) {
            listName += EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_TOPADS_TYPE;
        }
        return listName;
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
        enhancedECommerceProductCartMapData.setDimension53(cartItemData.getOriginData().getPriceOriginal() > 0);
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
        enhancedECommerceProductCartMapData.setWarehouseId(String.valueOf(cartItemData.getOriginData().getWarehouseId()));
        enhancedECommerceProductCartMapData.setProductWeight(String.valueOf(cartItemData.getOriginData().getWeightPlan()));
        enhancedECommerceProductCartMapData.setCartId(String.valueOf(cartItemData.getOriginData().getCartId()));
        enhancedECommerceProductCartMapData.setPromoCode(cartItemData.getOriginData().getPromoCodes());
        enhancedECommerceProductCartMapData.setPromoDetails(cartItemData.getOriginData().getPromoDetails());
        enhancedECommerceProductCartMapData.setDimension83(cartItemData.getOriginData().isFreeShipping() ?
                EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR : EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        return enhancedECommerceProductCartMapData;
    }

    @Override
    public Map<String, Object> generateCheckoutDataAnalytics(List<CartItemData> cartItemDataList, String step) {
        Map<String, Object> checkoutMapData = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setStep(step);
        if (step.equals(EnhancedECommerceActionField.STEP_0)) {
            enhancedECommerceActionField.setOption(EnhancedECommerceActionField.STEP_0_OPTION_VIEW_CART_PAGE);
        } else if (step.equals(EnhancedECommerceActionField.STEP_1)) {
            enhancedECommerceActionField.setOption(EnhancedECommerceActionField.STEP_1_OPTION_CART_PAGE_LOADED);
        }

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
    public void processGetRecommendationData(int page, List<String> allProductIds) {
        view.showItemLoading();
        RequestParams requestParam = getRecommendationUseCase.getRecomParams(
                page, "recom_widget", "cart", allProductIds, "");
        getRecommendationUseCase.execute(requestParam, new GetRecommendationSubscriber(view, this));
    }

    @Override
    public void processAddToCart(Object productModel) {
        view.showProgressLoading();

        int productId = 0;
        int shopId = 0;
        String externalSource = "";
        if (productModel instanceof CartWishlistItemHolderData) {
            CartWishlistItemHolderData cartWishlistItemHolderData = (CartWishlistItemHolderData) productModel;
            productId = Integer.parseInt(cartWishlistItemHolderData.getId());
            shopId = Integer.parseInt(cartWishlistItemHolderData.getShopId());
            externalSource = AddToCartRequestParams.Companion.getATC_FROM_WISHLIST();
        } else if (productModel instanceof CartRecentViewItemHolderData) {
            CartRecentViewItemHolderData cartRecentViewItemHolderData = (CartRecentViewItemHolderData) productModel;
            productId = Integer.parseInt(cartRecentViewItemHolderData.getId());
            shopId = Integer.parseInt(cartRecentViewItemHolderData.getShopId());
            externalSource = AddToCartRequestParams.Companion.getATC_FROM_RECENT_VIEW();
        } else if (productModel instanceof CartRecommendationItemHolderData) {
            CartRecommendationItemHolderData cartRecommendationItemHolderData = (CartRecommendationItemHolderData) productModel;
            productId = cartRecommendationItemHolderData.getRecommendationItem().getProductId();
            shopId = cartRecommendationItemHolderData.getRecommendationItem().getShopId();
            externalSource = AddToCartRequestParams.Companion.getATC_FROM_RECOMMENDATION();
        }

        AddToCartRequestParams addToCartRequestParams = new AddToCartRequestParams();
        addToCartRequestParams.setProductId(productId);
        addToCartRequestParams.setShopId(shopId);
        addToCartRequestParams.setQuantity(0);
        addToCartRequestParams.setNotes("");
        addToCartRequestParams.setWarehouseId(0);
        addToCartRequestParams.setAtcFromExternalSource(externalSource);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams);
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AddToCartSubscriber(view, this, productModel));
    }

    @Override
    public Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartWishlistItemHolderData cartWishlistItemHolderData,
                                                                          AddToCartDataModel addToCartDataResponseModel, boolean isCartEmpty) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setList(isCartEmpty ? EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART : EnhancedECommerceActionField.LIST_WISHLIST);
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductName(cartWishlistItemHolderData.getName());
        enhancedECommerceProductCartMapData.setProductID(cartWishlistItemHolderData.getId());
        enhancedECommerceProductCartMapData.setPrice(cartWishlistItemHolderData.getRawPrice());
        enhancedECommerceProductCartMapData.setCategory(cartWishlistItemHolderData.getCategory());
        enhancedECommerceProductCartMapData.setQty(cartWishlistItemHolderData.getMinOrder());
        enhancedECommerceProductCartMapData.setShopId(cartWishlistItemHolderData.getShopId());
        enhancedECommerceProductCartMapData.setShopType(cartWishlistItemHolderData.getShopType());
        enhancedECommerceProductCartMapData.setShopName(cartWishlistItemHolderData.getShopName());
        enhancedECommerceProductCartMapData.setPicture(cartWishlistItemHolderData.getImageUrl());
        enhancedECommerceProductCartMapData.setUrl(cartWishlistItemHolderData.getUrl());
        enhancedECommerceProductCartMapData.setDimension45(String.valueOf(addToCartDataResponseModel.getData().getCartId()));
        enhancedECommerceProductCartMapData.setBrand("");
        enhancedECommerceProductCartMapData.setCategoryId("");
        enhancedECommerceProductCartMapData.setVariant("");

        EnhancedECommerceAdd enhancedECommerceAdd = new EnhancedECommerceAdd();
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.getActionFieldMap());
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.getProduct());

        stringObjectMap.put("currencyCode", "IDR");
        stringObjectMap.put(EnhancedECommerceAdd.Companion.getKEY_ADD(), enhancedECommerceAdd.getAddMap());
        return stringObjectMap;
    }

    @Override
    public Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData cartRecentViewItemHolderData,
                                                                          AddToCartDataModel addToCartDataResponseModel, boolean isCartEmpty) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setList(isCartEmpty ? EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART : EnhancedECommerceActionField.LIST_RECENT_VIEW);
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductName(cartRecentViewItemHolderData.getName());
        enhancedECommerceProductCartMapData.setProductID(cartRecentViewItemHolderData.getId());
        enhancedECommerceProductCartMapData.setPrice(cartRecentViewItemHolderData.getPrice());
        enhancedECommerceProductCartMapData.setQty(cartRecentViewItemHolderData.getMinOrder());
        enhancedECommerceProductCartMapData.setDimension52(cartRecentViewItemHolderData.getShopId());
        enhancedECommerceProductCartMapData.setDimension57(cartRecentViewItemHolderData.getShopName());
        enhancedECommerceProductCartMapData.setDimension59(cartRecentViewItemHolderData.getShopType());
        enhancedECommerceProductCartMapData.setDimension77(String.valueOf(addToCartDataResponseModel.getData().getCartId()));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategoryId("");
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);

        EnhancedECommerceAdd enhancedECommerceAdd = new EnhancedECommerceAdd();
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.getActionFieldMap());
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.getProduct());

        stringObjectMap.put("currencyCode", "IDR");
        stringObjectMap.put(EnhancedECommerceAdd.Companion.getKEY_ADD(), enhancedECommerceAdd.getAddMap());
        return stringObjectMap;
    }

    @Override
    public Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData cartRecommendationItemHolderData,
                                                                          AddToCartDataModel addToCartDataResponseModel, boolean isCartEmpty) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setList(getActionFieldListStr(isCartEmpty, cartRecommendationItemHolderData.getRecommendationItem()));
        EnhancedECommerceRecomProductCartMapData enhancedECommerceProductCartMapData = new EnhancedECommerceRecomProductCartMapData();
        enhancedECommerceProductCartMapData.setProductName(cartRecommendationItemHolderData.getRecommendationItem().getName());
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(cartRecommendationItemHolderData.getRecommendationItem().getProductId()));
        enhancedECommerceProductCartMapData.setPrice(cartRecommendationItemHolderData.getRecommendationItem().getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceProductCartMapData.setCategory(cartRecommendationItemHolderData.getRecommendationItem().getCategoryBreadcrumbs());
        enhancedECommerceProductCartMapData.setQty(cartRecommendationItemHolderData.getRecommendationItem().getMinOrder());
        enhancedECommerceProductCartMapData.setShopId(String.valueOf(cartRecommendationItemHolderData.getRecommendationItem().getShopId()));
        enhancedECommerceProductCartMapData.setShopType(cartRecommendationItemHolderData.getRecommendationItem().getShopType());
        enhancedECommerceProductCartMapData.setShopName(cartRecommendationItemHolderData.getRecommendationItem().getShopName());
        enhancedECommerceProductCartMapData.setDimension45(String.valueOf(addToCartDataResponseModel.getData().getCartId()));
        enhancedECommerceProductCartMapData.setDimension53(cartRecommendationItemHolderData.getRecommendationItem().getDiscountPercentageInt() > 0);
        enhancedECommerceProductCartMapData.setDimension40(addToCartDataResponseModel.getData().getTrackerListName());

        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategoryId("");
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);

        EnhancedECommerceAdd enhancedECommerceAdd = new EnhancedECommerceAdd();
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.getActionFieldMap());
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.getProduct());

        stringObjectMap.put("currencyCode", "IDR");
        stringObjectMap.put(EnhancedECommerceAdd.getKEY_ADD(), enhancedECommerceAdd.getAddMap());
        return stringObjectMap;
    }

    @Override
    public Map<String, Object> generateRecentViewProductClickDataLayer(CartRecentViewItemHolderData cartRecentViewItemHolderData, int position) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setList(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW);
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = new EnhancedECommerceProductCartMapData();

        enhancedECommerceProductCartMapData.setProductName(cartRecentViewItemHolderData.getName());
        enhancedECommerceProductCartMapData.setProductID(cartRecentViewItemHolderData.getId());
        enhancedECommerceProductCartMapData.setPrice(cartRecentViewItemHolderData.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceProductCartMapData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setPosition(String.valueOf(position));

        EnhancedECommerceAdd enhancedECommerceAdd = new EnhancedECommerceAdd();
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.getActionFieldMap());
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.getProduct());

        stringObjectMap.put("currencyCode", "IDR");
        stringObjectMap.put(EnhancedECommerceAdd.getKEY_ADD(), enhancedECommerceAdd.getAddMap());
        return stringObjectMap;
    }

    @Override
    public Map<String, Object> generateWishlistProductClickDataLayer(CartWishlistItemHolderData cartWishlistItemHolderData, int position) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        EnhancedECommerceActionField enhancedECommerceActionField = new EnhancedECommerceActionField();
        enhancedECommerceActionField.setList(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST);
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData = new EnhancedECommerceProductCartMapData();

        enhancedECommerceProductCartMapData.setProductName(cartWishlistItemHolderData.getName());
        enhancedECommerceProductCartMapData.setProductID(cartWishlistItemHolderData.getId());
        enhancedECommerceProductCartMapData.setPrice(cartWishlistItemHolderData.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceProductCartMapData.setCategory(cartWishlistItemHolderData.getCategory());
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setPosition(String.valueOf(position));

        EnhancedECommerceAdd enhancedECommerceAdd = new EnhancedECommerceAdd();
        enhancedECommerceAdd.setActionField(enhancedECommerceActionField.getActionFieldMap());
        enhancedECommerceAdd.addProduct(enhancedECommerceProductCartMapData.getProduct());

        stringObjectMap.put("currencyCode", "IDR");
        stringObjectMap.put(EnhancedECommerceAdd.getKEY_ADD(), enhancedECommerceAdd.getAddMap());
        return stringObjectMap;
    }

    @NonNull
    private EnhancedECommerceProductCartMapData getProductRecentViewImpressionMapData(CartRecentViewItemHolderData recentViewItemHolderData, boolean isEmptyCart, int position) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductID(recentViewItemHolderData.getId());
        enhancedECommerceProductCartMapData.setProductName(recentViewItemHolderData.getName());
        enhancedECommerceProductCartMapData.setPrice(recentViewItemHolderData.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategory(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);

        if (isEmptyCart) {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART);
        } else {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW);
        }

        enhancedECommerceProductCartMapData.setPosition(String.valueOf(position));
        return enhancedECommerceProductCartMapData;
    }

    @Override
    public Map<String, Object> generateRecentViewProductClickEmptyCartDataLayer(CartRecentViewItemHolderData cartRecentViewItemHolderData, int position) {
        EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                new EnhancedECommerceEmptyCartProductData();
        enhancedECommerceEmptyCartProductData.setProductID(cartRecentViewItemHolderData.getId());
        enhancedECommerceEmptyCartProductData.setProductName(cartRecentViewItemHolderData.getName());
        enhancedECommerceEmptyCartProductData.setPrice(cartRecentViewItemHolderData.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(position));
        enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        List<Map<String, Object>> productsData = new ArrayList<>();
        productsData.add(enhancedECommerceEmptyCartProductData.getProduct());

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = getEnhancedECommerceOnClickEmptyCartData(
                productsData, EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART);

        return enhancedECommerceEmptyCart.getData();
    }

    @NonNull
    private EnhancedECommerceProductCartMapData getProductWishlistImpressionMapData(CartWishlistItemHolderData wishlistItemHolderData, boolean isEmptyCart, int position) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductID(wishlistItemHolderData.getId());
        enhancedECommerceProductCartMapData.setProductName(wishlistItemHolderData.getName());
        enhancedECommerceProductCartMapData.setPrice(wishlistItemHolderData.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setCategory(wishlistItemHolderData.getCategory());
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);

        if (isEmptyCart) {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART);
        } else {
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST);
        }

        enhancedECommerceProductCartMapData.setPosition(String.valueOf(position));
        return enhancedECommerceProductCartMapData;
    }

    @Override
    public Map<String, Object> generateWishlistProductClickEmptyCartDataLayer(CartWishlistItemHolderData cartWishlistItemHolderData, int position) {
        EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                new EnhancedECommerceEmptyCartProductData();
        enhancedECommerceEmptyCartProductData.setProductID(cartWishlistItemHolderData.getId());
        enhancedECommerceEmptyCartProductData.setProductName(cartWishlistItemHolderData.getName());
        enhancedECommerceEmptyCartProductData.setPrice(cartWishlistItemHolderData.getPrice().replaceAll("[^0-9]", ""));
        enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setCategory(cartWishlistItemHolderData.getCategory());
        enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(position));
        enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        List<Map<String, Object>> productsData = new ArrayList<>();
        productsData.add(enhancedECommerceEmptyCartProductData.getProduct());

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = getEnhancedECommerceOnClickEmptyCartData(
                productsData, EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART);

        return enhancedECommerceEmptyCart.getData();
    }
}
