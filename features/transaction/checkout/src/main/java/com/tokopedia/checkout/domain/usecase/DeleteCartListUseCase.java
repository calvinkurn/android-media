package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.DeleteAndRefreshCartListData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

//import static com.tokopedia.checkout.domain.usecase.UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class DeleteCartListUseCase extends UseCase<DeleteAndRefreshCartListData> {
    public static final String PARAM_IS_DELETE_ALL_DATA
            = "PARAM_IS_DELETE_ALL_DATA";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART";
    public static final String PARAM_TO_BE_REMOVED_PROMO_CODES
            = "PARAM_TO_BE_REMOVED_PROMO_CODES";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase;

    @Inject
    public DeleteCartListUseCase(ICartRepository cartRepository, ICartMapper cartMapper,
                                 ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.clearCacheAutoApplyStackUseCase = clearCacheAutoApplyStackUseCase;
    }

    @Override
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
                });
    }
}
