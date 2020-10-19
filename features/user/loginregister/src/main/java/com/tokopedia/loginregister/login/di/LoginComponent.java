package com.tokopedia.loginregister.login.di;

import com.tokopedia.loginfingerprint.utils.crypto.Cryptography;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.login.service.RegisterPushNotifService;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment;
import com.tokopedia.loginregister.login.view.fragment.SellerSeamlessLoginFragment;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;

/**
 * @author by nisie on 10/15/18.
 */

@LoginScope
@Component(modules = {
        LoginModule.class,
        LoginQueryModule.class,
        LoginUseCaseModule.class,
        SeamlessSellerViewModelModule.class
}, dependencies = LoginRegisterComponent.class)
public interface LoginComponent {

    void inject(LoginActivity activity);

    void inject(LoginEmailPhoneFragment fragment);

    void inject(SellerSeamlessLoginFragment fragment);

    void inject(RegisterPushNotifService service);
}
