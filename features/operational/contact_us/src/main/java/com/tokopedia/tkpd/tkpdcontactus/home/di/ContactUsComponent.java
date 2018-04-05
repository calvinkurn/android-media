package com.tokopedia.tkpd.tkpdcontactus.home.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.tkpdcontactus.home.di.scope.ContactUsModuleScope;
import com.tokopedia.tkpd.tkpdcontactus.home.view.fragment.ContactUsHomeFragment;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@ContactUsModuleScope
@Component(modules = ContactUsModule.class, dependencies = BaseAppComponent.class)
public interface ContactUsComponent {


    void inject(ContactUsHomeFragment contactUsHomeFragment);
}