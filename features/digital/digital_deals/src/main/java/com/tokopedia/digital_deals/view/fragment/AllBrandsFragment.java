package com.tokopedia.digital_deals.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.contractor.AllBrandsContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.AllBrandsPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class AllBrandsFragment extends BaseDaggerFragment implements AllBrandsContract.View, SearchInputView.Listener {

    private static final boolean IS_SHORT_LAYOUT = true;
    private static final String ARG_PARAM_EXTRA_DEALS_DATA = "ARG_PARAM_EXTRA_DEALS_DATA";
    private final int SPAN_COUNT_3 = 3;
    private LinearLayout baseMainContent;
    private LinearLayout noContent;
    private FrameLayout progressBarLayout;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerview;
    private SearchInputView searchInputView;
    @Inject
    AllBrandsPresenter mPresenter;
    private CategoriesModel categoriesModel;
    private String searchText;


    public static Fragment newInstance(CategoriesModel categoriesModel) {
        AllBrandsFragment categoryFragment = new AllBrandsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_DEALS_DATA, categoriesModel);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.categoriesModel = getArguments().getParcelable(ARG_PARAM_EXTRA_DEALS_DATA);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_brands, container, false);
        setUpVariables(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getAllBrands();
    }

    private void setUpVariables(View view) {
        recyclerview = view.findViewById(R.id.rv_brand_items);
        searchInputView = view.findViewById(R.id.search_input_view);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        noContent = view.findViewById(R.id.no_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_brand));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_17));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT_3, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(new DealsBrandAdapter(null, DealsBrandAdapter.ITEM_BRAND_NORMAL));
        searchInputView.setListener(this);
        KeyboardHandler.DropKeyboard(getContext(), searchInputView);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_menu_search);
        if (item != null)
            item.setVisible(false);
    }

    @Override
    public void onSearchSubmitted(String text) {
        searchText = text;
        mPresenter.sendEventClick(DealsAnalytics.EVENT_CLICK_SEARCH_BRAND, text);
        mPresenter.searchSubmitted(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        searchText = text;
        mPresenter.sendEventClick(DealsAnalytics.EVENT_CLICK_SEARCH_BRAND, text);
        mPresenter.searchTextChanged(text);
    }


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
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
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void renderBrandList(List<Brand> brandList, boolean isSearchSubmitted, Boolean... fromSearchResult) {

        boolean fromSearch = false;
        if (fromSearchResult.length > 0) {
            if (fromSearchResult[0] != null) {
                fromSearch = fromSearchResult[0];
            }
        }
        if (brandList != null && brandList.size() > 0) {
            ((DealsBrandAdapter) recyclerview.getAdapter()).updateAdapter(brandList, fromSearch);
            recyclerview.setVisibility(View.VISIBLE);
            recyclerview.addOnScrollListener(rvOnScrollListener);
            noContent.setVisibility(View.GONE);
            if (isSearchSubmitted) {
                KeyboardHandler.DropKeyboard(getContext(), searchInputView);
            }
        } else {
            mPresenter.sendEventView(DealsAnalytics.EVENT_NO_BRAND_FOUND,
                    searchText);
            recyclerview.setVisibility(View.GONE);
            recyclerview.removeOnScrollListener(rvOnScrollListener);
            noContent.setVisibility(View.VISIBLE);
        }
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
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
        noContent.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getParams() {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND);
        if (location != null)
            requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
        if (categoriesModel.getPosition() != 0) {
            requestParams.putInt(Utils.QUERY_PARAM_CHILD_CATEGORY_ID, categoriesModel.getCategoryId());
        }
        return requestParams;
    }

    @Override
    public View getRootView() {
        return baseMainContent;
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
    public void addBrandsToCards(List<Brand> brandList) {
        if (brandList != null) {
            ((DealsBrandAdapter) recyclerview.getAdapter()).addAll(brandList);
        }

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void showEmptyView() {
        noContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        noContent.setVisibility(View.GONE);
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
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }
}
