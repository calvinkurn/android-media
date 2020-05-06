package com.tokopedia.events.domain.postusecase;

import com.google.gson.JsonObject;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by pranaymohapatra on 07/12/17.
 */

public class VerifyCartUseCase extends UseCase<VerifyCartResponse> {

    private final EventRepository eventRepository;

    public VerifyCartUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<VerifyCartResponse> createObservable(RequestParams requestParams) {
        JsonObject requestBody = (JsonObject) requestParams.getObject(Utils.Constants.CHECKOUTDATA);
        boolean flag = requestParams.getBoolean(Utils.Constants.BOOK, false);
        return eventRepository.verifyCard(requestBody, flag);
    }
}
