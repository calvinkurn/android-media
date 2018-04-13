package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.TopEventsSuggestionsAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.presenter.DealsSearchPresenter;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;


public class DealsSearchActivity extends BaseSimpleActivity implements
        DealsSearchContract.IDealsSearchView, SearchInputView.Listener, View.OnClickListener {

    private Context context;
    private DealsComponent dealsComponent;
    @Inject
    public DealsSearchPresenter mPresenter;

    private FrameLayout mainContent;
    private LinearLayout llTopEvents;
    private View progressBarLayout;
    private ProgressBar progBar;
    private SearchInputView searchInputView;

    private RecyclerView rvSearchResults;
    private RecyclerView rvTopDealsSuggestions;
    private TextView tvTopDeals;
    private ImageView back;
    private LinearLayoutManager layoutManager;
    private DividerItemDecoration mDividerItemDecoration;
    private LinearLayout noContent;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_deals_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initInjector();
        executeInjector();
        setUpVariables();
        searchInputView.setListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mPresenter.attachView(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mPresenter.initialize();

    }

    private void setUpVariables() {
        rvTopDealsSuggestions = findViewById(R.id.rv_top_events_suggestions);
        rvSearchResults = findViewById(R.id.rv_search_results);
        searchInputView = findViewById(R.id.search_input_view);
        progBar = findViewById(R.id.prog_bar);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        mainContent = findViewById(R.id.main_content);
        tvTopDeals = findViewById(R.id.tv_topevents);
        llTopEvents = findViewById(R.id.ll_topevents);
        back=findViewById(R.id.imageViewBack);
        noContent=findViewById(R.id.no_content);
        back.setOnClickListener(this);
        mDividerItemDecoration = new DividerItemDecoration(rvTopDealsSuggestions.getContext());
    }

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, DealsSearchActivity.class);
    }

    @Override
    public void onSearchSubmitted(String text) {
        mPresenter.searchSubmitted(text);
    }

    @Override
    public void onSearchTextChanged(String text) {

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

    }

    @Override
    public void renderFromSearchResults(List<CategoryItemsViewModel> categoryItemsViewModels) {
        if (categoryItemsViewModels != null && categoryItemsViewModels.size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            DealsCategoryAdapter eventCategoryAdapter = new DealsCategoryAdapter(getActivity(), categoryItemsViewModels);
            rvSearchResults.addOnScrollListener(rvOnScrollListener);
            rvSearchResults.setLayoutManager(linearLayoutManager);
            rvSearchResults.setAdapter(eventCategoryAdapter);
            rvSearchResults.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            llTopEvents.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                Log.d("inputMeethod", "notnull");
                imm.hideSoftInputFromWindow(searchInputView.getSearchTextView().getWindowToken(), 0);
//                searchInputView.getSearchTextView().setFocusable(false);
                rvSearchResults.requestFocus();
            }


        } else {
            rvSearchResults.setVisibility(View.GONE);
            llTopEvents.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public RequestParams getParams() {
        return RequestParams.EMPTY;
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
    public void setTrendingDeals(List<SearchViewModel> searchViewModels) {
        if (searchViewModels != null && !searchViewModels.isEmpty()) {
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, searchViewModels, mPresenter);
            rvTopDealsSuggestions.setLayoutManager(layoutManager);
            rvTopDealsSuggestions.setAdapter(adapter);
            rvTopDealsSuggestions.addItemDecoration(mDividerItemDecoration);
            rvTopDealsSuggestions.removeOnScrollListener(rvOnScrollListener);
            rvSearchResults.removeOnScrollListener(rvOnScrollListener);
            tvTopDeals.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            llTopEvents.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.GONE);
        } else {
            llTopEvents.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSuggestions(List<SearchViewModel> suggestions, String highlight) {
        if (suggestions != null && !suggestions.isEmpty()) {
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, suggestions, mPresenter);
            adapter.setHighLightText(highlight);
            rvTopDealsSuggestions.setLayoutManager(layoutManager);
            rvTopDealsSuggestions.setAdapter(adapter);
            rvTopDealsSuggestions.addItemDecoration(mDividerItemDecoration);
            rvTopDealsSuggestions.addOnScrollListener(rvOnScrollListener);
            llTopEvents.setVisibility(View.VISIBLE);
            tvTopDeals.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.GONE);
            noContent.setVisibility(View.GONE);
        } else {
            rvSearchResults.setVisibility(View.GONE);
            llTopEvents.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void removeFooter(boolean searchSubmitted) {
        if (searchSubmitted) {
            ((DealsCategoryAdapter) rvSearchResults.getAdapter()).removeFooter();
        }
        else
            ((TopEventsSuggestionsAdapter) rvTopDealsSuggestions.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter(boolean searchSubmitted) {
        if (searchSubmitted)
            ((DealsCategoryAdapter) rvSearchResults.getAdapter()).addFooter();
        else
            ((TopEventsSuggestionsAdapter) rvTopDealsSuggestions.getAdapter()).addFooter();
    }

    @Override
    public void addDealsToCards(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ((DealsCategoryAdapter) rvSearchResults.getAdapter()).addAll(categoryItemsViewModels);

    }


    @Override
    public void addDeals(List<SearchViewModel> searchViewModels) {
        ((TopEventsSuggestionsAdapter) rvTopDealsSuggestions.getAdapter()).addAll(searchViewModels);

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }


    private void initInjector() {
        dealsComponent = DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(this))
                .build();
    }

    private void executeInjector() {
        if (dealsComponent == null) initInjector();
        dealsComponent.inject(this);
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
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageViewBack){
            super.onBackPressed();
        }
    }
}
