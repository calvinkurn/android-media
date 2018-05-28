package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetCategoryDetailRequestUseCase extends UseCase<CategoryDetailsDomain> {
    private final DealsRepository dealsRepository;

    public GetCategoryDetailRequestUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    @Override
    public Observable<CategoryDetailsDomain> createObservable(RequestParams requestParams) {
        return dealsRepository.getCategoryDetails(requestParams.getParameters());
    }
}
