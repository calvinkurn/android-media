package com.tokopedia.otp.cotp.domain.source;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.otp.cotp.data.SQLoginApi;
import com.tokopedia.otp.cotp.di.CotpScope;
import com.tokopedia.otp.cotp.domain.mapper.MakeLoginMapper;
import com.tokopedia.otp.cotp.view.viewmodel.OtpLoginDomain;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/18/17.
 */

public class MakeLoginDataSource {

    private final SQLoginApi otpLoginApi;
    private final MakeLoginMapper makeLoginMapper;

    @Inject
    UserSession userSession;

    @Inject
    public MakeLoginDataSource(@CotpScope SQLoginApi otpLoginApi,
                               MakeLoginMapper makeLoginMapper) {
        this.otpLoginApi = otpLoginApi;
        this.makeLoginMapper = makeLoginMapper;
    }

    public Observable<OtpLoginDomain> makeLogin(Map<String, Object> parameters) {
        return otpLoginApi
                .makeLogin(parameters)
                .map(makeLoginMapper)
                .doOnNext(saveToCache());
    }


    private Action1<OtpLoginDomain> saveToCache() {
        return new Action1<OtpLoginDomain>() {
            @Override
            public void call(OtpLoginDomain makeLoginDomain) {
                if (makeLoginDomain.isLogin()) {
                    userSession.setLoginSession(makeLoginDomain.isLogin(),
                            String.valueOf(makeLoginDomain.getUserId()),
                            makeLoginDomain.getFullName(),
                            String.valueOf(makeLoginDomain.getShopId()),
                            makeLoginDomain.isMsisdnVerified(),
                            makeLoginDomain.getShopName());
                    userSession.setEmail(userSession.getTempEmail());
                    userSession.setGoldMerchant(makeLoginDomain.getShopIsGold());
                    userSession.setPhoneNumber(userSession.getTempPhoneNumber());
                } else {
                    userSession.setTempLoginName(makeLoginDomain.getFullName());
                    userSession.setTempLoginSession(String.valueOf(makeLoginDomain.getUserId()));
                }
            }
        };
    }
}
