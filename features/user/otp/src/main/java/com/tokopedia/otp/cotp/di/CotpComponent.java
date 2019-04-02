package com.tokopedia.otp.cotp.di;

import com.tokopedia.otp.common.di.OtpComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;

import dagger.Component;

/**
 * @author by nisie on 4/24/18.
 */

@CotpScope
@Component(modules = {CotpModule.class}, dependencies = OtpComponent.class)
public interface CotpComponent {

    void inject(VerificationActivity activity);

    void inject(ChooseVerificationMethodFragment fragment);

    void inject(VerificationFragment fragment);

}