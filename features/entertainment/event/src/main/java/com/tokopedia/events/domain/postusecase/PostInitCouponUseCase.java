package com.tokopedia.events.domain.postusecase;

import com.google.gson.JsonObject;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.CouponModel;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 19/04/18.
 */

public class PostInitCouponUseCase extends UseCase<CouponModel> {
    private final EventRepository eventRepository;

    @Inject
    public PostInitCouponUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<CouponModel> createObservable(RequestParams requestParams) {
        JsonObject requestBody = (JsonObject) requestParams.getObject(Utils.Constants.CHECKOUTDATA);
        return eventRepository.postCouponInit(requestBody);
    }
}
