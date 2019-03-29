package com.tokopedia.nps.domain.repository;

import com.tokopedia.nps.domain.Feedback;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public interface FeedbackRepository {
    Observable<Feedback> post(RequestParams params);
}
