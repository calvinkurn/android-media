package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.adapter.BrandsFragmentPagerAdapter;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.AllBrandsPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AllBrandsActivity extends DealsBaseActivity implements SearchInputView.Listener, View.OnClickListener, AllBrandsFragment.UpdateLocation {

    public static final String EXTRA_CATEGOTRY_LIST = "category_item_list";
    private ViewPager categoryViewPager;
    private TabLayout tabs;
    private BrandsFragmentPagerAdapter brandsTabsPagerAdapter;
    public static final String EXTRA_LIST = "list";
    public static final String SEARCH_TEXT = "search_text";
    private TextView toolbarTitle;
    private SearchInputView searchInputView;
    private ImageView backArrow;
    AllBrandsFragment allBrandsFragment;
    List<CategoriesModel> categoryList = new ArrayList<>();
    private String searchText;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_all_brands;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left_brands, R.anim.slide_out_right_brands);
        setUpVariables();
    }

    private void setUpVariables() {
        tabs = findViewById(R.id.tabs);
        searchInputView = findViewById(R.id.search_input_view);
        backArrow = findViewById(R.id.backArraw);
        toolbarTitle = findViewById(R.id.tv_location_name);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_brand));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_17));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        searchInputView.setListener(this);
        categoryViewPager = findViewById(R.id.container);
        categoryList = getIntent().getParcelableArrayListExtra(EXTRA_LIST);
        searchText = getIntent().getStringExtra(SEARCH_TEXT);
        if (!TextUtils.isEmpty(searchText)) {
            searchInputView.setSearchText(searchText);
        }
        brandsTabsPagerAdapter =
                new BrandsFragmentPagerAdapter(getSupportFragmentManager(), categoryList, searchInputView.getSearchText());
        setCategoryViewPagerListener();
        categoryViewPager.setAdapter(brandsTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setOffscreenPageLimit(1);
        categoryViewPager.setSaveFromParentEnabled(false);

        toolbarTitle.setOnClickListener(this);
        KeyboardHandler.DropKeyboard(this, searchInputView);

        Location location = Utils.getSingletonInstance().getLocation(this);
        if (location != null) {
            toolbarTitle.setText(location.getName());
        }
        Fragment fragment = brandsTabsPagerAdapter.getSelectedFragment(categoryViewPager.getCurrentItem());
        if (fragment instanceof AllBrandsFragment) {
            allBrandsFragment = (AllBrandsFragment) fragment;
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                startSearch(searchText, position);
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
        startSearch(searchText, categoryViewPager.getCurrentItem());
    }

    @Override
    public void onSearchTextChanged(String searchText) {
        this.searchText = searchText;
        startSearch(searchText, categoryViewPager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_location_name) {
            allBrandsFragment.getLocations(toolbarTitle.getText().toString());
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        if (location != null) {
            toolbarTitle.setText(location.getName());
        }
    }

    private void startSearch(String searchText, int position) {
        Fragment fragment = brandsTabsPagerAdapter.getSelectedFragment(position);
        if (fragment instanceof AllBrandsFragment) {
            AllBrandsFragment allBrandsFragment = (AllBrandsFragment) fragment;
            allBrandsFragment.onSearchSubmitted(searchText);
        }
    }
}
