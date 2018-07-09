package com.tokopedia.nps.data.repository;

import com.tokopedia.nps.data.model.FeedbackEntity;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public interface FeedbackDataStore {
    Observable<FeedbackEntity> post(RequestParams params);
}
