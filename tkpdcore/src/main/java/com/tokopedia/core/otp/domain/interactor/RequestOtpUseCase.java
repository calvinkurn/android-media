package com.tokopedia.core.otp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.domain.OtpRepository;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 * Don't use this, use RequestOtpUseCase in tkpdsession instead.
 */
@Deprecated
public class RequestOtpUseCase extends UseCase<RequestOtpModel> {

    public static final String PARAM_MODE = "mode";
    public static final String PARAM_OTP_TYPE = "otp_type";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_MSISDN = "msisdn";

    public static final String MODE_SMS = "sms";
    public static final String MODE_CALL = "call";

    private final OtpRepository otpRepository;

    public RequestOtpUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             OtpRepository otpRepository) {
        super(threadExecutor, postExecutionThread);
        this.otpRepository = otpRepository;
    }

    @Override
    public Observable<RequestOtpModel> createObservable(RequestParams requestParams) {
        return otpRepository.requestOtp(requestParams.getParameters());
    }
}
