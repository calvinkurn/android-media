package com.tokopedia.core.otp.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.core.otp.data.factory.OtpSourceFactory;
import com.tokopedia.core.otp.domain.OtpRepository;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public class OtpRepositoryImpl implements OtpRepository {

    private final OtpSourceFactory otpSourceFactory;

    public OtpRepositoryImpl(OtpSourceFactory otpSourceFactory) {
        this.otpSourceFactory = otpSourceFactory;
    }

    @Override
    public Observable<RequestOtpModel> requestOtp(TKPDMapParam<String, Object> parameters) {
        return otpSourceFactory.createCloudOtpSource().requestOtp(parameters);
    }

    @Override
    public Observable<ValidateOtpModel> validateOtp(TKPDMapParam<String, Object> parameters) {
        return otpSourceFactory.createCloudOtpSource().validateOtp(parameters);
    }

    @Override
    public Observable<RequestOtpModel> requestOtpToEmail(TKPDMapParam<String, Object> parameters) {
        return otpSourceFactory.createCloudOtpSource().requestOtpToEmail(parameters);
    }
}
