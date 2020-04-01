package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.events.R;
import com.tokopedia.events.view.adapter.TopEventsSuggestionsAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.customview.SearchInputView;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.user.session.UserSession;

import java.util.List;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchActivity extends EventBaseActivity implements
        EventSearchContract.EventSearchView, SearchInputView.Listener, View.OnClickListener {

    public static final String SCREEN_NAME = "digital/events/search";
    public EventSearchPresenter eventSearchPresenter;

    FrameLayout mainContent;

    View progressBarLayout;
    ProgressBar progBar;
    SearchInputView searchInputView;
    View noResults;
    RecyclerView rvSearchResults;
    RecyclerView rvTopEventSuggestions;
    TextView tvTopevents;
    View filterBtn;
    ImageView ivFinish;

    LinearLayoutManager layoutManager;

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
        searchInputView.setListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        eventsAnalytics = new EventsAnalytics();
        eventsAnalytics.sendScreenNameEvent(getScreenName());
    }

    @Override
    void setupVariables() {
        mainContent = findViewById(R.id.main_content);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        progBar = findViewById(R.id.prog_bar);
        searchInputView = findViewById(R.id.search_input_view);
        noResults = findViewById(R.id.no_search_results);
        rvSearchResults = findViewById(R.id.rv_search_results);
        rvTopEventSuggestions = findViewById(R.id.rv_top_events_suggestions);
        tvTopevents = findViewById(R.id.tv_topevents);
        filterBtn = findViewById(R.id.btn_filter);
        ivFinish = findViewById(R.id.iv_finish);

        ivFinish.setOnClickListener(this);
        filterBtn.setOnClickListener(this);
    }

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventSearchActivity.class);
    }

    @Override
    public void onSearchSubmitted(String text) {
        String customMessage = "NOTHING";
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
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, searchViewModels, eventSearchPresenter, false, false);
            rvTopEventSuggestions.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
    public void setSuggestions(List<CategoryItemsViewModel> suggestions, String highlight, boolean showCards, boolean shouldFireEvent) {
        if (suggestions != null && !suggestions.isEmpty()) {
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, suggestions, eventSearchPresenter, showCards, shouldFireEvent);
            if (!showCards)
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
        tvFilter.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (this, R.drawable.ic_filter_button), null, null, null);
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
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return SCREEN_NAME;
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_finish) {
            finish();
        } else if (v.getId() == R.id.btn_filter) {
            eventSearchPresenter.openFilters();
        }
    }
}
