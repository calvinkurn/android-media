package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.BrandsFragmentPagerAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.AllBrandsHomeContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.fragment.SelectLocationBottomSheet;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.AllCategoryPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AllBrandsActivity extends DealsBaseActivity implements AllBrandsHomeContract.View, HasComponent<DealsComponent>, SearchInputView.Listener, View.OnClickListener, AllBrandsFragment.UpdateLocation, PopupMenu.OnMenuItemClickListener, SelectLocationBottomSheet.CloseSelectLocationBottomSheet, DealsLocationAdapter.ActionListener {

    public static final String EXTRA_CATEGOTRY_LIST = "category_item_list";
    private ViewPager categoryViewPager;
    private TabLayout tabs;
    private BrandsFragmentPagerAdapter brandsTabsPagerAdapter;
    public static final String EXTRA_LIST = "list";
    public static final String SEARCH_TEXT = "search_text";
    private TextView toolbarTitle;
    private SearchInputView searchInputView;
    private ImageView backArrow, overFlowIcon;
    AllBrandsFragment allBrandsFragment;
    CoordinatorLayout mainContent;
    DealsAnalytics dealsAnalytics;
    List<CategoriesModel> categoryList = new ArrayList<>();
    private String searchText;
    private UserSession userSession;
    private Menu mMenu;
    private int categoryId;
    private int position = 0;
    @Inject
    AllCategoryPresenter mPresenter;
    CloseableBottomSheetDialog selectLocationFragment;

    public static final String FROM_VOUCHER = "isVoucher";


    @DeepLink({DealsUrl.AppLink.DIGITAL_DEALS_ALL_BRAND})
    public static TaskStackBuilder getAllBrandsStaticIntent(Context context, Bundle extras) {
        Intent destination;

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = ((DealsModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(new Intent(context, DealsHomeActivity.class));

        if (extras != null) {
            String deepLink = extras.getString(DeepLink.URI);
            Uri.Builder uri = Uri.parse(deepLink).buildUpon();


            extras.putString(FROM_VOUCHER, extras.getString(FROM_VOUCHER));
            destination = new Intent(context, AllBrandsActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);

            taskStackBuilder.addNextIntent(destination);
        }
        return taskStackBuilder;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_all_brands;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        overridePendingTransition(R.anim.slide_in_left_brands, R.anim.slide_out_right_brands);
        userSession = new UserSession(this);
        dealsAnalytics = new DealsAnalytics();
        setUpVariables();
    }

    private void setUpVariables() {
        mainContent = findViewById(R.id.main_content);
        tabs = findViewById(R.id.tabs);
        searchInputView = findViewById(R.id.search_input_view);
        backArrow = findViewById(R.id.backArraw);
        overFlowIcon = findViewById(R.id.overFlow_icon);
        toolbarTitle = findViewById(R.id.tv_location_name);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_brand));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_17));
        searchInputView.setSearchTextColor(ContextCompat.getColor(this, R.color.clr_ae31353b));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        searchInputView.setListener(this);
        categoryViewPager = findViewById(R.id.container);
        searchText = getIntent().getStringExtra(SEARCH_TEXT);
        if (!TextUtils.isEmpty(searchText)) {
            searchInputView.setSearchText(searchText);
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra(FROM_VOUCHER))) {
            toolbarTitle.setVisibility(View.GONE);
            mPresenter.attachView(this);
            mPresenter.getAllCategories();
        } else {
            categoryList = getIntent().getParcelableArrayListExtra(EXTRA_LIST);
            toolbarTitle.setOnClickListener(this);
            Location location = Utils.getSingletonInstance().getLocation(this);
            if (location != null) {
                toolbarTitle.setText(location.getName());
            }

            if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("cat_id"))) {
                categoryId = Integer.parseInt(getIntent().getStringExtra("cat_id"));
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryId == categoryList.get(i).getCategoryId()) {
                        position = i;
                    }
                }
            }
            brandsTabsPagerAdapter =
                    new BrandsFragmentPagerAdapter(getSupportFragmentManager(), categoryList, searchInputView.getSearchText());
            categoryViewPager.setAdapter(brandsTabsPagerAdapter);
            tabs.setupWithViewPager(categoryViewPager);
            categoryViewPager.setOffscreenPageLimit(1);
            categoryViewPager.setCurrentItem(position);
            categoryViewPager.setSaveFromParentEnabled(false);


            KeyboardHandler.DropKeyboard(this, searchInputView);
            Fragment fragment = brandsTabsPagerAdapter.getSelectedFragment(categoryViewPager.getCurrentItem());
            if (fragment instanceof AllBrandsFragment) {
                allBrandsFragment = (AllBrandsFragment) fragment;
            }

            setCategoryViewPagerListener();
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        overFlowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AllBrandsActivity.this, v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_deals_home, popupMenu.getMenu());
                mMenu = popupMenu.getMenu();
                for (int i = 0; i < mMenu.size(); i++) {
                    MenuItem item = popupMenu.getMenu().getItem(i);
                    SpannableString s = new SpannableString(item.getTitle());
                    s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
                    s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    item.setTitle(s);
                }
                popupMenu.setOnMenuItemClickListener(AllBrandsActivity.this);
                popupMenu.show();
            }
        });
    }

    private void setCategoryViewPagerListener() {
        categoryViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(categoryViewPager));
        categoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dealsAnalytics.sendSelectedHeaderEvent(categoryList.get(position).getTitle());
                refetchData(searchText, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onSearchSubmitted(String searchText) {
        this.searchText = searchText;
        refetchData(searchText, categoryViewPager.getCurrentItem());
    }

    @Override
    public void onSearchTextChanged(String searchText) {
        this.searchText = searchText;
        refetchData(searchText, categoryViewPager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_location_name) {
            allBrandsFragment.getLocations(toolbarTitle.getText().toString());
        }
    }

    @Override
    public void startLocationFragment(List<Location> locations) {
        selectLocationFragment = CloseableBottomSheetDialog.createInstanceRounded(this);
        selectLocationFragment.setCustomContentView(new SelectLocationBottomSheet(this, false, locations, this, toolbarTitle.getText().toString(), this), "", false);
        selectLocationFragment.show();
    }

    private void refetchData(String searchText, int position) {
        Fragment fragment = brandsTabsPagerAdapter.getSelectedFragment(position);
        if (fragment instanceof AllBrandsFragment) {
            AllBrandsFragment allBrandsFragment = (AllBrandsFragment) fragment;
            allBrandsFragment.onSearchSubmitted(searchText);
            allBrandsFragment.reloadIfLocationUpdated();
        }
    }

    private AllBrandsFragment getCurrentSelectedFragment() {
        Fragment fragment = brandsTabsPagerAdapter.getSelectedFragment(categoryViewPager.getCurrentItem());
        if (fragment instanceof AllBrandsFragment) {
            return (AllBrandsFragment) fragment;

        }
        return null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_promo) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_PROMO,
                    "");
            ((DealsModuleRouter) this.getApplication())
                    .actionOpenGeneralWebView(this, DealsUrl.WebUrl.PROMOURL);
        } else if (id == R.id.action_booked_history) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_DAFTAR_TRANSAKSI,
                    "");
            if (userSession.isLoggedIn()) {
                RouteManager.route(this, ApplinkConst.DEALS_ORDER);
            } else {
                Intent intent = ((DealsModuleRouter) this.getApplication()).
                        getLoginIntent(this);
                startActivity(intent);
            }
        } else if (id == R.id.action_faq) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_BANTUAN,
                    "");
            ((DealsModuleRouter) this.getApplication())
                    .actionOpenGeneralWebView(this, DealsUrl.WebUrl.FAQURL);
        }
        return true;
    }

    @Override
    public void closeBottomsheet() {
        if (selectLocationFragment != null) {
            selectLocationFragment.dismiss();
        }
    }

    @Override
    public void onLocationItemSelected(boolean locationUpdated) {
        Location location = Utils.getSingletonInstance().getLocation(this);
        if (locationUpdated && location != null) {
            toolbarTitle.setText(location.getName());
        }
        AllBrandsFragment allBrandsFragment = getCurrentSelectedFragment();
        if (allBrandsFragment != null && locationUpdated) {
            allBrandsFragment.onLocationUpdated();
        }
        if (selectLocationFragment != null) {
            selectLocationFragment.dismiss();
        }
    }


    @Override
    public DealsComponent getComponent() {
        return DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void renderCategoryList(List<CategoryItem> categoryItems) {
        for (int i = 0; i < categoryItems.size() - 1; i++) {
            if (categoryItems.get(i).getIsCard() != 1) {
                CategoriesModel categoriesModel = new CategoriesModel();
                categoriesModel.setName(categoryItems.get(i).getName());
                categoriesModel.setTitle(categoryItems.get(i).getTitle());
                categoriesModel.setCategoryUrl(categoryItems.get(i).getCategoryUrl());
                categoriesModel.setPosition(i);
                categoriesModel.setCategoryId(categoryItems.get(i).getCategoryId());
                categoriesModel.setItems(categoryItems.get(i).getItems());
                categoryList.add(categoriesModel);
            }
        }

        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("cat_id"))) {
            categoryId = Integer.parseInt(getIntent().getStringExtra("cat_id"));
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryId == categoryList.get(i).getCategoryId()) {
                    position = i;
                }
            }
        }
        brandsTabsPagerAdapter =
                new BrandsFragmentPagerAdapter(getSupportFragmentManager(), categoryList, searchInputView.getSearchText());
        categoryViewPager.setAdapter(brandsTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setOffscreenPageLimit(1);
        categoryViewPager.setCurrentItem(position);
        categoryViewPager.setSaveFromParentEnabled(false);


        KeyboardHandler.DropKeyboard(this, searchInputView);
        Fragment fragment = brandsTabsPagerAdapter.getSelectedFragment(categoryViewPager.getCurrentItem());
        if (fragment instanceof AllBrandsFragment) {
            allBrandsFragment = (AllBrandsFragment) fragment;
        }

        setCategoryViewPagerListener();
    }
}
