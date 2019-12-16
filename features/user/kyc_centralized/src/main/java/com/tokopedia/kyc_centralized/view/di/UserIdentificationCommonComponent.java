package com.tokopedia.kyc_centralized.view.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFinalFragment;

import dagger.Component;

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationCommonScope
@Component(modules = UserIdentificationCommonModule.class, dependencies = BaseAppComponent.class)
public interface UserIdentificationCommonComponent {
    void inject(UserIdentificationFormFinalFragment fragment);
}
