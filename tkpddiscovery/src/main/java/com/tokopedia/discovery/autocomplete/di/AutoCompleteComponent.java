package com.tokopedia.discovery.autocomplete.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import dagger.Component;

@AutoCompleteScope
@Component(modules = AutoCompleteModule.class, dependencies = AppComponent.class)
public interface AutoCompleteComponent {

    void inject(SearchMainFragment fragment);

    void inject(SearchPresenter presenter);
}
