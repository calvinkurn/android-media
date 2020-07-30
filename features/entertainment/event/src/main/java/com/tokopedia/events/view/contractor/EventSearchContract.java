package com.tokopedia.events.view.contractor;

import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchContract {
    public interface EventSearchView extends EventBaseContract.EventBaseView {
        FragmentManager getFragmentManagerInstance();

        void setTopEvents(List<CategoryItemsViewModel> searchViewModels);

        void setSuggestions(List<CategoryItemsViewModel> suggestions, String highlight, boolean showCards, boolean shouldFireEvent);

        void removeFooter();

        void addFooter();

        void addEvents(List<CategoryItemsViewModel> searchViewModels);

        LinearLayoutManager getLayoutManager();

        void setFilterActive();

        void setFilterInactive();
    }

    public interface EventSearchPresenter extends EventBaseContract.EventBasePresenter {

        void setupCallback(EventsContract.AdapterCallbacks callbacks);

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
