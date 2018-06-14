package com.tokopedia.digital_deals.domain.postusecase;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.LikeUpdateResultDomain;
import com.tokopedia.digital_deals.domain.model.request.likes.LikeUpdateModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;


public class PostUpdateDealLikesUseCase extends UseCase<LikeUpdateResultDomain> {

    DealsRepository mRepository;

    @Inject
    public PostUpdateDealLikesUseCase(DealsRepository eventRepository) {
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
