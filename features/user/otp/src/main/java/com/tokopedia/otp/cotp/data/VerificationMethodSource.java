package com.tokopedia.otp.cotp.data;

import com.tokopedia.otp.cotp.domain.mapper.VerificationMethodMapper;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 1/18/18.
 */

public class VerificationMethodSource {

    private CotpMethodListApi cotpApi;
    private VerificationMethodMapper verificationMethodMapper;

    @Inject
    public VerificationMethodSource(CotpMethodListApi cotpApi,
                                    VerificationMethodMapper verificationMethodMapper) {
        this.cotpApi = cotpApi;
        this.verificationMethodMapper = verificationMethodMapper;
    }

    public Observable<ListVerificationMethod> getMethodList(RequestParams requestParams) {
            return cotpApi
                    .getVerificationMethodList(requestParams.getParameters())
                    .map(verificationMethodMapper);
    }
}
