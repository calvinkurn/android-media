package com.tokopedia.events.domain.postusecase;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.request.verify.ValidateShow;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 07/12/17.
 */

public class PostValidateShowUseCase extends UseCase<ValidateResponse> {

    ValidateShow validateShowModel;
    private final EventRepository eventRepository;

    @Inject
    public PostValidateShowUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<ValidateResponse> createObservable(RequestParams requestParams) {

        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(validateShowModel));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return eventRepository.validateShow(requestBody) ;
    }

    public void setValidateShowModel(ValidateShow model){
        this.validateShowModel = model;
    }
}
