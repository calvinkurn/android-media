package com.tokopedia.loginphone.checkloginphone.data;

import com.tokopedia.loginphone.checkloginphone.domain.mapper.CheckMsisdnTokoCashMapper;
import com.tokopedia.loginphone.checkloginphone.domain.pojo.CheckMsisdnTokoCashPojo;
import com.tokopedia.loginphone.common.data.LoginRegisterPhoneApi;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashSource {

    private LoginRegisterPhoneApi loginRegisterPhoneApi;
    private CheckMsisdnTokoCashMapper checkMsisdnTokoCashMapper;

    @Inject
    public CheckMsisdnTokoCashSource(LoginRegisterPhoneApi loginRegisterPhoneApi,
                                     CheckMsisdnTokoCashMapper checkMsisdnTokoCashMapper) {
        this.loginRegisterPhoneApi = loginRegisterPhoneApi;
        this.checkMsisdnTokoCashMapper = checkMsisdnTokoCashMapper;
    }

    public Observable<CheckMsisdnTokoCashPojo> checkMsisdn(RequestParams requestParams) {
        return loginRegisterPhoneApi.checkMsisdn(requestParams.getParameters())
                .map(checkMsisdnTokoCashMapper);
    }
}
