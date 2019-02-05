package com.tokopedia.topchat.chattemplate.data.repository;

import com.tokopedia.topchat.chattemplate.data.factory.EditTemplateChatFactory;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;

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
    public Observable<EditTemplateViewModel> editTemplate(int index, HashMap<String, Object>
            parameters) {
        return templateChatFactory.createCloudEditTemplateDataSource().editTemplate(index, parameters);
    }

    @Override
    public Observable<EditTemplateViewModel> createTemplate(HashMap<String, Object> parameters) {
        return templateChatFactory.createCloudEditTemplateDataSource().createTemplate(parameters);
    }

    @Override
    public Observable<EditTemplateViewModel> deleteTemplate(int index) {
        return templateChatFactory.createCloudEditTemplateDataSource().deleteTemplate(index);
    }
}
