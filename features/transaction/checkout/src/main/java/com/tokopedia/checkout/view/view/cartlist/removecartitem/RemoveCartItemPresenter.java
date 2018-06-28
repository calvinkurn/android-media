package com.tokopedia.checkout.view.view.cartlist.removecartitem;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.view.view.cartlist.removecartitem.viewmodel.CartProductHeaderViewModel;
import com.tokopedia.checkout.view.view.cartlist.removecartitem.viewmodel.CartProductItemViewModel;
import com.tokopedia.transactionanalytics.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.EnhancedECommerceProductCartMapData;
import com.tokopedia.transactiondata.entity.request.RemoveCartRequest;
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
 * @author Irfan Khoirul on 24/05/18.
 */

public class RemoveCartItemPresenter extends BaseDaggerPresenter<RemoveCartItemContract.View>
        implements RemoveCartItemContract.Presenter {

    private CartProductHeaderViewModel cartProductHeaderViewModel;
    private List<CartProductItemViewModel> cartProductItemViewModelList = new ArrayList<>();
    private final CompositeSubscription compositeSubscription;
    private final DeleteCartUseCase deleteCartUseCase;
    private List<CartItemData> cartItemDataList;

    @Inject
    public RemoveCartItemPresenter(CompositeSubscription compositeSubscription,
                                   DeleteCartUseCase deleteCartUseCase) {
        this.compositeSubscription = compositeSubscription;
        this.deleteCartUseCase = deleteCartUseCase;
    }

    @Override
    public void detachView() {
        compositeSubscription.unsubscribe();
        super.detachView();
    }

    @Override
    public void setCartProductItemViewModelList(List<CartItemData> cartItemDataList) {
        this.cartItemDataList = cartItemDataList;
        for (CartItemData cartItemData : cartItemDataList) {
            CartProductItemViewModel cartProductItemViewModel = new CartProductItemViewModel();
            cartProductItemViewModel.setCartItemData(cartItemData);
            cartProductItemViewModelList.add(cartProductItemViewModel);
        }
    }

    @Override
    public List<CartProductItemViewModel> getCartProductItemViewModelList() {
        return cartProductItemViewModelList;
    }

    @Override
    public void setCartProductHeaderViewModel(CartProductHeaderViewModel cartProductHeaderViewModel) {
        this.cartProductHeaderViewModel = cartProductHeaderViewModel;
    }

    @Override
    public CartProductHeaderViewModel getCartProductHeaderViewModel() {
        return cartProductHeaderViewModel;
    }

    @Override
    public void processRemoveCartItem(List<String> cartIdStringList, boolean addToWishlist) {
        getView().showLoading();
        List<Integer> cartIdIntList = new ArrayList<>();
        for (String cartIdString : cartIdStringList) {
            cartIdIntList.add(Integer.parseInt(cartIdString));
        }

        RemoveCartRequest removeCartRequest = new RemoveCartRequest.Builder()
                .addWishlist(addToWishlist ? 1 : 0)
                .cartIds(cartIdIntList)
                .build();

        TKPDMapParam<String, String> paramDelete = new TKPDMapParam<>();
        paramDelete.put("params", new Gson().toJson(removeCartRequest));

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
                                getView().hideLoading();
                                throwable.printStackTrace();
                                getView().showError(getView().getActivity().getString(R.string.default_request_error_unknown));
                            }

                            @Override
                            public void onNext(DeleteCartData deleteCartData) {
                                getView().hideLoading();
                                if (deleteCartData.isSuccess()) {
                                    String messageSuccess = getView().getActivity()
                                            .getString(R.string.label_delete_cart_item_success);
                                    getView().renderSuccessDeleteAllCart(messageSuccess);
                                } else {
                                    String messageFailed = getView().getActivity().
                                            getString(R.string.label_delete_cart_item_failed);
                                    getView().renderOnFailureDeleteCart(messageFailed);
                                }
                            }
                        })
        );
    }

    @Override
    public Map<String, Object> generateCartDataAnalytics(List<String> selectedCartItemIds,
                                                         String enhancedECommerceAction) {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (String cartItemId : selectedCartItemIds) {
            for (CartItemData cartItemData : this.cartItemDataList) {
                if (cartItemId.equals(String.valueOf(cartItemData.getOriginData().getCartId()))) {
                    cartItemDataList.add(cartItemData);
                }
            }
        }
        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();

        enhancedECommerceCartMapData.setCurrencyCode("IDR");
        enhancedECommerceCartMapData.setAction(enhancedECommerceAction);

        for (CartItemData cartItemData : cartItemDataList) {
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
