package com.tokopedia.loginphone.checkloginphone.domain.usecase;


import com.tokopedia.loginphone.checkloginphone.data.CheckMsisdnTokoCashSource;
import com.tokopedia.loginphone.checkloginphone.domain.pojo.CheckMsisdnTokoCashPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashUseCase extends UseCase<CheckMsisdnTokoCashPojo> {

    private static final String PARAM_MSISDN = "msisdn";
    private CheckMsisdnTokoCashSource checkMsisdnTokoCashSource;

    @Inject
    public CheckMsisdnTokoCashUseCase(CheckMsisdnTokoCashSource checkMsisdnTokoCashSource) {
        this.checkMsisdnTokoCashSource = checkMsisdnTokoCashSource;
    }

    @Override
    public Observable<CheckMsisdnTokoCashPojo> createObservable(RequestParams requestParams) {
        return checkMsisdnTokoCashSource.checkMsisdn(requestParams);
    }

    public static RequestParams getParam(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_MSISDN, phoneNumber);
        return params;
    }
}
