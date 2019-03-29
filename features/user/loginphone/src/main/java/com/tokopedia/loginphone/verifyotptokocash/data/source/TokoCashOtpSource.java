package com.tokopedia.loginphone.verifyotptokocash.data.source;

import com.tokopedia.loginphone.common.data.LoginRegisterPhoneApi;
import com.tokopedia.loginphone.verifyotptokocash.domain.mapper.RequestOtpTokoCashMapper;
import com.tokopedia.loginphone.verifyotptokocash.domain.mapper.VerifyOtpTokoCashMapper;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/4/17.
 */

public class TokoCashOtpSource {

    private final LoginRegisterPhoneApi loginRegisterPhoneApi;
    private final RequestOtpTokoCashMapper requestOtpTokoCashMapper;
    private final VerifyOtpTokoCashMapper verifyOtpTokoCashMapper;

    @Inject
    public TokoCashOtpSource(LoginRegisterPhoneApi loginRegisterPhoneApi,
                             RequestOtpTokoCashMapper requestOtpTokoCashMapper,
                             VerifyOtpTokoCashMapper verifyOtpTokoCashMapper) {
        this.loginRegisterPhoneApi = loginRegisterPhoneApi;
        this.requestOtpTokoCashMapper = requestOtpTokoCashMapper;
        this.verifyOtpTokoCashMapper = verifyOtpTokoCashMapper;
    }

    public Observable<RequestOtpTokoCashPojo> requestLoginOtp(HashMap<String, Object> parameters) {
        return loginRegisterPhoneApi.requestLoginOtp(parameters)
                .map(requestOtpTokoCashMapper);
    }

    public Observable<VerifyOtpTokoCashPojo> verifyOtpTokoCash(HashMap<String, Object> parameters) {
        return loginRegisterPhoneApi.verifyLoginOtp(parameters)
                .map(verifyOtpTokoCashMapper);
    }
}
