package com.tokopedia.core.drawer2.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationMapper implements Func1<Response<TkpdResponse>, NotificationModel> {

    @Inject
    public NotificationMapper() {
    }

    @Override
    public NotificationModel call(Response<TkpdResponse> response) {
        NotificationModel model = new NotificationModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                NotificationData data = response.body().convertDataObj(NotificationData.class);
                model.setSuccess(true);
                model.setNotificationData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
            model.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
