package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.presenter.DealsLocationPresenter;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class SelectLocationFragment  extends BaseDaggerFragment implements
        DealsLocationContract.View, SearchInputView.Listener, DealsLocationAdapter.ActionListener{

    public static final String EXTRA_CALLBACK_LOCATION = "EXTRA_CALLBACK_LOCATION";

    private CoordinatorLayout mainContent;
    private CoordinatorLayout baseMainContent;
    private LinearLayout llTopEvents;
    private FrameLayout progressBarLayout;
    private LinearLayout noContent;
    private LinearLayout llSearchView;
    private ProgressBar progBar;
    private SearchInputView searchInputView;
    private RecyclerView rvSearchResults;
    private TextView tvTopDeals;
    @Inject
    public DealsLocationPresenter mPresenter;
    private DealsLocationAdapter dealsCategoryAdapter;


    public static Fragment createInstance() {
        Fragment fragment = new SelectLocationFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_location, container, false);
        setUpVariables(view);
        setHasOptionsMenu(true);
        mPresenter.getLocations();
        return view;
    }



    private void setUpVariables(View view) {
        rvSearchResults = view.findViewById(R.id.rv_search_results);
        searchInputView = view.findViewById(R.id.search_input_view);
        progBar = view.findViewById(R.id.prog_bar);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        mainContent = view.findViewById(R.id.main_content);
        tvTopDeals = view.findViewById(R.id.tv_popular_cities);
        llTopEvents = view.findViewById(R.id.ll_topcities);
        noContent = view.findViewById(R.id.no_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        dealsCategoryAdapter = new DealsLocationAdapter(getActivity(), null, this);
        llSearchView = view.findViewById(R.id.ll_search);
        rvSearchResults.setAdapter(dealsCategoryAdapter);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_location));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_17));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        searchInputView.setSearchImageView(getResources().getDrawable(R.drawable.ic_location));
        searchInputView.setListener(this);
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
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void renderFromSearchResults(List<LocationViewModel> locationViewModelList, boolean isTopLocations) {

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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchInputView.getSearchTextView().getWindowToken(), 0);
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

    @Override
    protected void initInjector() {
        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(getContext()))
                .build().inject(this);
        mPresenter.attachView(this);
    }


    @Override
    public void onLocationItemSelected(boolean locationUpdated) {

        getActivity().setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_LOCATION, locationUpdated));
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
