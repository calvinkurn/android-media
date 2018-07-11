package com.tokopedia.nps.data.mapper;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.nps.data.model.FeedbackEntity;
import com.tokopedia.nps.domain.Feedback;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by meta on 28/06/18.
 */
public class FeedbackMapper implements Func1<FeedbackEntity, Feedback> {

    private final String IS_SUCCESS = "1";

    public FeedbackMapper() { }

    @Override
    public Feedback call(FeedbackEntity entity) {
        Feedback item = new Feedback();
        item.setSuccess(entity.getIs_success().equals(IS_SUCCESS));
        return item;
    }
}
