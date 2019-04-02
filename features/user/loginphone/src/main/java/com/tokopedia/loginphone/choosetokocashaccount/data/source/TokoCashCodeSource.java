package com.tokopedia.loginphone.choosetokocashaccount.data.source;

import com.tokopedia.loginphone.choosetokocashaccount.data.GetCodeTokoCashPojo;
import com.tokopedia.loginphone.choosetokocashaccount.domain.mapper.GetCodeTokoCashMapper;
import com.tokopedia.loginphone.common.data.LoginRegisterPhoneApi;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/5/17.
 */

public class TokoCashCodeSource {

    private final LoginRegisterPhoneApi loginRegisterPhoneApi;
    private final GetCodeTokoCashMapper getCodeTokoCashMapper;

    @Inject
    public TokoCashCodeSource(LoginRegisterPhoneApi loginRegisterPhoneApi,
                              GetCodeTokoCashMapper getCodeTokoCashMapper) {
        this.loginRegisterPhoneApi = loginRegisterPhoneApi;
        this.getCodeTokoCashMapper = getCodeTokoCashMapper;
    }

    public Observable<GetCodeTokoCashPojo> getAccessToken(Map<String, Object> parameters) {
        return loginRegisterPhoneApi.getAuthorizeCode(parameters)
                .map(getCodeTokoCashMapper);
    }
}
