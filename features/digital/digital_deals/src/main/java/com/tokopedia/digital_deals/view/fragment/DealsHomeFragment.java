package com.tokopedia.digital_deals.view.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryItemAdapter;
import com.tokopedia.digital_deals.view.adapter.SlidingImageAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.customview.WrapContentHeightViewPager;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class DealsHomeFragment extends BaseDaggerFragment implements DealsContract.View, View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest {


    private final int SPAN_COUNT_4 = 4;
    private Menu mMenu;
    @Inject
    public DealsHomePresenter mPresenter;
    private WrapContentHeightViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;
    private CoordinatorLayout mainContent;
    private View progressBarLayout;
    private ProgressBar progBar;
    private RecyclerView rvCatItems;
    private RecyclerView rvTrendingDeals;
    private RecyclerView rvBrandItems;
    private CoordinatorLayout baseMainContent;
    private LinearLayout noContent;
    private ConstraintLayout clBrands;
    private ConstraintLayout clBanners;
    private TextView searchInputView;
    private AppBarLayout appBarLayout;
    private final boolean IS_SHORT_LAYOUT = false;

    private ConstraintLayout clSearch;
    private TextView tvLocationName;
    private LinearLayoutManager layoutManager;
    private TextView tvSeeAllBrands;
    private TextView tvSeeAllPromo;
    private int adapterPosition = -1;
    private boolean forceRefresh;
    private DealsCategoryAdapter categoryAdapter;

    public static Fragment createInstance() {
        Fragment fragment = new DealsHomeFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals_home, container, false);

        setHasOptionsMenu(true);
        setUpVariables(view);
        checkLocationStatus();

        return view;
    }

    private void checkLocationStatus() {

        Location location = Utils.getSingletonInstance().getLocation(getActivity());

        if (location != null) {
            tvLocationName.setText(location.getName());
            mPresenter.getDealsList(true);

        } else {
            navigateToActivityRequest(new Intent(getActivity(), DealsLocationActivity.class), DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);
        }

    }

    private void setUpVariables(View view) {
        rvCatItems = view.findViewById(R.id.rv_category_items);
        rvBrandItems = view.findViewById(R.id.rv_brand_items);
        rvTrendingDeals = view.findViewById(R.id.rv_trending_deals);
        viewPager = view.findViewById(R.id.deals_bannerpager);
        circlePageIndicator = view.findViewById(R.id.pager_indicator);
        mainContent = view.findViewById(R.id.main_content);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progBar = view.findViewById(R.id.prog_bar);
        baseMainContent = view.findViewById(R.id.base_main_content);
        searchInputView = view.findViewById(R.id.search_input_view);
        tvLocationName = view.findViewById(R.id.tv_location_name);
        tvSeeAllPromo = view.findViewById(R.id.see_all_promo);
        clBrands = view.findViewById(R.id.cl_brands);
        clBanners = view.findViewById(R.id.cl_banners);
        noContent = view.findViewById(R.id.no_content);
        clSearch = view.findViewById(R.id.cl_search_view);
        tvSeeAllBrands = view.findViewById(R.id.tv_see_all_brands);
        tvSeeAllBrands.setOnClickListener(this);
        searchInputView.setOnClickListener(this);
        tvLocationName.setOnClickListener(this);
        tvSeeAllPromo.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTrendingDeals.setNestedScrollingEnabled(false);
        rvTrendingDeals.setLayoutManager(layoutManager);
        rvCatItems.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT_4,
                GridLayoutManager.VERTICAL, false));
        rvBrandItems.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT_4,
                GridLayoutManager.VERTICAL, false));

        Drawable img = getResources().getDrawable(R.drawable.ic_search_grey_deal);
        setDrawableTint(img);
        searchInputView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);


        appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                hideSearchButton();
            } else {
                showSearchButton();
            }
            Log.d("Offest Changed", "Offset : " + verticalOffset);
        });
        img = getResources().getDrawable(R.drawable.ic_location_2);
        tvLocationName.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

    }

    private void setDrawableTint(Drawable img) {
        Drawable wrappedDrawable = DrawableCompat.wrap(img);
        Drawable mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.color_search_icon));
    }

    public void hideSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_menu_search);
            item.setVisible(false);
            item.setEnabled(false);
        }
    }

    public void showSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_menu_search);
            item.setVisible(true);
            item.setEnabled(true);
        }
    }

    @Override
    protected void initInjector() {
        NetworkClient.init(getActivity());
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
        mPresenter.initialize();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY:
                Location location = Utils.getSingletonInstance().getLocation(getActivity());
                if (location == null) {
                    if (getActivity() != null)
                        getActivity().finish();
                } else {
                    if (data != null) {
                        boolean isLocationUpdated = data.getBooleanExtra(SelectLocationFragment.EXTRA_CALLBACK_LOCATION, true);
                        if (isLocationUpdated) {
                            Utils.getSingletonInstance().showSnackBarDeals(location.getName(), getActivity(), mainContent, true);
                        }
                        mPresenter.getDealsList(true);
                    }
                    tvLocationName.setText(location.getName());
                }
                break;
            case DealsHomeActivity.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    if (getActivity() != null && getActivity().getApplication() != null) {
                        UserSessionInterface userSession = new UserSession(getActivity());
                        if (userSession.isLoggedIn()) {
                            if (adapterPosition == -1) {
                                startOrderListActivity();
                            } else {
                                if (rvTrendingDeals.getAdapter() != null)
                                    ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).setLike(adapterPosition);
                            }
                        }
                    }
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void renderCategoryList(List<CategoryItem> categoryList, CategoryItem
            carousel, CategoryItem top) {

        if (top.getItems() != null && top.getItems().size() > 0) {
            rvTrendingDeals.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            categoryAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.HOME_PAGE, this, IS_SHORT_LAYOUT);
            rvTrendingDeals.setAdapter(categoryAdapter);
            categoryAdapter.addAll(top.getItems(), false);
            categoryAdapter.notifyDataSetChanged();
        } else {
            mPresenter.sendEventView(DealsAnalytics.EVENT_NO_DEALS_AVAILABLE_ON_YOUR_LOCATION,
                    tvLocationName.getText().toString());
            rvTrendingDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
        if (carousel.getItems() != null && carousel.getItems().size() > 0) {
            clBanners.setVisibility(View.VISIBLE);
            setViewPagerListener(new SlidingImageAdapter(carousel.getItems(), mPresenter));
            circlePageIndicator.setViewPager(viewPager);
            mPresenter.startBannerSlide(viewPager);
        } else {
            clBanners.setVisibility(View.GONE);
        }
        if (categoryList != null) {
            rvCatItems.setAdapter(new DealsCategoryItemAdapter(categoryList));
        }

    }

    @Override
    public void renderBrandList(List<Brand> brandList) {
        if (brandList != null) {
            clBrands.setVisibility(View.VISIBLE);
            DealsBrandAdapter dealsBrandAdapter = new DealsBrandAdapter(brandList, true);
            dealsBrandAdapter.setPopularBrands(true);
            rvBrandItems.setAdapter(dealsBrandAdapter);

        } else {
            clBrands.setVisibility(View.GONE);
        }
    }

    @Override
    public void addDealsToCards(CategoryItem top) {
        if (top.getItems() != null) {
            ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).addAll(top.getItems());
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void removeFooter() {
        ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).addFooter();
    }

    @Override
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
        clSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void startOrderListActivity() {
        RouteManager.route(getActivity(), ApplinkConst.DEALS_ORDER);
    }

    @Override
    public int getRequestCode() {
        return DealsHomeActivity.REQUEST_CODE_LOGIN;
    }

    private void setViewPagerListener(SlidingImageAdapter adapter) {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mPresenter.onBannerSlide(arg0);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
    }

    @Override
    public RequestParams getParams() {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DealsHomePresenter.TAG, location.getSearchName());
        return requestParams;
    }

    @Override
    public RequestParams getBrandParams() {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND);
        requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
        return requestParams;
    }

    @Override
    public View getRootView() {
        return mainContent;
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
    public void hideFavouriteButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_favourite);
        item.setVisible(false);
    }

    @Override
    public void showFavouriteButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_favourite);
        item.setVisible(false);
    }

    @Override
    public void startGeneralWebView(String url) {
        ((DealsModuleRouter) getActivity().getApplication())
                .actionOpenGeneralWebView(getActivity(), url);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_deals_home, menu);
        mMenu = menu;
        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(s);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onOptionMenuClick(v.getId());
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        try {
            if (rvTrendingDeals.getAdapter() != null) {
                ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).unsubscribeUseCase();
            }
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        this.adapterPosition = position;
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
            if (categoryAdapter != null)
                categoryAdapter.notifyDataSetChanged();
            forceRefresh = false;
        }
    }
}
