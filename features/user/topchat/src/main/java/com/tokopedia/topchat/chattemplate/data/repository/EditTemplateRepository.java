package com.tokopedia.topchat.chattemplate.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface EditTemplateRepository {
    Observable<EditTemplateViewModel> editTemplate(int index, TKPDMapParam<String, Object> object);

    Observable<EditTemplateViewModel> createTemplate(TKPDMapParam<String, Object> object);

    Observable<EditTemplateViewModel> deleteTemplate(int index);
}
