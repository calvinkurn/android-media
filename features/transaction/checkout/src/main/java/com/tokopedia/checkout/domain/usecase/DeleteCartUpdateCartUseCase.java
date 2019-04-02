package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.DeleteUpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class DeleteCartUpdateCartUseCase extends UseCase<DeleteUpdateCartData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;

    @Inject
    public DeleteCartUpdateCartUseCase(ICartRepository cartRepository, ICartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<DeleteUpdateCartData> createObservable(RequestParams requestParams) {
        final TKPDMapParam<String, String> paramDelete = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART);

        final TKPDMapParam<String, String> paramUpdate = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        return cartRepository.deleteCartData(paramDelete)
                .map(new Func1<DeleteCartDataResponse, DeleteCartData>() {
                    @Override
                    public DeleteCartData call(DeleteCartDataResponse deleteCartDataResponse) {
                        return cartMapper.convertToDeleteCartData(deleteCartDataResponse);
                    }
                })
                .flatMap(new Func1<DeleteCartData, Observable<DeleteUpdateCartData>>() {
                    @Override
                    public Observable<DeleteUpdateCartData> call(final DeleteCartData deleteCartData) {
                        if (deleteCartData.isSuccess()) {
                            return cartRepository.updateCartData(paramUpdate)
                                    .map(new Func1<UpdateCartDataResponse, UpdateCartData>() {
                                        @Override
                                        public UpdateCartData call(UpdateCartDataResponse updateCartDataResponse) {
                                            return cartMapper.convertToUpdateCartData(updateCartDataResponse);
                                        }
                                    }).map(new Func1<UpdateCartData, DeleteUpdateCartData>() {
                                        @Override
                                        public DeleteUpdateCartData call(UpdateCartData updateCartData) {
                                            DeleteUpdateCartData deleteUpdateCartData = new DeleteUpdateCartData();
                                            if (!updateCartData.isSuccess()) {
                                                deleteUpdateCartData.setMessage(updateCartData.getMessage());
                                                deleteUpdateCartData.setSuccess(updateCartData.isSuccess());
                                            } else {
                                                deleteUpdateCartData.setMessage(deleteCartData.getMessage());
                                                deleteUpdateCartData.setSuccess(deleteCartData.isSuccess());
                                            }
                                            return deleteUpdateCartData;
                                        }
                                    });
                        } else {
                            DeleteUpdateCartData deleteUpdateCartData = new DeleteUpdateCartData();
                            deleteUpdateCartData.setMessage(deleteCartData.getMessage());
                            deleteUpdateCartData.setSuccess(deleteCartData.isSuccess());
                            return Observable.just(deleteUpdateCartData);
                        }

                    }
                });
    }
}
