package com.tokopedia.topchat.chattemplate.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class TemplateRepositoryImpl implements TemplateRepository {

    private TemplateChatFactory templateChatFactory;

    public TemplateRepositoryImpl(TemplateChatFactory templateChatFactory) {
        this.templateChatFactory = templateChatFactory;
    }

    @Override
    public Observable<GetTemplateViewModel> getTemplate(HashMap<String, Object> parameters) {
        return templateChatFactory.createCloudGetTemplateDataSource().getTemplate(parameters);
    }

    @Override
    public Observable<GetTemplateViewModel> setAvailabilityTemplate(JsonObject parameters) {
        return templateChatFactory.createCloudSetTemplateDataSource().setTemplate(parameters);
    }
}
