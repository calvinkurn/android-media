package com.tokopedia.common_digital.cart.data.repository;

import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.CartDigitalInfoData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 27/08/18.
 */
public class DigitalGetCartUseCase extends UseCase<CartDigitalInfoData> {

    public static final String PARAM_CATEGORY_ID = "PARAM_CATEGORY_ID";

    private IDigitalCartRepository digitalCartRepository;

    public DigitalGetCartUseCase(IDigitalCartRepository digitalCartRepository) {
        this.digitalCartRepository = digitalCartRepository;
    }

    @Override
    public Observable<CartDigitalInfoData> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        return digitalCartRepository.getCart(categoryId);
    }

    public RequestParams createRequestParams(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

}
