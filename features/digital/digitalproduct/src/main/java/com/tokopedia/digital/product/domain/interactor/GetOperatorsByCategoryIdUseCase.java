package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by Rizky on 24/01/18.
 */

public class GetOperatorsByCategoryIdUseCase extends UseCase<List<Operator>> {

    private final String PARAM_CATEGORY_ID = "category_id";

    private GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase;

    public GetOperatorsByCategoryIdUseCase(GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        this.getDigitalCategoryByIdUseCase = getDigitalCategoryByIdUseCase;
    }

    @Override
    public Observable<List<Operator>> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");

        return getDigitalCategoryByIdUseCase.createObservable(getDigitalCategoryByIdUseCase.createRequestParam(categoryId))
                .map(productDigitalData -> productDigitalData.getCategoryData().getOperatorList());
    }

    public RequestParams createRequestParam(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

}
