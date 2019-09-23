package com.tokopedia.autocomplete.presentation.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.autocomplete.presentation.SearchContract;
import com.tokopedia.autocomplete.subscriber.SearchSubscriber;
import com.tokopedia.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.autocomplete.usecase.DeleteRecentSearchUseCase;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * @author erry on 23/02/17.
 */

public class SearchPresenter extends BaseDaggerPresenter<SearchContract.View>
        implements SearchContract.Presenter {

    private static final String TAG = SearchPresenter.class.getSimpleName();
    private final Context context;
    private String querySearch = "";

    @Inject
    AutoCompleteUseCase autoCompleteUseCase;

    @Inject
    DeleteRecentSearchUseCase deleteRecentSearchUseCase;

    @Inject
    UserSessionInterface userSession;

    public SearchPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void search(SearchParameter searchParameter) {
        this.querySearch = searchParameter.getSearchQuery();
        autoCompleteUseCase.execute(
                AutoCompleteUseCase.getParams(
                        searchParameter.getSearchParameterMap(),
                        userSession.getDeviceId(),
                        userSession.getUserId()
                ),
                new SearchSubscriber(querySearch,
                        new DefaultAutoCompleteViewModel(),
                        new TabAutoCompleteViewModel(),
                        getView())
        );
    }

    @Override
    public void deleteRecentSearchItem(String keyword) {
        RequestParams params = DeleteRecentSearchUseCase.getParams(
                keyword,
                userSession.getDeviceId(),
                userSession.getUserId()
        );
        deleteRecentSearchUseCase.execute(
                params,
                new SearchSubscriber(querySearch,
                        new DefaultAutoCompleteViewModel(),
                        new TabAutoCompleteViewModel(),
                        getView())
        );
    }

    @Override
    public void deleteAllRecentSearch() {
        RequestParams params = DeleteRecentSearchUseCase.getParams(
                userSession.getDeviceId(),
                userSession.getUserId()
        );
        deleteRecentSearchUseCase.execute(
                params,
                new SearchSubscriber("",
                        new DefaultAutoCompleteViewModel(),
                        new TabAutoCompleteViewModel(),
                        getView())
        );
    }

    @Override
    public void initializeDataSearch() {
        checkViewAttached();
    }

    @Override
    public void detachView() {
        super.detachView();
        autoCompleteUseCase.unsubscribe();
    }
}
