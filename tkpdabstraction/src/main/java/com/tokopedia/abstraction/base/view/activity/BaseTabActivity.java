package com.tokopedia.abstraction.base.view.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tokopedia.abstraction.R;

/**
 * Created by nathan on 7/11/17.
 */

public abstract class BaseTabActivity extends BaseToolbarActivity {

    protected ViewPager viewPager;
    protected TabLayout tabLayout;

    protected abstract PagerAdapter getViewPagerAdapter();

    protected abstract int getPageLimit();

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        viewPager = (ViewPager) findViewById(getViewPagerResourceId());
        tabLayout = (TabLayout) findViewById(getTabLayoutResourceId());
        viewPager.setOffscreenPageLimit(getPageLimit());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        viewPager.setAdapter(getViewPagerAdapter());
    }

    protected int getViewPagerResourceId() {
        return R.id.pager;
    }

    protected int getTabLayoutResourceId() {
        return R.id.indicator;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_tab;
    }
}
