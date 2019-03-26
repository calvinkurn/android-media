package com.tokopedia.digital_deals.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.adapter.BrandsFragmentPagerAdapter;
import com.tokopedia.digital_deals.view.model.CategoriesModel;

import java.util.List;

public class AllBrandsActivity extends DealsBaseActivity {

    private ViewPager categoryViewPager;
    private TabLayout tabs;
    private BrandsFragmentPagerAdapter brandsTabsPagerAdapter;
    public static final String EXTRA_LIST = "list";

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_all_brands;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left_brands, R.anim.slide_out_right_brands);
        setUpVariables();
        updateTitle(getResources().getString(R.string.brands));
    }

    private void setUpVariables() {
        tabs = findViewById(R.id.tabs);
        categoryViewPager = findViewById(R.id.container);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        List<CategoriesModel> categoryList = getIntent().getParcelableArrayListExtra(EXTRA_LIST);
        brandsTabsPagerAdapter =
                new BrandsFragmentPagerAdapter(getSupportFragmentManager(), categoryList);
        setCategoryViewPagerListener();
        categoryViewPager.setAdapter(brandsTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setOffscreenPageLimit(1);
        categoryViewPager.setSaveFromParentEnabled(false);
    }

    private void setCategoryViewPagerListener() {
        categoryViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(categoryViewPager));
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

}
