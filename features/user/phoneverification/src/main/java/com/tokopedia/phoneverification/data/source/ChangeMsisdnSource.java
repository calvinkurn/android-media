package com.tokopedia.phoneverification.data.source;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.phoneverification.domain.mapper.ChangePhoneNumberMapper;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangeMsisdnSource {
    private final AccountsService accountsService;
    private final ChangePhoneNumberMapper changePhoneNumberMapper;

    public ChangeMsisdnSource(AccountsService accountsService,
                              ChangePhoneNumberMapper changePhoneNumberMapper) {
        this.accountsService = accountsService;
        this.changePhoneNumberMapper = changePhoneNumberMapper;
    }


    public Observable<ChangePhoneNumberViewModel> changePhoneNumber(HashMap<String, Object> parameters) {
        return accountsService.getApi()
                .changePhoneNumber(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(), parameters))
                .map(changePhoneNumberMapper);
    }
}
