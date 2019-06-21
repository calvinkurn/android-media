package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.DeleteAndRefreshCartListData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.checkout.domain.usecase.UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class DeleteCartGetCartListUseCase extends UseCase<DeleteAndRefreshCartListData> {
    public static final String PARAM_IS_DELETE_ALL_DATA
            = "PARAM_IS_DELETE_ALL_DATA";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_CART";
    public static final String PARAM_TO_BE_REMOVED_PROMO_CODES
            = "PARAM_TO_BE_REMOVED_PROMO_CODES";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final Context context;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;

    @Inject
    public DeleteCartGetCartListUseCase(Context context, ICartRepository cartRepository, ICartMapper cartMapper,
                                        ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase) {
        this.context = context;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.clearCacheAutoApplyStackUseCase = clearCacheAutoApplyStackUseCase;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<DeleteAndRefreshCartListData> createObservable(RequestParams requestParams) {

        final TKPDMapParam<String, String> paramDelete = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART);

        final TKPDMapParam<String, String> paramGetCart = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_GET_CART);

        final TKPDMapParam<String, String> paramUpdateCart = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        final boolean isDeleteAllCart = requestParams.getBoolean(PARAM_IS_DELETE_ALL_DATA, false);

        final ArrayList<String> toBeDeletedPromoCode = (ArrayList<String>) requestParams.getObject(PARAM_TO_BE_REMOVED_PROMO_CODES);

        return Observable.just(new DeleteAndRefreshCartListData())
                .flatMap(new Func1<DeleteAndRefreshCartListData, Observable<DeleteAndRefreshCartListData>>() {
                    @Override
                    public Observable<DeleteAndRefreshCartListData> call(DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                        if (isDeleteAllCart) {
                            return Observable.just(deleteAndRefreshCartListData);
                        }
                        return cartRepository.updateCartData(paramUpdateCart).map(new Func1<UpdateCartDataResponse, DeleteAndRefreshCartListData>() {
                            @Override
                            public DeleteAndRefreshCartListData call(UpdateCartDataResponse updateCartDataResponse) {
                                return deleteAndRefreshCartListData;
                            }
                        });
                    }
                })
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
                    public Observable<DeleteAndRefreshCartListData> call(DeleteAndRefreshCartListData deleteAndRefreshCartListData) {
                        if (toBeDeletedPromoCode == null || toBeDeletedPromoCode.size() == 0) {
                            return Observable.just(deleteAndRefreshCartListData);
                        }
                        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), toBeDeletedPromoCode);
                        return clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                                .map(new Func1<GraphqlResponse, DeleteAndRefreshCartListData>() {
                                    @Override
                                    public DeleteAndRefreshCartListData call(GraphqlResponse graphqlResponse) {
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
