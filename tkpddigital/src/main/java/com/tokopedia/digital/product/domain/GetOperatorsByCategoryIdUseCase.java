package com.tokopedia.digital.product.domain;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.Operator;
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

    private DigitalCategoryRepository digitalCategoryRepository;

    public GetOperatorsByCategoryIdUseCase(DigitalCategoryRepository digitalCategoryRepository) {
        this.digitalCategoryRepository = digitalCategoryRepository;
    }

    @Override
    public Observable<List<Operator>> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");

        return digitalCategoryRepository.getCategoryFromLocal(categoryId)
                .map(new Func1<CategoryData, List<Operator>>() {
                    @Override
                    public List<Operator> call(CategoryData categoryData) {
                        return categoryData.getOperatorList();
                    }
                });
    }

    public RequestParams createRequestParam(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

}
