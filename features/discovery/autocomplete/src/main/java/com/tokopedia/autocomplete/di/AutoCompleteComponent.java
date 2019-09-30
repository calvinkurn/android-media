package com.tokopedia.autocomplete.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.autocomplete.di.net.AutoCompleteNetModule;
import com.tokopedia.autocomplete.fragment.SearchMainFragment;
import com.tokopedia.autocomplete.presentation.presenter.SearchPresenter;

import dagger.Component;

@AutoCompleteScope
@Component(modules = {AutoCompleteModule.class, AutoCompleteNetModule.class},
            dependencies = BaseAppComponent.class)
public interface AutoCompleteComponent {
    void inject(SearchMainFragment fragment);

    void inject(SearchPresenter presenter);
}
