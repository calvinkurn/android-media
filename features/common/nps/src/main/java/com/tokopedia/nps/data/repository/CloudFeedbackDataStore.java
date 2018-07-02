package com.tokopedia.nps.data.repository;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.nps.data.mapper.FeedbackEntityMapper;
import com.tokopedia.nps.data.model.FeedbackEntity;
import com.tokopedia.nps.data.net.NpsService;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public class CloudFeedbackDataStore implements FeedbackDataStore {

    private NpsService service;
    private FeedbackEntityMapper mapper;

    public CloudFeedbackDataStore(NpsService service, FeedbackEntityMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public Observable<FeedbackEntity> post(HashMap<String, String> params) {
        return service.getApi().postFeedback(
                params.get("rating"),
                params.get("category"),
                params.get("user_id"),
                params.get("comment"),
                params.get("app_version"),
                params.get("device_model"),
                params.get("os_type"),
                params.get("os_version")
        ).map(mapper);
    }
}
