package com.tokopedia.topchat.chattemplate.domain.usecase;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class SetAvailabilityTemplateUseCase extends UseCase<GetTemplateViewModel> {

    private final TemplateRepository templateRepository;

    @Inject
    public SetAvailabilityTemplateUseCase(TemplateRepository templateRepository) {
        super();
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<GetTemplateViewModel> createObservable(RequestParams requestParams) {
        JsonObject object = (JsonObject) requestParams.getParameters().get("json");
        return templateRepository.setAvailabilityTemplate(object);
    }

    public static RequestParams generateParam(JsonArray list, boolean isEnabled) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        if(list!=null) {
            object.add("position", list);
        }
        object.addProperty("is_enable", isEnabled);
        requestParams.putObject("json", object);
        return requestParams;
    }
}
