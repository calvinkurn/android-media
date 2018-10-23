package com.tokopedia.core.drawer2.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.drawer2.data.pojo.TopchatNotificationPojo;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 11/17/17.
 */

public class TopChatNotificationMapper implements Func1<Response<TkpdResponse>, TopChatNotificationModel> {
    @Override
    public TopChatNotificationModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                TopchatNotificationPojo data = response.body().convertDataObj(TopchatNotificationPojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private TopChatNotificationModel mappingToDomain(TopchatNotificationPojo data) {
        return new TopChatNotificationModel(data.getNotifUnreads());
    }
}
