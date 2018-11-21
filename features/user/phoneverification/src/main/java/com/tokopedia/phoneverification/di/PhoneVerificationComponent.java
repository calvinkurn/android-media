package com.tokopedia.phoneverification.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.phoneverification.view.fragment.ReferralPhoneNumberVerificationFragment;

import dagger.Component;

/**
 * @author by alvinatin on 12/10/18.
 */

@PhoneVerificationScope
@Component(modules = PhoneVerificationModule.class, dependencies = BaseAppComponent.class)
public interface PhoneVerificationComponent {
    void inject(PhoneVerificationActivationActivity activity);

    void inject(PhoneVerificationFragment fragment);

    void inject(ChangePhoneNumberFragment fragment);

    void inject(ReferralPhoneNumberVerificationFragment fragment);
}
