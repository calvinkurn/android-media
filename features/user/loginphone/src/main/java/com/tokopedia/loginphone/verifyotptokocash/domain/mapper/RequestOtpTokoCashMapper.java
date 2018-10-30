package com.tokopedia.loginphone.verifyotptokocash.domain.mapper;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.common.network.TokoCashErrorException;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpTokoCashMapper implements Func1<Response<DataResponse<RequestOtpTokoCashPojo>>,
        RequestOtpTokoCashPojo> {

    private final Context context;

    @Inject
    public RequestOtpTokoCashMapper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public RequestOtpTokoCashPojo call(Response<DataResponse<RequestOtpTokoCashPojo>> response) {
        int REQUEST_OTP_LIMIT_REACHED = 412;

        if (response.isSuccessful()) {
            return response.body().getData();
        } else if (response.code() == REQUEST_OTP_LIMIT_REACHED) {
            throw new TokoCashErrorException(context.getString(R.string
                    .limit_otp_reached));
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
