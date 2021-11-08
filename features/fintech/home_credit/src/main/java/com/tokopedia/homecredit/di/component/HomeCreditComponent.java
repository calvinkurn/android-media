package com.tokopedia.homecredit.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.homecredit.di.module.HomeCreditModule;
import com.tokopedia.homecredit.di.scope.HomeCreditScope;

import dagger.Component;

@HomeCreditScope
@Component(modules =
        HomeCreditModule.class,
        dependencies = BaseAppComponent.class)
public interface HomeCreditComponent {

}
