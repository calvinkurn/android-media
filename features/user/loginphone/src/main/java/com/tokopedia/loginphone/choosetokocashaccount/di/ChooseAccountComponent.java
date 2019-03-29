package com.tokopedia.loginphone.choosetokocashaccount.di;

import com.tokopedia.loginphone.choosetokocashaccount.view.fragment.ChooseTokocashAccountFragment;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;

import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@ChooseAccountScope
@Component(modules = ChooseAccountModule.class, dependencies = LoginRegisterPhoneComponent.class)
public interface ChooseAccountComponent {

    void inject(ChooseTokocashAccountFragment fragment);

}