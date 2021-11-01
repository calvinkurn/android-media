package com.tokopedia.chooseaccount.di;

import com.tokopedia.chooseaccount.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.chooseaccount.view.fingerprint.ChooseAccountFingerprintFragment;
import com.tokopedia.chooseaccount.view.general.ChooseAccountFragment;

import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@ChooseAccountScope
@Component(modules = {
        ChooseAccountModule.class,
        ChooseAccountViewModelModule.class
}, dependencies = LoginRegisterPhoneComponent.class)
public interface ChooseAccountComponent {

    void inject(ChooseAccountFragment fragment);
    void inject(ChooseAccountFingerprintFragment fragment);
}