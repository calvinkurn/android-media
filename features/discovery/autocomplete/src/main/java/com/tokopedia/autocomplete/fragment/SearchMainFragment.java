package com.tokopedia.autocomplete.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.autocomplete.HostAutoCompleteAdapter;
import com.tokopedia.autocomplete.HostAutoCompleteFactory;
import com.tokopedia.autocomplete.HostAutoCompleteTypeFactory;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.autocomplete.adapter.ItemClickListener;
import com.tokopedia.autocomplete.analytics.AppScreen;
import com.tokopedia.autocomplete.di.AutoCompleteComponent;
import com.tokopedia.autocomplete.di.DaggerAutoCompleteComponent;
import com.tokopedia.autocomplete.presentation.SearchContract;
import com.tokopedia.autocomplete.presentation.activity.AutoCompleteActivity;
import com.tokopedia.autocomplete.presentation.presenter.SearchPresenter;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;

import javax.inject.Inject;

/**
 * @author erry on 23/02/17.
 */

public class SearchMainFragment extends BaseDaggerFragment implements SearchContract.View, ItemClickListener {
    public static final int PAGER_POSITION_PRODUCT = 0;
    public static final int PAGER_POSITION_SHOP = 1;


    public static final String FRAGMENT_TAG = "SearchHistoryFragment";
    public static final String INIT_QUERY = "INIT_QUERY";
    private static final String SEARCH_PARAMETER = "SEARCH_PARAMETER";
    private static final String MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Inject
    SearchPresenter presenter;

    private HostAutoCompleteAdapter adapter;
    private String networkErrorMessage;
    private boolean onTabShop;

    private SearchParameter searchParameter;
    private PerformanceMonitoring performanceMonitoring;

    public static SearchMainFragment newInstance() {

        Bundle args = new Bundle();

        SearchMainFragment fragment = new SearchMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchMainFragment newInstance(String query) {

        Bundle args = new Bundle();
        args.putString(INIT_QUERY, query);
        SearchMainFragment fragment = new SearchMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initResources();
        setRetainInstance(true);
    }

    @Override
    protected void initInjector() {
        AutoCompleteComponent component = DaggerAutoCompleteComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .build();
        component.inject(this);
        component.inject(presenter);
    }

    private BaseAppComponent getBaseAppComponent() {
        if(getActivity() == null || getActivity().getApplication() == null) return null;

        return ((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent();
    }

    private void initResources() {
        networkErrorMessage = getString(com.tokopedia.abstraction.R.string.msg_network_error);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.autocomplete_fragment_universearch, container, false);
        recyclerView = parentView.findViewById(R.id.list);
        prepareView();
        presenter.attachView(this);
        presenter.initializeDataSearch();
        return parentView;
    }

    private void prepareView() {
        HostAutoCompleteTypeFactory typeFactory = new HostAutoCompleteFactory(
                this,
                getChildFragmentManager()
        );
        layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
        adapter = new HostAutoCompleteAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(0);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public void setCurrentTab(final int pos) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public int getCurrentTab() {
        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 1) {
            return isOnTabShop() ? SearchMainFragment.PAGER_POSITION_SHOP : SearchMainFragment.PAGER_POSITION_PRODUCT;
        } else {
            return SearchMainFragment.PAGER_POSITION_PRODUCT;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_UNIVERSEARCH;
    }

    @Override
    public void showAutoCompleteResult(DefaultAutoCompleteViewModel defaultAutoCompleteViewModel,
                                       TabAutoCompleteViewModel tabAutoCompleteViewModel) {
        stopTracePerformanceMonitoring();
        adapter.setDefaultViewModel(defaultAutoCompleteViewModel);
        adapter.setSuggestionViewModel(tabAutoCompleteViewModel);
        if (defaultAutoCompleteViewModel.getList().isEmpty()) {
            recyclerView.scrollToPosition(1);
        } else {
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            searchParameter = savedInstanceState.getParcelable(SEARCH_PARAMETER);
            if(searchParameter != null) {
                presenter.search(searchParameter);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SEARCH_PARAMETER, searchParameter);
    }

    public void search(SearchParameter searchParameter){
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE);
        presenter.search(searchParameter);
    }

    public void deleteAllRecentSearch(){
        presenter.deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword){
        presenter.deleteRecentSearchItem(keyword);
    }

    private void dropKeyBoard() {
        if (getActivity() != null && getActivity() instanceof AutoCompleteActivity) {
            ((AutoCompleteActivity) getActivity()).dropKeyboard();
        }
    }

    @Override
    public void onItemClicked(String applink, String webUrl) {
        dropKeyBoard();
        startActivityFromAutoComplete(applink);
    }

    private void startActivityFromAutoComplete(String applink) {
        if(getActivity() == null) return;

        RouteManager.route(getActivity(), applink);
        getActivity().finish();
    }

    @Override
    public void copyTextToSearchView(String text) {
        ((AutoCompleteActivity) getActivity()).setSearchQuery(text + " ");
    }

    @Override
    public void onDeleteRecentSearchItem(String keyword) {
        ((AutoCompleteActivity) getActivity()).deleteRecentSearch(keyword);
    }

    @Override
    public void onDeleteAllRecentSearch() {
        ((AutoCompleteActivity) getActivity()).deleteAllRecentSearch();
    }

    @Override
    public void setOnTabShop(boolean onTabShop) {
        this.onTabShop = onTabShop;
    }

    public boolean isOnTabShop() {
        return onTabShop;
    }

    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    private void stopTracePerformanceMonitoring() {
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
        }
    }
}
