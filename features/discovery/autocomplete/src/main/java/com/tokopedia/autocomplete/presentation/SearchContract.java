package com.tokopedia.autocomplete.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.common.model.SearchParameter;

/**
 * @author erry on 23/02/17.
 */

public interface SearchContract {
    interface View extends CustomerView {
        void showAutoCompleteResult(DefaultAutoCompleteViewModel defaultAutoCompleteViewModel, TabAutoCompleteViewModel tabAutoCompleteViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void search(SearchParameter searchParameter);

        void deleteRecentSearchItem(String keyword);

        void deleteAllRecentSearch();

        void initializeDataSearch();
    }
}