package com.tokopedia.checkout.domain.usecase;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartRequest;
import com.tokopedia.transactiondata.entity.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Irfan Khoirul on 31/10/18.
 */

public class AddToCartOneClickShipmentUseCase extends UseCase<AddToCartDataResponse> {
    public static final String PARAM_ADD_TO_CART = "PARAM_ADD_TO_CART";

    private static final String KEY_PARAM_PARAMS = "params";
    private final Gson gson;

    private ICartRepository iCartRepository;

    @Inject
    public AddToCartOneClickShipmentUseCase(ICartRepository iCartRepository, Gson gson) {
        this.iCartRepository = iCartRepository;
        this.gson = gson;
    }

    @Override
    public Observable<AddToCartDataResponse> createObservable(RequestParams requestParams) {
        AddToCartRequest addToCartRequest = (AddToCartRequest) requestParams.getObject(PARAM_ADD_TO_CART);
        TKPDMapParam<String, String> paramRequest = new TKPDMapParam<>();
        String addToCartRequestString = gson.toJson(addToCartRequest);
        paramRequest.put(KEY_PARAM_PARAMS, addToCartRequestString);
        return iCartRepository.addToCartData(paramRequest);
    }

}
