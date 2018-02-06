package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
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
                .map(new Func1<ProductDigitalData, List<Operator>>() {
                    @Override
                    public List<Operator> call(ProductDigitalData productDigitalData) {
                        return productDigitalData.getCategoryData().getOperatorList();
                    }
                });
    }

    public RequestParams createRequestParam(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

}
