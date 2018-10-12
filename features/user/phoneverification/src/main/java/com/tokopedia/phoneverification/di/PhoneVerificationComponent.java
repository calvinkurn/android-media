package com.tokopedia.phoneverification.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

import dagger.Component;

/**
 * @author by alvinatin on 12/10/18.
 */

@PhoneVerificationScope
@Component(modules = PhoneVerificationModule.class, dependencies = BaseAppComponent.class)
public class PhoneVerificationComponent {
}
