package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.adapter.BrandsFragmentPagerAdapter;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.model.CategoriesModel;

import java.util.List;

public class AllBrandsActivity extends DealsBaseActivity implements SearchInputView.Listener{

    private ViewPager categoryViewPager;
    private TabLayout tabs;
    private BrandsFragmentPagerAdapter brandsTabsPagerAdapter;
    public static final String EXTRA_LIST = "list";
    private TextView toolbarTitle;
    private SearchInputView searchInputView;

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
        toolbarTitle = findViewById(R.id.toolbar_title);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_brand));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_17));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        categoryViewPager = findViewById(R.id.container);
        List<CategoriesModel> categoryList = getIntent().getParcelableArrayListExtra(EXTRA_LIST);
        brandsTabsPagerAdapter =
                new BrandsFragmentPagerAdapter(getSupportFragmentManager(), categoryList);
        setCategoryViewPagerListener();
        categoryViewPager.setAdapter(brandsTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setOffscreenPageLimit(1);
        categoryViewPager.setSaveFromParentEnabled(false);
        searchInputView.setListener(this);
        KeyboardHandler.DropKeyboard(this, searchInputView);

    }

    private void setCategoryViewPagerListener() {
        categoryViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(categoryViewPager));
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onSearchSubmitted(String searchText) {
        Fragment fragment=brandsTabsPagerAdapter.getSelectedFragment(categoryViewPager.getCurrentItem());
        if(fragment instanceof AllBrandsFragment){
            AllBrandsFragment allBrandsFragment=(AllBrandsFragment)fragment;
            allBrandsFragment.onSearchSubmitted(searchText);
        }
    }

    @Override
    public void onSearchTextChanged(String searchText) {
        Fragment fragment=brandsTabsPagerAdapter.getSelectedFragment(categoryViewPager.getCurrentItem());
        if(fragment instanceof AllBrandsFragment){
            AllBrandsFragment allBrandsFragment=(AllBrandsFragment)fragment;
            allBrandsFragment.onSearchTextChanged(searchText);
        }
    }
}
