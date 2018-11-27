package com.tokopedia.phoneverification.data.source;

import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.phoneverification.data.PhoneVerificationApi;
import com.tokopedia.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.phoneverification.domain.mapper.ChangePhoneNumberMapper;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangeMsisdnSource {
    private final PhoneVerificationApi phoneVerificationApi;
    private final ChangePhoneNumberMapper changePhoneNumberMapper;
    private UserSession userSession;

    @Inject
    public ChangeMsisdnSource(PhoneVerificationApi phoneVerificationApi,
                              ChangePhoneNumberMapper changePhoneNumberMapper,
                              UserSession userSession) {
        this.phoneVerificationApi = phoneVerificationApi;
        this.changePhoneNumberMapper = changePhoneNumberMapper;
        this.userSession = userSession;
    }


    public Observable<ChangePhoneNumberViewModel> changePhoneNumber(HashMap<String, Object> parameters) {
        TKPDMapParam<String, Object> map = new TKPDMapParam<>();

        return phoneVerificationApi
                .changePhoneNumber(AuthUtil.generateParamsNetwork(userSession.getUserId(),
                        userSession.getDeviceId(), generateRequestParam(parameters)))
                .map(changePhoneNumberMapper);
    }

    private TKPDMapParam<String, String> generateRequestParam(HashMap<String, Object> param) {
        TKPDMapParam<String, String> result = new TKPDMapParam<>();
        for (String key: param.keySet()){
            result.put(key, param.get(key).toString());
        }
        return result;
    }
}
