package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.view.adapter.TopEventsSuggestionsAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.customview.SearchInputView;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchActivity extends EventBaseActivity implements
        EventSearchContract.EventSearchView, SearchInputView.Listener {

    public EventSearchPresenter eventSearchPresenter;

    @BindView(R2.id.main_content)
    FrameLayout mainContent;

    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.search_input_view)
    SearchInputView searchInputView;
    @BindView(R2.id.no_search_results)
    View noResults;

    @BindView(R2.id.rv_search_results)
    RecyclerView rvSearchResults;
    @BindView(R2.id.rv_top_events_suggestions)
    RecyclerView rvTopEventSuggestions;
    @BindView(R2.id.tv_topevents)
    TextView tvTopevents;
    @BindView(R2.id.btn_filter)
    View filterBtn;

    LinearLayoutManager layoutManager;

    Unbinder unbinder;
    private EventsAnalytics eventsAnalytics;

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getEventSearchPresenter();
        eventSearchPresenter = (EventSearchPresenter) mPresenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_events_search;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        searchInputView.setListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        eventsAnalytics = new EventsAnalytics(getApplicationContext());

    }

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventSearchActivity.class);
    }

    @Override
    public void onSearchSubmitted(String text) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(getRootView().getWindowToken(), 0);
        eventSearchPresenter.searchSubmitted(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        filterBtn.setVisibility(View.GONE);
        eventSearchPresenter.searchTextChanged(text);

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public FragmentManager getFragmentManagerInstance() {
        return getSupportFragmentManager();
    }

    @Override
    public void setTopEvents(List<CategoryItemsViewModel> searchViewModels) {
        if (searchViewModels != null && !searchViewModels.isEmpty()) {
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, searchViewModels, eventSearchPresenter, true);
            rvTopEventSuggestions.setLayoutManager(layoutManager);
            rvTopEventSuggestions.setAdapter(adapter);
            rvTopEventSuggestions.removeOnScrollListener(rvOnScrollListener);
            tvTopevents.setText(Utils.Constants.TOP_EVENTS);
            tvTopevents.setVisibility(View.VISIBLE);
            rvTopEventSuggestions.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.GONE);
            noResults.setVisibility(View.GONE);
        } else {
            tvTopevents.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.GONE);
            noResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSuggestions(List<CategoryItemsViewModel> suggestions, String highlight, boolean showCards) {
        if (suggestions != null && !suggestions.isEmpty()) {
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, suggestions, eventSearchPresenter, showCards);
            if (showCards)
                filterBtn.setVisibility(View.VISIBLE);
            adapter.setHighLightText(highlight);
            rvTopEventSuggestions.setLayoutManager(layoutManager);
            rvTopEventSuggestions.setAdapter(adapter);
            rvTopEventSuggestions.addOnScrollListener(rvOnScrollListener);
            tvTopevents.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.GONE);
            noResults.setVisibility(View.GONE);
        } else {
            tvTopevents.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void removeFooter() {
        ((TopEventsSuggestionsAdapter) rvTopEventSuggestions.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((TopEventsSuggestionsAdapter) rvTopEventSuggestions.getAdapter()).addFooter();

    }

    @Override
    public void addEvents(List<CategoryItemsViewModel> searchViewModels) {
        ((TopEventsSuggestionsAdapter) rvTopEventSuggestions.getAdapter()).addAll(searchViewModels);

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void setFilterActive() {
        TextView tvFilter = findViewById(R.id.tv_filter);
        tvFilter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_button, 0, R.drawable.oval_3, 0);
    }

    @Override
    public void setFilterInactive() {
        TextView tvFilter = findViewById(R.id.tv_filter);
        tvFilter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_button, 0, 0, 0);
    }


    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            eventSearchPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eventsAnalytics.eventDigitalEventTracking( EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return eventSearchPresenter.getSCREEN_NAME();
    }

    @OnClick(R2.id.iv_finish)
    void clickFinish() {
        finish();
    }

    @OnClick(R2.id.btn_filter)
    void onClickFilter() {
        eventSearchPresenter.openFilters();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eventSearchPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
