package com.tokopedia.loginphone.chooseaccount.di;

import com.tokopedia.loginphone.chooseaccount.view.fragment.BaseChooseAccountFragment;
import com.tokopedia.loginphone.chooseaccount.view.fragment.ChooseAccountFragment;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;

import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@ChooseAccountScope
@Component(modules = {
        ChooseAccountModule.class,
        ChooseAccountQueryModule.class,
        ChooseAccountUseCaseModule.class,
        ChooseAccountViewModelModule.class
}, dependencies = LoginRegisterPhoneComponent.class)
public interface ChooseAccountComponent {

    void inject(ChooseAccountFragment fragment);
    void inject(BaseChooseAccountFragment fragment);
}