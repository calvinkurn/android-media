package com.tokopedia.discovery.autocomplete.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.module.net.DiscoveryNetModule;
import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import dagger.Component;

@DiscoveryScope
@Component(modules = {AutoCompleteModule.class, DiscoveryNetModule.class},
        dependencies = BaseAppComponent.class)
public interface AutoCompleteComponent {
    void inject(SearchMainFragment fragment);

    void inject(SearchPresenter presenter);
}
