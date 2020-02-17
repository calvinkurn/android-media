package com.tokopedia.topchat.chattemplate.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel;

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
    public Observable<GetTemplateUiModel> getTemplate(HashMap<String, Object> parameters) {
        return templateChatFactory.createCloudGetTemplateDataSource().getTemplate(parameters);
    }

    @Override
    public Observable<GetTemplateUiModel> setAvailabilityTemplate(JsonObject parameters, boolean isSeller) {
        return templateChatFactory.createCloudSetTemplateDataSource().setTemplate(parameters, isSeller);
    }
}
