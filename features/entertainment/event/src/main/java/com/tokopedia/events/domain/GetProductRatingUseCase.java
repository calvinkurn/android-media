package com.tokopedia.events.domain;

import com.tokopedia.events.domain.model.ProductRatingDomain;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 30/04/18.
 */

public class GetProductRatingUseCase extends UseCase<ProductRatingDomain> {

    private final EventRepository eventRepository;

    @Inject
    public GetProductRatingUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<ProductRatingDomain> createObservable(RequestParams requestParams) {
        int id = requestParams.getInt(Utils.Constants.PRODUCTID, 0);
        return eventRepository.getProductRating(id);
    }
}
