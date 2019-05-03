package com.tokopedia.search.result.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.SearchTrackingModule;
import com.tokopedia.search.di.module.UserSessionModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        UserSessionModule.class,
        SearchTrackingModule.class
}, dependencies = BaseAppComponent.class)
public interface ProductListSectionComponent {

}
