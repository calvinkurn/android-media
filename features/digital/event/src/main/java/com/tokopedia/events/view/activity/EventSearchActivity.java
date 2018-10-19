package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.adapter.TopEventsSuggestionsAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.customview.SearchInputView;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tokopedia.events.view.contractor.EventFilterContract.REQ_OPEN_FILTER;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchActivity extends TActivity implements
        EventSearchContract.IEventSearchView, SearchInputView.Listener {

    EventComponent eventComponent;
    @Inject
    public EventSearchPresenter mPresenter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_search);
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        ButterKnife.bind(this);
        searchInputView.setListener(this);
        mPresenter.attachView(this);
        toolbar.setTitle("Events");
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mPresenter.initialize();

    }

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventSearchActivity.class);
    }

    @Override
    public void onSearchSubmitted(String text) {
        if (getRootView() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(getRootView().getWindowToken(), 0);
        }
        mPresenter.searchSubmitted(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        filterBtn.setVisibility(View.GONE);
        mPresenter.searchTextChanged(text);

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getParams() {
        return null;
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
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, searchViewModels, mPresenter, true);
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
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, suggestions, mPresenter, showCards);
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
        tvFilter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_list, 0, R.drawable.oval_3, 0);
    }

    @Override
    public void setFilterInactive() {
        TextView tvFilter = findViewById(R.id.tv_filter);
        tvFilter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_list, 0, 0, 0);
    }

    private void executeInjector() {
        if (eventComponent == null) initInjector();
        eventComponent.inject(this);
    }

    private void initInjector() {
        eventComponent = DaggerEventComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .eventModule(new EventModule(this))
                .build();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return mPresenter.getSCREEN_NAME();
    }

    @OnClick(R2.id.iv_finish)
    void clickFinish() {
        finish();
    }

    @OnClick(R2.id.btn_filter)
    void onClickFilter() {
        mPresenter.openFilters();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
