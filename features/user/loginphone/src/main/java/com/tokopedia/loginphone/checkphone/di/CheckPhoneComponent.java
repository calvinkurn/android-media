package com.tokopedia.loginphone.checkphone.di;

import com.tokopedia.loginphone.checkphone.view.fragment.CheckPhoneNumberFragment;
import com.tokopedia.loginphone.checkphone.view.fragment.NotConnectedTokocashFragment;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@CheckPhoneScope
@Component(modules = CheckPhoneModule.class, dependencies = LoginRegisterPhoneComponent.class)
public interface CheckPhoneComponent {

    void inject(CheckPhoneNumberFragment fragment);

    void inject(NotConnectedTokocashFragment fragment);

}