package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.activity.DealsCategoryDetailActivity;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsCategoryDetailContract;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsCategoryDetailPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoriesModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class CategoryDetailHomeFragment extends BaseDaggerFragment implements DealsCategoryDetailContract.View, View.OnClickListener {


    private RecyclerView recyclerViewDeals;
    private RecyclerView recyclerViewBrands;
    private View progressBarLayout;
    private ProgressBar progBar;
    private CoordinatorLayout mainContent;
    private ConstraintLayout baseMainContent;
    private Menu mMenu;
    private Toolbar toolbar;
    private TextView seeAllBrands;
    private final boolean IS_SHORT_LAYOUT = true;
    private TextView popularLocation;
    private TextView numberOfDeals;
    private LinearLayoutManager layoutManager;
    private CategoriesModel categoriesModel;



    @Inject
    DealsCategoryDetailPresenter mPresenter;


    @Override
    protected void initInjector() {
        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(getContext()))
                .build().inject(this);
        mPresenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new CategoryDetailHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.categoriesModel = getArguments().getParcelable(DealDetailsPresenter.HOME_DATA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_home, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);


        mPresenter.getCategoryDetails();
//        mPresenter.getBrandsList();
        return view;
    }


    private void setViewIds(View view) {

        ((DealsCategoryDetailActivity)getActivity()).getSupportActionBar().setTitle(categoriesModel.getTitle());

        seeAllBrands = view.findViewById(R.id.tv_see_all);

        popularLocation = view.findViewById(R.id.tv_popular);
        numberOfDeals = view.findViewById(R.id.number_of_locations);
        recyclerViewDeals = view.findViewById(R.id.recyclerView);
        recyclerViewBrands = view.findViewById(R.id.recyclerViewBrands);
        mainContent = view.findViewById(R.id.main_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progBar = view.findViewById(R.id.prog_bar);
        seeAllBrands.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(layoutManager);

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
    public void renderCategoryList(List<CategoryItemsViewModel> deals) {
        if (deals != null) {
            recyclerViewDeals.setAdapter(new DealsCategoryAdapter(getContext(), deals, !IS_SHORT_LAYOUT));
        }

        popularLocation.setText(String.format(getActivity().getResources().getString(R.string.popular_deals_in_location)
                , Utils.getSingletonInstance().getLocation(getContext()).getName()));
        recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
    }

    @Override
    public void renderBrandList(List<BrandViewModel> brandList) {
        if (brandList != null) {
            recyclerViewBrands.setAdapter(new DealsBrandAdapter(getActivity(), brandList, true));
        }
    }

    @Override
    public void showProgressBar() {
        progBar.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBar.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        Log.d("onCreateOptions1", "aagye");
        inflater.inflate(R.menu.menu_brand_details, menu);
        mMenu = menu;
        onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    @Override
    public void hideSearchButton() {
        MenuItem item = mMenu.findItem(R.id.action_search);
        item.setVisible(false);
        item.setEnabled(false);
    }

    @Override
    public void showSearchButton() {
        MenuItem item = mMenu.findItem(R.id.action_search);
        item.setVisible(true);
        item.setEnabled(true);
    }

    @Override
    public void startGeneralWebView(String url) {

    }

    @Override
    public void removeFooter() {

        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addFooter();

    }

    @Override
    public void addDealsToCards(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addAll(categoryItemsViewModels);

    }


    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
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
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        Log.d("Myurllll", " " + categoriesModel.getUrl());
        requestParams.putString(DealsHomePresenter.TAG, categoriesModel.getUrl());
        return requestParams;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void onClick(View v) {
        mPresenter.onOptionMenuClick(v.getId());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}
