package com.tokopedia.otp.cotp.domain.source;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.otp.cotp.data.SQLoginApi;
import com.tokopedia.otp.cotp.di.CotpScope;
import com.tokopedia.otp.cotp.domain.mapper.MakeLoginMapper;
import com.tokopedia.otp.cotp.view.viewmodel.OtpLoginDomain;
import com.tokopedia.user.session.UserSession;

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
                    userSession.setIsLogin(makeLoginDomain.isLogin());
                    userSession.setUserId(String.valueOf(makeLoginDomain.getUserId()));
                    userSession.setName(makeLoginDomain.getFullName());
                    userSession.setEmail(userSession.getTempEmail());
                    userSession.setIsMsisdnVerified(makeLoginDomain.isMsisdnVerified());
                    userSession.setPhoneNumber(userSession.getTempPhoneNumber());

                    userSession.setShopId(String.valueOf(makeLoginDomain.getShopId()));
                    userSession.setShopName(makeLoginDomain.getShopName());
                    userSession.setIsGoldMerchant(makeLoginDomain.getShopIsGold());

                } else {
                    userSession.setTempLoginName(makeLoginDomain.getFullName());
                    userSession.setTempUserId(String.valueOf(makeLoginDomain.getUserId()));
                }
            }
        };
    }
}
