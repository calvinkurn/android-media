package com.tokopedia.phoneverification.data.source;

import com.tokopedia.phoneverification.data.PhoneVerificationApi;
import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.domain.mapper.VerifyPhoneNumberMapper;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyMsisdnSource {
    private PhoneVerificationApi phoneVerificationApi;
    private VerifyPhoneNumberMapper verifyPhoneNumberMapper;

    @Inject
    public VerifyMsisdnSource(PhoneVerificationApi phoneVerificationApi,
                              VerifyPhoneNumberMapper verifyPhoneNumberMapper) {
        this.phoneVerificationApi = phoneVerificationApi;
        this.verifyPhoneNumberMapper = verifyPhoneNumberMapper;
    }

    public Observable<VerifyPhoneNumberDomain> verifyPhoneNumber(HashMap<String, Object> params) {
        return phoneVerificationApi
                .verifyPhoneNumber(params).map(verifyPhoneNumberMapper);
    }

}
