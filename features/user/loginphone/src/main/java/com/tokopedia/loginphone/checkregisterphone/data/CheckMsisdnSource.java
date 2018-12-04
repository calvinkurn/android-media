package com.tokopedia.loginphone.checkregisterphone.data;

import com.tokopedia.loginphone.checkregisterphone.domain.mapper.CheckRegisterMsisdnMapper;
import com.tokopedia.loginphone.checkregisterphone.domain.pojo.CheckRegisterPhoneNumberPojo;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnSource {
    private final CheckMsisdnApi checkMsisdnApi;
    private final CheckRegisterMsisdnMapper checkMsisdnMapper;

    @Inject
    public CheckMsisdnSource(CheckMsisdnApi checkMsisdnApi,
                             CheckRegisterMsisdnMapper checkMsisdnMapper) {
        this.checkMsisdnApi = checkMsisdnApi;
        this.checkMsisdnMapper = checkMsisdnMapper;
    }

    public Observable<CheckRegisterPhoneNumberPojo> changePhoneNumber(HashMap<String, Object> parameters) {
        return checkMsisdnApi
                .checkMsisdnRegisterPhoneNumber(parameters)
                .map(checkMsisdnMapper);
    }
}