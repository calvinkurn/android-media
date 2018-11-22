package com.tokopedia.useridentification.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.useridentification.view.fragment.UserIdentificationFormFinalFragment;
import com.tokopedia.useridentification.view.fragment.UserIdentificationInfoFragment;

import dagger.Component;

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationScope
@Component(modules = UserIdentificationModule.class, dependencies = BaseAppComponent.class)
public interface UserIdentificationComponent  {

    void inject(UserIdentificationInfoFragment fragment);

    void inject(UserIdentificationFormFinalFragment fragment);
}
