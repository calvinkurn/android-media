package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.DeleteAndRefreshCartListData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class DeleteCartGetCartListUseCase extends UseCase<DeleteAndRefreshCartListData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_CART";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final Context context;

    @Inject
    public DeleteCartGetCartListUseCase(Context context, ICartRepository cartRepository, ICartMapper cartMapper) {
        this.context = context;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<DeleteAndRefreshCartListData> createObservable(RequestParams requestParams) {

        final TKPDMapParam<String, String> paramDelete = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART);

        final TKPDMapParam<String, String> paramGetCart = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_GET_CART);

        return Observable.just(new DeleteAndRefreshCartListData())
                .flatMap(new Func1<DeleteAndRefreshCartListData, Observable<DeleteAndRefreshCartListData>>() {
                    @Override
                    public Observable<DeleteAndRefreshCartListData> call(final DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                        return cartRepository.deleteCartData(paramDelete).map(
                                new Func1<DeleteCartDataResponse, DeleteAndRefreshCartListData>() {
                                    @Override
                                    public DeleteAndRefreshCartListData call(
                                            DeleteCartDataResponse deleteCartDataResponse
                                    ) {
                                        deleteAndRefreshCartListData.setDeleteCartData(
                                                cartMapper.convertToDeleteCartData(deleteCartDataResponse)
                                        );
                                        return deleteAndRefreshCartListData;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<DeleteAndRefreshCartListData, Observable<DeleteAndRefreshCartListData>>() {
                    @Override
                    public Observable<DeleteAndRefreshCartListData> call(final DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                        return cartRepository.getShopGroupList(paramGetCart)
                                .map(new Func1<CartDataListResponse, DeleteAndRefreshCartListData>() {
                                    @Override
                                    public DeleteAndRefreshCartListData call(CartDataListResponse cartDataListResponse) {
                                        deleteAndRefreshCartListData.setCartListData(
                                                cartMapper.convertToCartItemDataList(context, cartDataListResponse)
                                        );
                                        return deleteAndRefreshCartListData;
                                    }
                                });
                    }
                });
    }
}
