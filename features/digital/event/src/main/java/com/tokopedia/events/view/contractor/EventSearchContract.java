package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchContract {
    public interface IEventSearchView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);


        void showProgressBar();

        void hideProgressBar();

        RequestParams getParams();

        android.view.View getRootView();

        FragmentManager getFragmentManagerInstance();

        void setTopEvents(List<CategoryItemsViewModel> searchViewModels);

        void setSuggestions(List<CategoryItemsViewModel> suggestions, String highlight, boolean showCards);

        void removeFooter();

        void addFooter();

        void addEvents(List<CategoryItemsViewModel> searchViewModels);

        LinearLayoutManager getLayoutManager();

        void setFilterActive();

        void setFilterInactive();
    }

    public interface IEventSearchPresenter extends CustomerPresenter<IEventSearchView> {

        void setupCallback(EventsContract.AdapterCallbacks callbacks);

        void initialize();

        void onDestroy();

        void setEventLike(CategoryItemsViewModel model, int position);

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);

        boolean onOptionMenuClick(int id);

        void onSearchResultClick(SearchViewModel searchViewModel, int position);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

        String getSCREEN_NAME();

        void openFilters();

        void onActivityResult(int requestcode, int resultcode, Intent data);


    }
}
