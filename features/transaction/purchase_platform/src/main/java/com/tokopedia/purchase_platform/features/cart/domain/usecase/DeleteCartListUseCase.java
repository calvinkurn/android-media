package com.tokopedia.purchase_platform.features.cart.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers;
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.domain.model.DeleteAndRefreshCartListData;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class DeleteCartListUseCase {
    public static final String PARAM_IS_DELETE_ALL_DATA
            = "PARAM_IS_DELETE_ALL_DATA";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART";
    public static final String PARAM_TO_BE_REMOVED_PROMO_CODES
            = "PARAM_TO_BE_REMOVED_PROMO_CODES";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;
    private final ExecutorSchedulers schedulers;

    @Inject
    public DeleteCartListUseCase(ICartRepository cartRepository, ICartMapper cartMapper,
                                 ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase,
                                 ExecutorSchedulers schedulers) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.clearCacheAutoApplyStackUseCase = clearCacheAutoApplyStackUseCase;
        this.schedulers = schedulers;
    }

    @SuppressWarnings("unchecked")
    public Observable<DeleteAndRefreshCartListData> createObservable(RequestParams requestParams) {

        final TKPDMapParam<String, String> paramDelete = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART);

        final ArrayList<String> toBeDeletedPromoCode = (ArrayList<String>) requestParams.getObject(PARAM_TO_BE_REMOVED_PROMO_CODES);

        return Observable.just(new DeleteAndRefreshCartListData())
                .flatMap((Func1<DeleteAndRefreshCartListData, Observable<DeleteAndRefreshCartListData>>) deleteAndRefreshCartListData -> cartRepository.deleteCartData(paramDelete).map(
                        deleteCartDataResponse -> {
                            deleteAndRefreshCartListData.setDeleteCartData(
                                    cartMapper.convertToDeleteCartData(deleteCartDataResponse)
                            );
                            return deleteAndRefreshCartListData;
                        }))
                .flatMap((Func1<DeleteAndRefreshCartListData, Observable<DeleteAndRefreshCartListData>>) deleteAndRefreshCartListData -> {
                    if (toBeDeletedPromoCode == null || toBeDeletedPromoCode.size() == 0) {
                        return Observable.just(deleteAndRefreshCartListData);
                    }
                    clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.Companion.getPARAM_VALUE_MARKETPLACE(), toBeDeletedPromoCode);
                    return clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                            .map(graphqlResponse -> deleteAndRefreshCartListData);
                })
                .subscribeOn(schedulers.getIo())
                .observeOn(schedulers.getMain());
    }
}
