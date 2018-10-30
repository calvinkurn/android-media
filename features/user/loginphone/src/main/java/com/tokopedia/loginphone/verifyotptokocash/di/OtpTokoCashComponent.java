package com.tokopedia.loginphone.verifyotptokocash.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loginphone.choosetokocashaccount.view.fragment.ChooseTokocashAccountFragment;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.TokoCashVerificationFragment;
import com.tokopedia.otp.common.di.OtpModule;
import com.tokopedia.otp.common.di.OtpScope;
import com.tokopedia.otp.cotp.data.CotpApi;
import com.tokopedia.otp.cotp.data.SQLoginApi;
import com.tokopedia.otp.cotp.di.CotpModule;
import com.tokopedia.otp.cotp.di.CotpScope;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by nisie on 10/22/18.
 */
@OtpTokoCashScope
@CotpScope
@OtpScope
@Component(modules = {OtpTokoCashModule.class, CotpModule.class,
        OtpModule.class}, dependencies = LoginRegisterPhoneComponent.class)
public interface OtpTokoCashComponent {

    void inject(TokoCashVerificationFragment fragment);

    void inject(ChooseTokocashAccountFragment fragment);

    UserSession provideUserSession();

    CotpApi provideCotpApi();

    SQLoginApi provideSQLoginApi();

    AnalyticTracker provAnalyticTracker();


}