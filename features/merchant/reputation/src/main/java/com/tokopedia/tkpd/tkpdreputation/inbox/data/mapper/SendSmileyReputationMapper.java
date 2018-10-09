package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.SendSmileyPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/31/17.
 */

public class SendSmileyReputationMapper implements Func1<Response<TkpdResponse>,
        SendSmileyReputationDomain> {
    @Override
    public SendSmileyReputationDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                SendSmileyPojo data = response.body().convertDataObj(SendSmileyPojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
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

    private SendSmileyReputationDomain mappingToDomain(SendSmileyPojo data) {
        return new SendSmileyReputationDomain(data.getIsSuccess());
    }
}
