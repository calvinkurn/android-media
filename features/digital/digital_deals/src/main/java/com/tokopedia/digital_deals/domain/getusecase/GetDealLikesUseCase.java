package com.tokopedia.digital_deals.domain.getusecase;

import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.request.likes.GetLikesDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;


public class GetDealLikesUseCase extends UseCase<GetLikesDomain> {

    DealsRepository mRepository;

    @Inject
    public GetDealLikesUseCase(DealsRepository eventRepository) {
        this.mRepository = eventRepository;
    }

    @Override
    public Observable<GetLikesDomain> createObservable(RequestParams requestParams) {

        return mRepository.getLikes(requestParams.getString("deal_id", null));
    }
}
