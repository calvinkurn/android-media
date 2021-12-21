package com.tokopedia.topchat.chattemplate.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public interface TemplateRepository {
    Observable<GetTemplateUiModel> setAvailabilityTemplate(JsonObject parameters, boolean isSeller);
}
