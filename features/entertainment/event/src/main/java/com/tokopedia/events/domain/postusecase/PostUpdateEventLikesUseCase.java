package com.tokopedia.events.domain.postusecase;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.model.request.likes.LikeUpdateModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 04/04/18.
 */

public class PostUpdateEventLikesUseCase extends UseCase<LikeUpdateResultDomain> {

    EventRepository mRepository;

    @Inject
    public PostUpdateEventLikesUseCase(EventRepository eventRepository) {
        this.mRepository = eventRepository;
    }

    @Override
    public Observable<LikeUpdateResultDomain> createObservable(RequestParams requestParams) {
        LikeUpdateModel requestModel = (LikeUpdateModel) requestParams.getObject("request_body");
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestModel));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return mRepository.updateLikes(requestBody);
    }
}
