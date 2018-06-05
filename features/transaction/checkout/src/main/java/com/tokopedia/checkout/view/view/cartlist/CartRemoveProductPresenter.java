package com.tokopedia.checkout.view.view.cartlist;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.DeleteUpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.usecase.DeleteCartUpdateCartUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.view.base.CartMvpPresenter;
import com.tokopedia.transactiondata.entity.request.RemoveCartRequest;
import com.tokopedia.transactiondata.entity.request.UpdateCartRequest;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

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

}
