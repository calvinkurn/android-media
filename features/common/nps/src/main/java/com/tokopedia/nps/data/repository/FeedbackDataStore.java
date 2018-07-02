package com.tokopedia.nps.data.repository;

import com.tokopedia.nps.data.model.FeedbackEntity;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public interface FeedbackDataStore {
    Observable<FeedbackEntity> post(HashMap<String, String> params);
}
