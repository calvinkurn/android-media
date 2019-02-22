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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.CategoryDetailActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsCategoryDetailContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.DealsCategoryDetailPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.CategoryDetailCallbacks;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class CategoryDetailHomeFragment extends BaseDaggerFragment implements DealsCategoryDetailContract.View, View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest {

    private final boolean IS_SHORT_LAYOUT = true;
    private RecyclerView recyclerViewDeals;
    private RecyclerView recyclerViewBrands;
    private View progressBarLayout;
    private ProgressBar progBar;
    private CoordinatorLayout mainContent;
    private CoordinatorLayout baseMainContent;
    private ConstraintLayout clBrands;
    private Menu mMenu;
    private TextView seeAllBrands;
    private TextView popularLocation;
    private TextView numberOfDeals;
    private LinearLayoutManager layoutManager;
    private ScrollView noContent;
    @Inject
    DealsAnalytics dealsAnalytics;
    @Inject
    DealsCategoryDetailPresenter mPresenter;
    private CategoriesModel categoriesModel;
    private CategoryDetailCallbacks fragmentCallbacks;
    private String locationName;
    private DealsCategoryAdapter dealsAdapter;
    private int adapterPosition = -1;
    private boolean forceRefresh;


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
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
        this.categoriesModel = getArguments().getParcelable(CategoryDetailActivity.CATEGORIES_DATA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail_home, container, false);
        setHasOptionsMenu(true);
        setViewIds(view);


        mPresenter.getCategoryDetails(true);
        mPresenter.getBrandsList(true);
        return view;
    }


    private void setViewIds(View view) {

        seeAllBrands = view.findViewById(R.id.tv_see_all);
        popularLocation = view.findViewById(R.id.tv_popular);
        numberOfDeals = view.findViewById(R.id.number_of_locations);
        recyclerViewDeals = view.findViewById(R.id.rv_deals);
        recyclerViewBrands = view.findViewById(R.id.rv_brands);
        mainContent = view.findViewById(R.id.main_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progBar = view.findViewById(R.id.prog_bar);
        clBrands = view.findViewById(R.id.cl_brands);
        noContent = view.findViewById(R.id.scroll_view_no_content);
        seeAllBrands.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(layoutManager);
        dealsAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.CATEGORY_PAGE, this, !IS_SHORT_LAYOUT);
        dealsAdapter.setCategoryName(categoriesModel.getTitle());
        recyclerViewDeals.setAdapter(dealsAdapter);

    }


    @Override
    protected String getScreenName() {
        return null;
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
    public void renderCategoryList(List<ProductItem> deals, int count) {
        Location location = Utils.getSingletonInstance().getLocation(getContext());
        if (location != null)
            locationName = location.getName();
        if (deals != null && deals.size() > 0) {
            if (count == 0)
                numberOfDeals.setText(String.format(getResources().getString(R.string.number_of_items), deals.size()));
            else
                numberOfDeals.setText(String.format(getResources().getString(R.string.number_of_items), count));
            dealsAdapter.clearList();
            dealsAdapter.addAll(deals, false);
            dealsAdapter.notifyDataSetChanged();
            recyclerViewDeals.setVisibility(View.VISIBLE);
            recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
            noContent.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(locationName))
                dealsAnalytics.sendEventDealsDigitalView(DealsAnalytics.EVENT_NO_DEALS_AVAILABLE_ON_YOUR_LOCATION,
                        locationName);
            numberOfDeals.setText(String.format(getResources().getString(R.string.number_of_items), count));
            recyclerViewDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(locationName))
            popularLocation.setText(String.format(getActivity().getResources().getString(R.string.popular_deals_in_location)
                    , locationName));
        else
            popularLocation.setText(getActivity().getResources().getString(R.string.text_deals));
    }

    @Override
    public void renderBrandList(List<Brand> brandList) {
        if (brandList != null) {
            clBrands.setVisibility(View.VISIBLE);
            recyclerViewBrands.setAdapter(new DealsBrandAdapter(brandList, true));
        } else {
            clBrands.setVisibility(View.GONE);
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

        inflater.inflate(R.menu.menu_category_detail, menu);
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
        MenuItem item = mMenu.findItem(R.id.action_menu_search);
        item.setVisible(false);
    }

    @Override
    public void showSearchButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_search);
        item.setVisible(true);

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
    public void addDealsToCards(List<ProductItem> productItems) {
        if (productItems != null) {
            ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addAll(productItems);
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void checkLocationStatus() {

        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location == null) {
            navigateToActivityRequest(new Intent(getActivity(), DealsLocationActivity.class), DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);
        } else {
            Intent searchIntent = new Intent(getActivity(), DealsSearchActivity.class);
            navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
        }
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
    public RequestParams getCategoryParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DealsHomePresenter.TAG, categoriesModel.getCategoryUrl());
        Location location = Utils.getSingletonInstance().getLocation(getContext());
        if (location != null)
            requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
        return requestParams;
    }

    @Override
    public RequestParams getBrandParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND);
        requestParams.putInt(Utils.QUERY_PARAM_CHILD_CATEGORY_ID, categoriesModel.getCategoryId());
        Location location = Utils.getSingletonInstance().getLocation(getContext());
        if (location != null)
            requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
        return requestParams;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (getActivity() == null)
            return;
        switch (requestCode) {
            case DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null) {
                        Intent searchIntent = new Intent(getActivity(), DealsSearchActivity.class);
                        navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
                    }
                }
                break;
            case DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null && !TextUtils.isEmpty(locationName) && !TextUtils.isEmpty(location.getName()) && !locationName.equals(location.getName())) {
                        mPresenter.getCategoryDetails(true);
                        mPresenter.getBrandsList(true);
                    }

                }
                break;
            case DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null && !TextUtils.isEmpty(locationName) && !TextUtils.isEmpty(location.getName()) && !locationName.equals(location.getName())) {
                        mPresenter.getCategoryDetails(true);
                        mPresenter.getBrandsList(true);
                    } else {
                        mPresenter.getCategoryDetails(false);
                        mPresenter.getBrandsList(false);
                    }

                }
                break;
            case DealsHomeActivity.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    UserSessionInterface userSession = new UserSession(getActivity());
                    if (userSession.isLoggedIn()) {
                        if (adapterPosition != -1) {
                            if (dealsAdapter != null)
                                dealsAdapter.setLike(adapterPosition);
                        }
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_see_all) {
            fragmentCallbacks.replaceFragment(categoriesModel);
        } else {
            mPresenter.onOptionMenuClick(v.getId());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (CategoryDetailActivity) activity;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        adapterPosition = position;
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void onStop() {
        forceRefresh = true;
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (forceRefresh) {
            if (dealsAdapter != null)
                dealsAdapter.notifyDataSetChanged();
            forceRefresh = false;
        }
    }
}
