package com.tokopedia.topchat.chattemplate.data.repository;

import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface EditTemplateRepository {
    Observable<EditTemplateUiModel> editTemplate(int index, HashMap<String, Object> object, boolean isSeller);

    Observable<EditTemplateUiModel> createTemplate(HashMap<String, Object> object);

    Observable<EditTemplateUiModel> deleteTemplate(int index, boolean isSeller);
}
