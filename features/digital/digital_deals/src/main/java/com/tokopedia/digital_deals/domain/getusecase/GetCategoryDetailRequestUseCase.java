package com.tokopedia.digital_deals.domain.getusecase;

import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import rx.Observable;

public class GetCategoryDetailRequestUseCase extends UseCase<CategoryDetailsDomain> {
    private final DealsRepository dealsRepository;

    public GetCategoryDetailRequestUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    @Override
    public Observable<CategoryDetailsDomain> createObservable(RequestParams requestParams) {
        HashMap<String, Object> params=requestParams.getParameters();
        String url=String.valueOf(params.get(DealsHomePresenter.TAG));
        params.remove(DealsHomePresenter.TAG);
        return dealsRepository.getCategoryDetails(url, requestParams.getParameters());
    }
}
