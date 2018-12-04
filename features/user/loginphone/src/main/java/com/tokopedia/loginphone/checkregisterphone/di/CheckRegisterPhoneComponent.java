package com.tokopedia.loginphone.checkregisterphone.di;

import com.tokopedia.loginphone.checkloginphone.di.CheckLoginPhoneModule;
import com.tokopedia.loginphone.checkloginphone.di.CheckLoginPhoneScope;
import com.tokopedia.loginphone.checkloginphone.view.fragment.CheckLoginPhoneNumberFragment;
import com.tokopedia.loginphone.checkloginphone.view.fragment.NotConnectedTokocashFragment;
import com.tokopedia.loginphone.checkregisterphone.view.fragment.CheckRegisterPhoneNumberFragment;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;

import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@CheckRegisterPhoneScope
@Component(modules = CheckRegisterPhoneModule.class, dependencies = LoginRegisterPhoneComponent.class)
public interface CheckRegisterPhoneComponent {

    void inject(CheckRegisterPhoneNumberFragment fragment);

}