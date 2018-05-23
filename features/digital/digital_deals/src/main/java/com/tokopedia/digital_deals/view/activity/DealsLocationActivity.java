package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.presenter.DealsLocationPresenter;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;


public class DealsLocationActivity extends BaseSimpleActivity implements
        DealsLocationContract.View, SearchInputView.Listener, DealsLocationAdapter.ActionListener{

    private DealsComponent dealsComponent;
    @Inject
    public DealsLocationPresenter mPresenter;

    public static final String EXTRA_CALLBACK_LOCATION = "EXTRA_CALLBACK_LOCATION";
    private CoordinatorLayout mainContent;
    private CoordinatorLayout baseMainContent;
    private LinearLayout llTopEvents;
    private FrameLayout progressBarLayout;
    private ProgressBar progBar;
    private SearchInputView searchInputView;

    private RecyclerView rvSearchResults;
    private TextView tvTopDeals;
    private LinearLayout noContent;
    private LinearLayout llSearchView;
    private DealsLocationAdapter dealsCategoryAdapter;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_change_location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setBackgroundResource(R.color.white);
        initInjector();
        mPresenter.attachView(this);
        setUpVariables();
        mPresenter.getLocations();
    }

    private void setUpVariables() {
        rvSearchResults = findViewById(R.id.rv_search_results);
        searchInputView = findViewById(R.id.search_input_view);
        progBar = findViewById(R.id.prog_bar);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        mainContent = findViewById(R.id.main_content);
        tvTopDeals = findViewById(R.id.tv_popular_cities);
        llTopEvents = findViewById(R.id.ll_topcities);
        noContent = findViewById(R.id.no_content);
        baseMainContent = findViewById(R.id.base_main_content);
        dealsCategoryAdapter = new DealsLocationAdapter(getActivity(), null, this);
        llSearchView = findViewById(R.id.ll_search);
        rvSearchResults.setAdapter(dealsCategoryAdapter);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_location));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_17));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        searchInputView.setSearchImageView(getResources().getDrawable(R.drawable.ic_location));
        searchInputView.setListener(this);


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
    public void renderFromSearchResults(List<LocationViewModel> locationViewModelList, boolean isTopLocations) {
        Log.d("hdshsjhsaj", "" + locationViewModelList.size() + "  " + isTopLocations);

        if (isTopLocations) {
            tvTopDeals.setVisibility(View.VISIBLE);
            baseMainContent.setVisibility(View.VISIBLE);
        } else {
            tvTopDeals.setVisibility(View.GONE);
        }
        if (locationViewModelList != null && locationViewModelList.size() != 0) {
            dealsCategoryAdapter.updateAdapter(locationViewModelList);
            rvSearchResults.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            llTopEvents.setVisibility(View.VISIBLE);
            if (isTopLocations) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    Log.d("inputMeethod", "notnull");
                    imm.hideSoftInputFromWindow(searchInputView.getSearchTextView().getWindowToken(), 0);
//                searchInputView.getSearchTextView().setFocusable(false);
                    rvSearchResults.requestFocus();
                }
            }

        } else {
            rvSearchResults.setVisibility(View.GONE);
            llTopEvents.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
        progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
        progBar.setVisibility(View.GONE);
    }

    @Override
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
        llSearchView.setVisibility(View.VISIBLE);
    }

    @Override
    public RequestParams getParams() {
        return RequestParams.EMPTY;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    private void initInjector() {
        dealsComponent = DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(this))
                .build();
        dealsComponent.inject(this);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onLocationItemSelected(boolean locationUpdated) {

        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_LOCATION, locationUpdated));
        finish();
    }
}
