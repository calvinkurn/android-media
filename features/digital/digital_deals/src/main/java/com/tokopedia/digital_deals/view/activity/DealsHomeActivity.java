package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryItemAdapter;
import com.tokopedia.digital_deals.view.adapter.SlidingImageAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.abstraction.constant.TkpdAppLink.DIGITAL_DEALS;

public class DealsHomeActivity extends BaseSimpleActivity implements HasComponent<DealsComponent>, DealsContract.View {

    private Menu mMenu;
    DealsComponent mdealsComponent;
    private Context context;

    @Inject
    public DealsHomePresenter mPresenter;

    private TouchViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;

    private FrameLayout mainContent;

    private View progressBarLayout;
    private ProgressBar progBar;

    private RecyclerView recyclerViewCatItems;
    private RecyclerView recyclerView;
    private CoordinatorLayout baseMainContent;
    private int mBannnerPos;
    private final static String THEMEPARK = "themepark";
    private final static String TOP = "top";


    public static final int REQUEST_CODE_EVENTLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_EVENTSEARCHACTIVITY = 901;

    public final static String EXTRA_SECTION = "extra_section";

    private SlidingImageAdapter adapter;

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
        context = this;

        toolbar.setBackgroundResource(R.color.white);

        setUpVariables();
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        mPresenter.getDealsList();
        Intent detailsIntent = new Intent(context, BrandOutletDetailsActivity.class);
        startActivity(detailsIntent);
    }

    private void setUpVariables() {
        recyclerViewCatItems = findViewById(R.id.recyclerViewCatItems);
        recyclerView = findViewById(R.id.recyclerView);
        viewPager = findViewById(R.id.deals_bannerpager);
        circlePageIndicator = findViewById(R.id.pager_indicator);
        mainContent = findViewById(R.id.main_content);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        progBar = findViewById(R.id.prog_bar);
        baseMainContent = findViewById(R.id.base_main_content);
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
    public void renderCategoryList(List<CategoryViewModel> categoryList) {
        DealsCategoryAdapter categoryAdapter;
        DealsCategoryItemAdapter categoryItemAdapter;

        if (categoryList.get(0).getItems() != null || categoryList.get(0).getItems().size() != 0) {
            if (categoryList.get(0).getName().equalsIgnoreCase("top")) {
                categoryAdapter = new DealsCategoryAdapter(context, categoryList.get(0).getItems());
                recyclerView.setAdapter(categoryAdapter);
            } else {
                adapter = new SlidingImageAdapter(context, mPresenter.getCarouselImages(categoryList.get(0).getItems()), mPresenter);
                setViewPagerListener();
                circlePageIndicator.setViewPager(viewPager);
                mPresenter.startBannerSlide(viewPager);
            }
        }

        if (categoryList.get(1).getItems() != null || categoryList.get(1).getItems().size() != 0) {
            if (categoryList.get(1).getName().equalsIgnoreCase("top")) {

            } else {
                categoryAdapter = new DealsCategoryAdapter(context, categoryList.get(1).getItems());
                recyclerView.setAdapter(categoryAdapter);
                adapter = new SlidingImageAdapter(context, mPresenter.getCarouselImages(categoryList.get(1).getItems()), mPresenter);
                setViewPagerListener();
                circlePageIndicator.setViewPager(viewPager);
                mPresenter.startBannerSlide(viewPager);
            }
        }
        categoryItemAdapter = new DealsCategoryItemAdapter(context, categoryList);
        recyclerViewCatItems.setAdapter(categoryItemAdapter);
        baseMainContent.setVisibility(View.VISIBLE);
    }


    private void setViewPagerListener() {

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
        return RequestParams.EMPTY;
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
    public void hideSearchButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_search);
        item.setVisible(false);
        item.setEnabled(false);
    }

    @Override
    public void showSearchButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_search);
        item.setVisible(true);
        item.setEnabled(true);
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

}
