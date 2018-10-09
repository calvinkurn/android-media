package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 24/01/18.
 */

public class GetProductsByOperatorIdUseCase extends UseCase<List<Product>> {

    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_OPERATOR_ID = "operator_id";

    private GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase;

    public GetProductsByOperatorIdUseCase(GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        this.getDigitalCategoryByIdUseCase = getDigitalCategoryByIdUseCase;
    }

    @Override
    public Observable<List<Product>> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        final String operatorId = requestParams.getString(PARAM_OPERATOR_ID, "");

        return getDigitalCategoryByIdUseCase.createObservable(getDigitalCategoryByIdUseCase.createRequestParam(categoryId))
                .flatMapIterable((Func1<ProductDigitalData, Iterable<Operator>>) productDigitalData -> productDigitalData.getCategoryData().getOperatorList())
                .filter(operator -> operator.getOperatorId().equals(operatorId))
                .map(Operator::getProductList);
    }

    public RequestParams createRequestParam(String categoryId, String operatorId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_OPERATOR_ID, operatorId);
        return requestParams;
    }

}
