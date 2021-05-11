package com.tokopedia.manageaddress.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {FakeAppModule.class})
public interface TestAppComponent extends BaseAppComponent {

}