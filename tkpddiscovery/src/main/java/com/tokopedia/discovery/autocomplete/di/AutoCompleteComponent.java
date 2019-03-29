package com.tokopedia.discovery.autocomplete.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.autocomplete.di.net.AutoCompleteNetModule;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import dagger.Component;

@AutoCompleteScope
@Component(modules = {AutoCompleteModule.class, AutoCompleteNetModule.class},
            dependencies = BaseAppComponent.class)
public interface AutoCompleteComponent {
    void inject(SearchMainFragment fragment);

    void inject(SearchPresenter presenter);
}
