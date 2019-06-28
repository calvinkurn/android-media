package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.AllBrandsActivity;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
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

public class AllBrandsFragment extends BaseDaggerFragment implements AllBrandsContract.View, SearchInputView.Listener{

    private static final boolean IS_SHORT_LAYOUT = true;
    private static final String ARG_PARAM_EXTRA_DEALS_DATA = "ARG_PARAM_EXTRA_DEALS_DATA";
    private static final String SCREEN_NAME = "/digital/deals/brand";
    private LinearLayout baseMainContent;
    private LinearLayout noContent;
    private FrameLayout progressBarLayout;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerview;
    @Inject
    AllBrandsPresenter mPresenter;
    private CategoriesModel categoriesModel;
    private String searchText;
    private String selectedLocation;
    private UpdateLocation updateLocation;
    private Location currentLocation;


    public static Fragment newInstance(CategoriesModel categoriesModel, String searchText) {
        AllBrandsFragment categoryFragment = new AllBrandsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_DEALS_DATA, categoriesModel);
        args.putString(AllBrandsActivity.SEARCH_TEXT, searchText);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.categoriesModel = getArguments().getParcelable(ARG_PARAM_EXTRA_DEALS_DATA);
        this.searchText = getArguments().getString(AllBrandsActivity.SEARCH_TEXT);
        currentLocation = Utils.getSingletonInstance().getLocation(getActivity());
        setHasOptionsMenu(true);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            updateLocation = (UpdateLocation) activity;
        }
        catch (Exception e) {
            throw new ClassCastException(activity.toString() + "must implement Update Location Interface");
        }

    }

    public void getLocations(String selectedLocation) {
        this.selectedLocation = selectedLocation;
        mPresenter.getLocations();
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
        mPresenter.sendScreenNameEvent(getScreenName());
        mPresenter.getAllBrands();
    }

    private void setUpVariables(View view) {
        recyclerview = view.findViewById(R.id.rv_brand_items);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        noContent = view.findViewById(R.id.no_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        layoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(new DealsBrandAdapter(null, DealsBrandAdapter.ITEM_BRAND_NORMAL));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_menu_search);
        if (item != null)
            item.setVisible(false);
    }

    @Override
    public void onSearchSubmitted(String text) {
        onSearchTextChanged(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        if(text == null || text.equals(searchText)){
            return;
        }
        searchText = text;
        mPresenter.searchSubmitted(searchText);
    }


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
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
            ((DealsBrandAdapter) recyclerview.getAdapter()).setBrandNativePage(true);
            recyclerview.setVisibility(View.VISIBLE);
            recyclerview.addOnScrollListener(rvOnScrollListener);
            noContent.setVisibility(View.GONE);
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
        if (!TextUtils.isEmpty(searchText)) {
            requestParams.putString(Utils.BRAND_QUERY_TAGS, searchText);
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

    @Override
    public void startLocationFragment(List<Location> locations) {
        updateLocation.startLocationFragment(locations);
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

    public void onLocationUpdated() {
        currentLocation = Utils.getSingletonInstance().getLocation(getActivity());
        if (currentLocation!= null) {
            mPresenter.getAllBrands();
        }
    }

    public void reloadIfLocationUpdated(){
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if(location!=null) {
            if (!location.equals(currentLocation)){
                onLocationUpdated();
            }
        }

    }

    public interface UpdateLocation {
        void startLocationFragment(List<Location> locations);
    }
}
