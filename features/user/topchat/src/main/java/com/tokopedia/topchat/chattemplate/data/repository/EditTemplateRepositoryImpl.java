package com.tokopedia.topchat.chattemplate.data.repository;

import com.tokopedia.topchat.chattemplate.data.factory.EditTemplateChatFactory;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateRepositoryImpl implements EditTemplateRepository {

    private EditTemplateChatFactory templateChatFactory;

    public EditTemplateRepositoryImpl(EditTemplateChatFactory templateChatFactory) {
        this.templateChatFactory = templateChatFactory;
    }

    @Override
    public Observable<EditTemplateUiModel> editTemplate(int index, HashMap<String, Object>
            parameters, boolean isSeller) {
        return templateChatFactory.createCloudEditTemplateDataSource().editTemplate(index, parameters, isSeller);
    }

    @Override
    public Observable<EditTemplateUiModel> createTemplate(HashMap<String, Object> parameters) {
        return templateChatFactory.createCloudEditTemplateDataSource().createTemplate(parameters);
    }

    @Override
    public Observable<EditTemplateUiModel> deleteTemplate(int index, boolean isSeller) {
        return templateChatFactory.createCloudEditTemplateDataSource().deleteTemplate(index, isSeller);
    }
}
