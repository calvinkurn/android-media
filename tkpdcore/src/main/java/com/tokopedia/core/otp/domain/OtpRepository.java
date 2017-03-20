package com.tokopedia.core.otp.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public interface OtpRepository {

    Observable<RequestOtpModel> requestOtp(TKPDMapParam<String, Object> parameters);

    Observable<ValidateOtpModel> validateOtp(TKPDMapParam<String, Object> parameters);

}
