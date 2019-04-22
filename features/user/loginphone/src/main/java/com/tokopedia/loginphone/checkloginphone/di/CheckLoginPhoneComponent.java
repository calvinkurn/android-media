package com.tokopedia.loginphone.checkloginphone.di;

import com.tokopedia.loginphone.checkloginphone.view.fragment.NotConnectedTokocashFragment;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@CheckLoginPhoneScope
@Component(modules = CheckLoginPhoneModule.class, dependencies = LoginRegisterPhoneComponent.class)
public interface CheckLoginPhoneComponent {

    void inject(NotConnectedTokocashFragment fragment);

}