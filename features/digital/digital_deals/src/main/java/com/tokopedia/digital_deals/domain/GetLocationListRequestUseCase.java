package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetLocationListRequestUseCase extends UseCase<LocationDomainModel> {
    private final DealsRepository dealsRepository;

    public GetLocationListRequestUseCase(DealsRepository dealsRepository) {
        super();
        this.dealsRepository = dealsRepository;
    }


    @Override
    public Observable<LocationDomainModel> createObservable(RequestParams requestParams) {
        return dealsRepository.getLocations();
    }
}
