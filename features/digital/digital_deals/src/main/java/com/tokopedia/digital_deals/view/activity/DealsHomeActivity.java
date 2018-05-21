package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryItemAdapter;
import com.tokopedia.digital_deals.view.adapter.SlidingImageAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;
import static com.tokopedia.abstraction.constant.TkpdAppLink.DIGITAL_DEALS;

public class DealsHomeActivity extends BaseSimpleActivity implements HasComponent<DealsComponent>, DealsContract.View, View.OnClickListener {

    private final int SPAN_COUNT_4 = 4;
    private Menu mMenu;
    private DealsComponent mdealsComponent;
    @Inject
    public DealsHomePresenter mPresenter;
    private TouchViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;
    private CoordinatorLayout mainContent;
    private View progressBarLayout;
    private ProgressBar progBar;
    private RecyclerView recyclerViewCatItems;
    private RecyclerView recyclerViewAllDeals;
    private RecyclerView recyclerViewTrendingDeals;
    private RecyclerView recyclerViewBrandItems;
    private CoordinatorLayout baseMainContent;
    private int mBannnerPos;
    private final static String THEMEPARK = "themepark";
    private final static String TOP = "top";
    private LinearLayout searchInputView;
    private final boolean IS_SHORT_LAYOUT = false;
    public static final int REQUEST_CODE_DEALSLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_DEALSSEARCHACTIVITY = 102;
    private ConstraintLayout clSearch;
    private ConstraintLayout clLocation;
    private TextView locationName;
    private LinearLayoutManager layoutManager;
    private String category = null;
    private TextView seeAllBrands;

    @DeepLink({DIGITAL_DEALS})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        Intent destination;
        try {
            String deepLink = extras.getString(DeepLink.URI);
            Uri.Builder uri = Uri.parse(deepLink).buildUpon();
            destination = new Intent(context, DealsHomeActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);

        } catch (Exception e) {
            destination = new Intent(context, DealsHomeActivity.class);
        }
        return destination;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_deals_home;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpVariables();
        toolbar.setBackgroundResource(R.color.white);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        checkLocationStatus();

    }

    private void checkLocationStatus() {

        LocationViewModel location = Utils.getSingletonInstance().getLocation(getActivity());

        if (location != null) {
            locationName.setText(location.getName());
            mPresenter.getDealsList();
            mPresenter.getBrandsList();

        } else {
            navigateToActivityRequest(new Intent(getActivity(), DealsLocationActivity.class), REQUEST_CODE_DEALSLOCATIONACTIVITY);
        }

    }

    private void setUpVariables() {
        recyclerViewCatItems = findViewById(R.id.recyclerViewCatItems);
        recyclerViewAllDeals = findViewById(R.id.recyclerViewAllDeals);
        recyclerViewBrandItems = findViewById(R.id.recyclerViewBrandItems);
        recyclerViewTrendingDeals = findViewById(R.id.recyclerViewTrendingDeals);
        viewPager = findViewById(R.id.deals_bannerpager);
        circlePageIndicator = findViewById(R.id.pager_indicator);
        mainContent = findViewById(R.id.main_content);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        progBar = findViewById(R.id.prog_bar);
        baseMainContent = findViewById(R.id.base_main_content);
        searchInputView = findViewById(R.id.search_input_view);
        locationName = findViewById(R.id.tv_location_name);
        clLocation = findViewById(R.id.cl_location);
        clSearch = findViewById(R.id.cl_search_view);
        seeAllBrands=findViewById(R.id.tv_see_all_brands);
        seeAllBrands.setOnClickListener(this);
        searchInputView.setOnClickListener(this);
        clLocation.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerViewTrendingDeals.setLayoutManager(layoutManager);
        recyclerViewCatItems.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT_4,
                GridLayoutManager.VERTICAL, false));
        recyclerViewBrandItems.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT_4,
                GridLayoutManager.VERTICAL, false));
    }

    private void initInjector() {
        mdealsComponent = DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(this))
                .build();
    }

    private void executeInjector() {
        if (mdealsComponent == null) initInjector();
        mdealsComponent.inject(this);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public DealsComponent getComponent() {
        if (mdealsComponent == null) initInjector();
        return mdealsComponent;
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
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_DEALSLOCATIONACTIVITY:
                LocationViewModel location = Utils.getSingletonInstance().getLocation(getActivity());
                if (location == null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.select_location_first), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    locationName.setText(location.getName());
                    mPresenter.getDealsList();
                    mPresenter.getBrandsList();
                }
                break;

            case REQUEST_CODE_DEALSSEARCHACTIVITY:
                if (resultCode == RESULT_OK) {
                    LocationViewModel location1 = Utils.getSingletonInstance().getLocation(getActivity());
                    locationName.setText(location1.getName());
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void renderCategoryList(List<CategoryViewModel> categoryList, CategoryViewModel carousel, CategoryViewModel top) {

        if (top.getItems() != null) {
            DealsCategoryAdapter categoryAdapter = new DealsCategoryAdapter(getActivity(), top.getItems(), IS_SHORT_LAYOUT);
//            recyclerViewAllDeals.setAdapter(categoryAdapter);
            recyclerViewTrendingDeals.setAdapter(categoryAdapter);
        }
        if (carousel.getItems() != null) {
            setViewPagerListener(new SlidingImageAdapter(getActivity(), mPresenter.getCarouselImages(carousel.getItems()), mPresenter));
            circlePageIndicator.setViewPager(viewPager);
            mPresenter.startBannerSlide(viewPager);
        }
        if (categoryList != null) {
            recyclerViewCatItems.setAdapter(new DealsCategoryItemAdapter(getActivity(), categoryList));
        }

    }

    @Override
    public void renderBrandList(List<BrandViewModel> brandList) {
        if (brandList != null) {
            recyclerViewBrandItems.setAdapter(new DealsBrandAdapter(getActivity(), brandList, true));
        }
    }

    @Override
    public void addDealsToCards(CategoryViewModel top) {
        if (top.getItems() != null) {

            ((DealsCategoryAdapter) recyclerViewTrendingDeals.getAdapter()).addAll(top.getItems());
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void removeFooter() {
        ((DealsCategoryAdapter) recyclerViewTrendingDeals.getAdapter()).removeFooter();
//        ((DealsCategoryAdapter) recyclerViewAllDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) recyclerViewTrendingDeals.getAdapter()).addFooter();
//        ((DealsCategoryAdapter) recyclerViewAllDeals.getAdapter()).addFooter();
    }

    @Override
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
        clSearch.setVisibility(View.VISIBLE);
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
                // TODO Auto-generated method stub

            }
        });

        viewPager.setAdapter(adapter);
    }

    @Override
    public RequestParams getParams() {
        LocationViewModel location = Utils.getSingletonInstance().getLocation(getActivity());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(mPresenter.TAG, location.getSearchName());
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
        item.setEnabled(false);
    }

    @Override
    public void showFavouriteButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_favourite);
        item.setVisible(true);
        item.setEnabled(true);
    }

    @Override
    public void startGeneralWebView(String url) {
        if (getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getApplication())
                    .actionOpenGeneralWebView(getActivity(), url);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deals_home, menu);
        mMenu = menu;
        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(s);
        }
        return super.onCreateOptionsMenu(menu);
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

}
