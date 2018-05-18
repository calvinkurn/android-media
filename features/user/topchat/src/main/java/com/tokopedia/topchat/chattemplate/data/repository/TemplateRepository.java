package com.tokopedia.topchat.chattemplate.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface TemplateRepository {

    Observable<GetTemplateViewModel> getTemplate(TKPDMapParam<String, Object> parameters);

    Observable<GetTemplateViewModel> setAvailabilityTemplate(JsonObject parameters);
}
