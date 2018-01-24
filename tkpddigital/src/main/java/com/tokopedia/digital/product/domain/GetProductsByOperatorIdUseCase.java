package com.tokopedia.digital.product.domain;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;
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

    private Context context;
    private DigitalCategoryRepository digitalCategoryRepository;

    public GetProductsByOperatorIdUseCase(Context context,
                                          DigitalCategoryRepository digitalCategoryRepository) {
        this.digitalCategoryRepository = digitalCategoryRepository;
    }

    @Override
    public Observable<List<Product>> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        final String operatorId = requestParams.getString(PARAM_OPERATOR_ID, "");

        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();

        return digitalCategoryRepository.getCategory(categoryId, paramQueryCategory)
                .flatMapIterable(new Func1<CategoryData, Iterable<Operator>>() {
                    @Override
                    public Iterable<Operator> call(CategoryData categoryData) {
                        return categoryData.getOperatorList();
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

    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(context, originParams);
    }

}
