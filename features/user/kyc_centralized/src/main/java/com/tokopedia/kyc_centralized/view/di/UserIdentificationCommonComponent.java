package com.tokopedia.kyc_centralized.view.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFaceFragment;
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFinalFragment;
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationInfoFragment;

import dagger.Component;

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationCommonScope
@Component(modules = UserIdentificationCommonModule.class, dependencies = BaseAppComponent.class)
public interface UserIdentificationCommonComponent {
    void inject(UserIdentificationFormFaceFragment fragment);
    void inject(UserIdentificationFormFinalFragment fragment);
    void inject(UserIdentificationInfoFragment fragment);
}
