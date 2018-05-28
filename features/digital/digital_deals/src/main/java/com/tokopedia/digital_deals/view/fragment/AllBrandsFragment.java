package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.contractor.DealAllBrandsContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.presenter.DealAllBrandsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoriesModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class AllBrandsFragment extends BaseDaggerFragment implements DealAllBrandsContract.View, View.OnClickListener, SearchInputView.Listener {

    private static final boolean IS_SHORT_LAYOUT = true;
    private final int SPAN_COUNT_3 = 3;
    private CategoriesModel categoriesModel;
    int mFragmentPos;
    private LinearLayout baseMainContent;

    @Inject
    DealAllBrandsPresenter mPresenter;
    private static final String ARG_PARAM_EXTRA_DEALS_DATA = "ARG_PARAM_EXTRA_DEALS_DATA";
    private static final String ARG_FRAGMENTPOSITION = "ARG_FRAG_POS";
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerview;

    private FrameLayout progressBarLayout;
    private ProgressBar progBar;
    private SearchInputView searchInputView;
    private LinearLayout noContent;
    private LinearLayout llSearchView;

    public static Fragment newInstance(CategoriesModel categoriesModel, int fragmentPosition) {
        AllBrandsFragment categoryFragment = new AllBrandsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_DEALS_DATA, categoriesModel);
        args.putInt(ARG_FRAGMENTPOSITION, fragmentPosition);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.categoriesModel = getArguments().getParcelable(ARG_PARAM_EXTRA_DEALS_DATA);
        this.mFragmentPos = getArguments().getInt(ARG_FRAGMENTPOSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_brands, container, false);
        setUpVariables(view);
        mPresenter.getAllBrands();

        return view;
    }

    private void setUpVariables(View view) {
        recyclerview = view.findViewById(R.id.recyclerViewBrandItems);
        searchInputView = view.findViewById(R.id.search_input_view);
        progBar = view.findViewById(R.id.prog_bar);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        noContent = view.findViewById(R.id.no_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        llSearchView = view.findViewById(R.id.ll_search);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_brand));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_17));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT_3, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(new DealsBrandAdapter(getContext(), null, !IS_SHORT_LAYOUT));
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
    public void onClick(View v) {

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
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void renderBrandList(List<BrandViewModel> brandList, boolean isSearchSubmitted) {
        if (brandList != null && brandList.size() != 0) {
            ((DealsBrandAdapter) recyclerview.getAdapter()).updateAdapter(brandList);
            recyclerview.setVisibility(View.VISIBLE);
            recyclerview.addOnScrollListener(rvOnScrollListener);
            noContent.setVisibility(View.GONE);
            if (isSearchSubmitted) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchInputView.getSearchTextView().getWindowToken(), 0);
                    recyclerview.requestFocus();
                }
            }
        } else {
            recyclerview.setVisibility(View.GONE);
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
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DealsHomePresenter.TAG, categoriesModel.getUrl());
        return requestParams;
    }

    @Override
    public View getRootView() {
        return baseMainContent;
    }


    @Override
    public void startGeneralWebView(String url) {

    }

    @Override
    public void removeFooter() {
        ((DealsBrandAdapter) recyclerview.getAdapter()).removeFooter();

    }

    @Override
    public void addFooter() {
        ((DealsBrandAdapter) recyclerview.getAdapter()).addFooter();

    }

    @Override
    public void addBrandsToCards(List<BrandViewModel> brandList) {
        ((DealsBrandAdapter) recyclerview.getAdapter()).addAll(brandList);

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
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


}
