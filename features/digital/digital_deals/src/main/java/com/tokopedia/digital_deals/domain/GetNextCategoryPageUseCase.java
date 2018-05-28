package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.view.presenter.DealsCategoryDetailPresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetNextCategoryPageUseCase extends UseCase<CategoryDetailsDomain> {
    private final DealsRepository dealsRepository;

    public GetNextCategoryPageUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    @Override
    public Observable<CategoryDetailsDomain> createObservable(RequestParams requestParams) {
        String nextUrl = requestParams.getString(DealsCategoryDetailPresenter.TAG, "");
        return dealsRepository.getCategoryDetails(nextUrl);
    }
}
