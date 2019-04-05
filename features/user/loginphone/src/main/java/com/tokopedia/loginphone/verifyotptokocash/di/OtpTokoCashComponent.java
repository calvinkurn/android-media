package com.tokopedia.loginphone.verifyotptokocash.di;

import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.ChooseTokocashVerificationMethodFragment;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.TokoCashVerificationFragment;
import com.tokopedia.otp.common.di.OtpModule;
import com.tokopedia.otp.common.di.OtpNetModule;
import com.tokopedia.otp.common.di.OtpScope;
import com.tokopedia.otp.cotp.data.CotpApi;
import com.tokopedia.otp.cotp.data.SQLoginApi;
import com.tokopedia.otp.cotp.di.CotpModule;
import com.tokopedia.otp.cotp.di.CotpScope;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@OtpTokoCashScope
@CotpScope
@OtpScope
@Component(modules = {OtpTokoCashModule.class, CotpModule.class,
        OtpNetModule.class}, dependencies = LoginRegisterPhoneComponent.class)
public interface OtpTokoCashComponent {

    void inject(TokoCashVerificationFragment fragment);

    void inject(ChooseTokocashVerificationMethodFragment fragment);

    UserSessionInterface provideUserSession();

    CotpApi provideCotpApi();

    SQLoginApi provideSQLoginApi();



}