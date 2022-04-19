package com.tokopedia.chooseaccount.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.scope.ActivityScope;
import com.tokopedia.chooseaccount.view.fingerprint.ChooseAccountFingerprintFragment;
import com.tokopedia.chooseaccount.view.general.ChooseAccountFragment;

import dagger.Component;

/**
 * @author by nisie on 10/22/18.
 */
@ActivityScope
@Component(modules = {
        ChooseAccountModule.class,
        ChooseAccountViewModelModule.class
}, dependencies = { BaseAppComponent.class })
public interface ChooseAccountComponent {
    void inject(ChooseAccountFragment fragment);
    void inject(ChooseAccountFingerprintFragment fragment);
}