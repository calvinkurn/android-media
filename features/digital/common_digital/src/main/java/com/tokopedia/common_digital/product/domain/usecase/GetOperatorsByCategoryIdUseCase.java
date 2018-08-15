package com.tokopedia.common_digital.product.domain.usecase;

import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.ProductDigitalData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 24/01/18.
 */

public class GetOperatorsByCategoryIdUseCase extends UseCase<List<Operator>> {

    private final String PARAM_CATEGORY_ID = "category_id";

    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    public GetOperatorsByCategoryIdUseCase(GetCategoryByIdUseCase getCategoryByIdUseCase) {
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    }

    @Override
    public Observable<List<Operator>> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");

        return getCategoryByIdUseCase.createObservable(getCategoryByIdUseCase.createRequestParam(categoryId))
                .map(productDigitalData -> productDigitalData.getCategoryData().getOperatorList());
    }

    public RequestParams createRequestParam(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

}
