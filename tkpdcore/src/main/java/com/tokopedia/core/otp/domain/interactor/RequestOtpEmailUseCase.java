package com.tokopedia.core.otp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.domain.OtpRepository;

import rx.Observable;

/**
 * Created by stevenfredian on 3/15/17.
 */
public class RequestOtpEmailUseCase extends UseCase<RequestOtpModel>{

    private final OtpRepository otpRepository;

    public RequestOtpEmailUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread
                                    , OtpRepository otpRepository) {
        super(threadExecutor, postExecutionThread);
        this.otpRepository = otpRepository;
    }

    @Override
    public Observable<RequestOtpModel> createObservable(RequestParams requestParams) {
        return otpRepository.requestOtpToEmail(requestParams.getParameters());
    }
}
