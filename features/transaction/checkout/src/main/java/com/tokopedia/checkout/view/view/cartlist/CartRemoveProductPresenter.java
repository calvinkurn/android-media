package com.tokopedia.checkout.view.view.cartlist;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.DeleteUpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.usecase.DeleteCartUpdateCartUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.view.base.CartMvpPresenter;
import com.tokopedia.transactionanalytics.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.EnhancedECommerceProductCartMapData;
import com.tokopedia.transactiondata.entity.request.RemoveCartRequest;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 05/02/18
 */

public class CartRemoveProductPresenter
        extends CartMvpPresenter<IRemoveProductListView<List<CartItemData>>> {

    private final DeleteCartUseCase deleteCartUseCase;
    private final CompositeSubscription compositeSubscription;
    private final DeleteCartUpdateCartUseCase deleteCartUpdateCartUseCase;

    @Inject
    public CartRemoveProductPresenter(CompositeSubscription compositeSubscription,
                                      DeleteCartUseCase deleteCartUseCase,
                                      DeleteCartUpdateCartUseCase deleteCartUpdateCartUseCase) {
        this.compositeSubscription = compositeSubscription;
        this.deleteCartUseCase = deleteCartUseCase;
        this.deleteCartUpdateCartUseCase = deleteCartUpdateCartUseCase;

    }

    @Override
    public void attachView(IRemoveProductListView<List<CartItemData>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    public void getCartItems(List<CartItemData> cartItemModels) {
        getMvpView().showList(cartItemModels);
    }

    public void processDeleteCart(List<CartItemData> cartItemDataListForDelete, List<CartItemData> cartItemForUpdate, boolean addWishList) {
        List<Integer> ids = new ArrayList<>();

        for (CartItemData data : cartItemDataListForDelete) {
            ids.add(data.getOriginData().getCartId());
        }

        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .addWishlist(addWishList ? 1 : 0)
                .cartIds(ids)
                .build();

        TKPDMapParam<String, String> paramDelete = new TKPDMapParam<>();
        paramDelete.put("params", new Gson().toJson(removeCartRequest));

        List<UpdateCartRequest> updateCartRequestList = new ArrayList<>();

        for (CartItemData cartItemData : cartItemForUpdate) {
            updateCartRequestList.add(new UpdateCartRequest.Builder()
                    .cartId(cartItemData.getOriginData().getCartId())
                    .notes(cartItemData.getUpdatedData().getRemark())
                    .quantity(cartItemData.getUpdatedData().getQuantity())
                    .build()
            );
        }

        TKPDMapParam<String, String> paramUpdate = new TKPDMapParam<>();
        paramUpdate.put("carts", new Gson().toJson(updateCartRequestList));

        if (!updateCartRequestList.isEmpty()) {

            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(DeleteCartUpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART,
                    getMvpView().getGenerateParamAuth(paramDelete));
            requestParams.putObject(DeleteCartUpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                    getMvpView().getGenerateParamAuth(paramUpdate));

            compositeSubscription.add(
                    deleteCartUpdateCartUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .subscribe(new Subscriber<DeleteUpdateCartData>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    throwable.printStackTrace();
                                    getMvpView().showError(getMvpView().getActivity().getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onNext(DeleteUpdateCartData deleteUpdateCartData) {
                                    if (deleteUpdateCartData.isSuccess()) {
                                        String messageSuccess = getMvpView().getActivity()
                                                .getString(R.string.label_delete_cart_item_success);
                                        getMvpView().renderSuccessDeletePartialCart(messageSuccess);
                                    } else {
                                        String messageFailed = getMvpView().getActivity().
                                                getString(R.string.label_delete_cart_item_failed);
                                        getMvpView().renderOnFailureDeleteCart(messageFailed);
                                    }
                                }
                            })
            );
        } else {

            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(DeleteCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING, paramDelete);
            compositeSubscription.add(
                    deleteCartUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .subscribe(new Subscriber<DeleteCartData>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    throwable.printStackTrace();
                                    getMvpView().showError(getMvpView().getActivity().getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onNext(DeleteCartData deleteCartData) {
                                    if (deleteCartData.isSuccess()) {
                                        String messageSuccess = getMvpView().getActivity()
                                                .getString(R.string.label_delete_cart_item_success);
                                        getMvpView().renderSuccessDeleteAllCart(messageSuccess);
                                    } else {
                                        String messageFailed = getMvpView().getActivity().
                                                getString(R.string.label_delete_cart_item_failed);
                                        getMvpView().renderOnFailureDeleteCart(messageFailed);
                                    }
                                }
                            })
            );
        }
    }

    public Map<String, Object> generateCartDataAnalytics(CartItemData removedCartItem, String enhancedECommerceAction) {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        return generateCartDataAnalytics(cartItemDataList, enhancedECommerceAction);
    }

    public Map<String, Object> generateCartDataAnalytics(List<CartItemData> cartItemDataList, String enhancedECommerceAction) {

        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();

        enhancedECommerceCartMapData.setCurrencyCode("IDR");
        enhancedECommerceCartMapData.setAction(enhancedECommerceAction);

        for (CartItemData cartItemData : cartItemDataList) {
            EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                    new EnhancedECommerceProductCartMapData();
            enhancedECommerceProductCartMapData.setProductName(cartItemData.getOriginData().getProductName());
            enhancedECommerceProductCartMapData.setProductID(String.valueOf(cartItemData.getOriginData().getProductId()));
            enhancedECommerceProductCartMapData.setPrice(cartItemData.getOriginData().getPriceFormatted());
            enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);

            enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(cartItemData.getOriginData().getCategoryForAnalytics())
                    ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    : cartItemData.getOriginData().getCategoryForAnalytics());
            enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceProductCartMapData.setQty(cartItemData.getUpdatedData().getQuantity());
            enhancedECommerceProductCartMapData.setShopId(cartItemData.getOriginData().getShopId());
            //   product.setShopType(generateShopType(productData.getShopInfo()));
            enhancedECommerceProductCartMapData.setShopName(cartItemData.getOriginData().getShopName());
            enhancedECommerceProductCartMapData.setCategoryId(cartItemData.getOriginData().getCategoryId());
            enhancedECommerceProductCartMapData.setDimension38(
                    TextUtils.isEmpty(cartItemData.getOriginData().getTrackerAttribution())
                            ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                            : cartItemData.getOriginData().getTrackerAttribution()
            );
            enhancedECommerceProductCartMapData.setAttribution(
                    TextUtils.isEmpty(cartItemData.getOriginData().getTrackerAttribution())
                            ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                            : cartItemData.getOriginData().getTrackerAttribution()
            );
            enhancedECommerceProductCartMapData.setDimension40(
                    TextUtils.isEmpty(cartItemData.getOriginData().getTrackerListName())
                            ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                            : cartItemData.getOriginData().getTrackerListName()
            );
            enhancedECommerceProductCartMapData.setListName(
                    TextUtils.isEmpty(cartItemData.getOriginData().getTrackerListName())
                            ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                            : cartItemData.getOriginData().getTrackerListName()
            );
            enhancedECommerceCartMapData.addProduct(enhancedECommerceProductCartMapData.getProduct());
        }
        return enhancedECommerceCartMapData.getCartMap();

    }
}
