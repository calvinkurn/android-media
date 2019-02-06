package com.tokopedia.topchat.chattemplate.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface TemplateRepository {

    Observable<GetTemplateViewModel> getTemplate(HashMap<String, Object> parameters);

    Observable<GetTemplateViewModel> setAvailabilityTemplate(JsonObject parameters);
}
