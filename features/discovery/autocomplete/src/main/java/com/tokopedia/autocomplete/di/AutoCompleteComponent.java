package com.tokopedia.autocomplete.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.autocomplete.di.net.AutoCompleteNetModule;
import com.tokopedia.autocomplete.initialstate.InitialStateFragment;
import com.tokopedia.autocomplete.initialstate.InitialStatePresenter;
import com.tokopedia.autocomplete.suggestion.SuggestionFragment;
import com.tokopedia.autocomplete.suggestion.SuggestionPresenter;

import dagger.Component;

@AutoCompleteScope
@Component(modules = {AutoCompleteModule.class, AutoCompleteNetModule.class}, dependencies = BaseAppComponent.class)
public interface AutoCompleteComponent {
    void inject(InitialStateFragment fragment);

    void inject(SuggestionFragment fragment);

    void inject(InitialStatePresenter initialStatePresenter);

    void inject(SuggestionPresenter suggestionPresenter);
}
