package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;
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

    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    public GetProductsByOperatorIdUseCase(GetCategoryByIdUseCase getCategoryByIdUseCase) {
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    }

    @Override
    public Observable<List<Product>> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        final String operatorId = requestParams.getString(PARAM_OPERATOR_ID, "");

        return getCategoryByIdUseCase.createObservable(getCategoryByIdUseCase.createRequestParam(categoryId))
                .flatMapIterable(new Func1<ProductDigitalData, Iterable<Operator>>() {
                    @Override
                    public Iterable<Operator> call(ProductDigitalData productDigitalData) {
                        return productDigitalData.getCategoryData().getOperatorList();
                    }
                })
                .filter(new Func1<Operator, Boolean>() {
                    @Override
                    public Boolean call(Operator operator) {
                        return operator.getOperatorId().equals(operatorId);
                    }
                })
                .map(new Func1<Operator, List<Product>>() {
                    @Override
                    public List<Product> call(Operator operator) {
                        return operator.getProductList();
                    }
                });
    }

    public RequestParams createRequestParam(String categoryId, String operatorId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_OPERATOR_ID, operatorId);
        return requestParams;
    }

}
