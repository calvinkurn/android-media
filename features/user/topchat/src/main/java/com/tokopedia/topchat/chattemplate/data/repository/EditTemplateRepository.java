package com.tokopedia.topchat.chattemplate.data.repository;

import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface EditTemplateRepository {
    Observable<EditTemplateViewModel> editTemplate(int index, HashMap<String, Object> object);

    Observable<EditTemplateViewModel> createTemplate(HashMap<String, Object> object);

    Observable<EditTemplateViewModel> deleteTemplate(int index);
}
