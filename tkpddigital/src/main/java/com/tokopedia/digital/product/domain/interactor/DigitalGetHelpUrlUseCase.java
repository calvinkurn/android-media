package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by alvarisi on 2/20/18.
 */

public class DigitalGetHelpUrlUseCase extends UseCase<String> {
    private static final String PARAM_CATEGORY_ID = "category_id";
    private static final String PARAM_DEFAULT_EMPTY = "";
    private IDigitalCategoryRepository digitalCategoryRepository;

    public DigitalGetHelpUrlUseCase(IDigitalCategoryRepository digitalCategoryRepository) {
        this.digitalCategoryRepository = digitalCategoryRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return digitalCategoryRepository.getHelpUrl(requestParams.getString(PARAM_CATEGORY_ID, PARAM_DEFAULT_EMPTY));
    }


    public RequestParams createRequest(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

}
