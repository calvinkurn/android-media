package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.BrandsFragmentPagerAdapter;
import com.tokopedia.digital_deals.view.contractor.AllBrandsHomeContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.fragment.SelectLocationBottomSheet;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.AllCategoryPresenter;
import com.tokopedia.digital_deals.view.utils.CurrentLocationCallBack;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AllBrandsActivity extends DealsBaseActivity implements AllBrandsHomeContract.View, HasComponent<DealsComponent>, SearchInputView.Listener, SearchInputView.FocusChangeListener, View.OnClickListener, AllBrandsFragment.UpdateLocation, PopupMenu.OnMenuItemClickListener, SelectLocationBottomSheet.SelectedLocationListener, CurrentLocationCallBack {

    public static final String EXTRA_CATEGOTRY_LIST = "category_item_list";
    private static final String ALL_BRANDS = "AllBrandsActivity";
    private ViewPager categoryViewPager;
    private TabLayout tabs;
    private BrandsFragmentPagerAdapter brandsTabsPagerAdapter;
    public static final String EXTRA_LIST = "list";
    public static final String SEARCH_TEXT = "search_text";
    private TextView toolbarTitle;
    private SearchInputView searchInputView;
    private ImageView backArrow, overFlowIcon;
    private TextView title;
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
    private boolean isLocationUpdated;
    SelectLocationBottomSheet selectLocationFragment;
    PermissionCheckerHelper permissionCheckerHelper;
    String fromVoucher;

    @Override
    protected int getLayoutRes() {
        return com.tokopedia.digital_deals.R.layout.activity_all_brands;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> params = UriUtil.destructureUri(ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS, uri, true);
            fromVoucher = params.get(0);
            permissionCheckerHelper = new PermissionCheckerHelper();
            checkForCurrentLocation();
        }
        userSession = new UserSession(this);
        dealsAnalytics = new DealsAnalytics();
        if (userSession.isLoggedIn()) {
            mPresenter.sendNSQEvent(userSession.getUserId(), "allbrand-page");
        }
        setUpVariables();
    }

    private void setUpVariables() {
        title = findViewById(com.tokopedia.digital_deals.R.id.toolbar_title);
        mainContent = findViewById(com.tokopedia.digital_deals.R.id.main_content);
        tabs = findViewById(com.tokopedia.digital_deals.R.id.tabs);
        searchInputView = findViewById(com.tokopedia.digital_deals.R.id.search_input_view);
        backArrow = findViewById(com.tokopedia.digital_deals.R.id.backArraw);
        overFlowIcon = findViewById(com.tokopedia.digital_deals.R.id.overFlow_icon);
        toolbarTitle = findViewById(com.tokopedia.digital_deals.R.id.tv_location_name);
        searchInputView.setSearchHint(getResources().getString(com.tokopedia.digital_deals.R.string.search_input_hint_brand));
        searchInputView.setSearchTextSize(getResources().getDimension(com.tokopedia.design.R.dimen.sp_17));
        searchInputView.setSearchTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.clr_ae31353b));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_24), getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_24));
        searchInputView.setListener(this);
        searchInputView.setFocusChangeListener(this);
        categoryViewPager = findViewById(com.tokopedia.digital_deals.R.id.container);
        searchText = getIntent().getStringExtra(SEARCH_TEXT);
        if (!TextUtils.isEmpty(searchText)) {
            searchInputView.setSearchText(searchText);
        }
        if (!TextUtils.isEmpty(fromVoucher)) {
            title.setText(getResources().getString(com.tokopedia.digital_deals.R.string.voucher));
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            title.setTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.clr_f531353b));
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
                menuInflater.inflate(com.tokopedia.digital_deals.R.menu.menu_deals_home, popupMenu.getMenu());
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
        if (v.getId() == com.tokopedia.digital_deals.R.id.tv_location_name) {
            allBrandsFragment.getLocations(toolbarTitle.getText().toString());
        }
    }

    @Override
    public void startLocationFragment(List<Location> locations) {
        Location location = Utils.getSingletonInstance().getLocation(this);
        Fragment fragment = SelectLocationBottomSheet.createInstance(toolbarTitle.getText().toString(), location);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(com.tokopedia.digital_deals.R.anim.deals_slide_in_up, com.tokopedia.digital_deals.R.anim.deals_slide_in_down,
                com.tokopedia.digital_deals.R.anim.deals_slide_out_down, com.tokopedia.digital_deals.R.anim.deals_slide_out_up)
                .add(com.tokopedia.digital_deals.R.id.main_content, fragment).addToBackStack(ALL_BRANDS).commit();
    }

    @Override
    public void updateLocationName(String locationName) {
        Location location = Utils.getSingletonInstance().getLocation(this);
        if (!TextUtils.isEmpty(locationName)) {
            toolbarTitle.setText(location.getName());
        }
    }

    private void refetchData(String searchText, int position) {
        Fragment fragment = brandsTabsPagerAdapter.getSelectedFragment(position);
        Location location = Utils.getSingletonInstance().getLocation(this);
        toolbarTitle.setText(location.getName());
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
        if (id == com.tokopedia.digital_deals.R.id.action_promo) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_PROMO,
                    "");
            RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, DealsUrl.WebUrl.PROMOURL);
        } else if (id == com.tokopedia.digital_deals.R.id.action_booked_history) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_DAFTAR_TRANSAKSI,
                    "");
            if (userSession.isLoggedIn()) {
                RouteManager.route(this, ApplinkConst.DEALS_ORDER);
            } else {
                RouteManager.route(this, ApplinkConst.LOGIN);
            }
        } else if (id == com.tokopedia.digital_deals.R.id.action_faq) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_BANTUAN,
                    "");
            RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, DealsUrl.WebUrl.FAQURL);
        }
        return true;
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
        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.setCategoryUrl("");
        categoriesModel.setTitle(getResources().getString(com.tokopedia.digital_deals.R.string.all_brands));
        categoriesModel.setName(getResources().getString(com.tokopedia.digital_deals.R.string.all_brands));
        categoriesModel.setPosition(0);
        categoryList.add(0, categoriesModel);


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

    @Override
    public void onLocationItemUpdated(boolean isLocationUpdated) {
        Location location = Utils.getSingletonInstance().getLocation(this);
        if (isLocationUpdated && location != null) {
            toolbarTitle.setText(location.getName());
        }
        AllBrandsFragment allBrandsFragment = getCurrentSelectedFragment();
        if (allBrandsFragment != null && isLocationUpdated) {
            allBrandsFragment.onLocationUpdated();
        }
    }

    @Override
    public void setDefaultLocationOnHomePage() {
        AllBrandsFragment allBrandsFragment = getCurrentSelectedFragment();
        if (allBrandsFragment != null && isLocationUpdated) {
            allBrandsFragment.setDefaultLocation();
        }
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {

    }

    @Override
    public void setCurrentLocation(DeviceLocation deviceLocation) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(this, TkpdCache.DEALS_LOCATION);
        localCacheHandler.putString(Utils.KEY_LOCATION_LAT, String.valueOf(deviceLocation.getLatitude()));
        localCacheHandler.putString(Utils.KEY_LOCATION_LONG, String.valueOf(deviceLocation.getLongitude()));
        localCacheHandler.applyEditor();
        allBrandsFragment.setCurrentCoordinates();
    }

    private void checkForCurrentLocation() {
        permissionCheckerHelper.checkPermission(this, PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION, new PermissionCheckerHelper.PermissionCheckListener() {
            @Override
            public void onPermissionDenied(String permissionText) {
                setDefaultLocationOnHomePage();
            }

            @Override
            public void onNeverAskAgain(String permissionText) {
            }

            @Override
            public void onPermissionGranted() {
                Utils.getSingletonInstance().detectAndSendLocation(getActivity(), permissionCheckerHelper, AllBrandsActivity.this);
            }
        }, getResources().getString(com.tokopedia.digital_deals.R.string.deals_use_current_location));
    }
}
