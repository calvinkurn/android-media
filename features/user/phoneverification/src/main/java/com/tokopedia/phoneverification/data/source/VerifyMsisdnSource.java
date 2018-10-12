package com.tokopedia.phoneverification.data.source;

import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.domain.mapper.VerifyPhoneNumberMapper;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyMsisdnSource {
    private AccountsService accountsService;
    private VerifyPhoneNumberMapper verifyPhoneNumberMapper;

    public VerifyMsisdnSource(AccountsService accountsService,
                              VerifyPhoneNumberMapper verifyPhoneNumberMapper) {
        this.accountsService = accountsService;
        this.verifyPhoneNumberMapper = verifyPhoneNumberMapper;
    }

    public Observable<VerifyPhoneNumberDomain> verifyPhoneNumber(HashMap<String, Object> params) {
        return accountsService.getApi()
                .verifyPhoneNumber(params).map(verifyPhoneNumberMapper);
    }

}
