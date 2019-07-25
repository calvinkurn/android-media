package com.tokopedia.contactus.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.home.view.fragment.BuyerPurchaseFragment;
import com.tokopedia.contactus.home.view.fragment.SellerPurchaseFragment;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;

/**
 * Created by sandeepgoyal on 11/04/18.
 */

public class BuyerPurchaseListActivity extends BaseSimpleActivity {

    TabLayout tab;
    TouchViewPager viewPagerList;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, BuyerPurchaseListActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_full_order_list_activity;
    }

    ViewPagerAdapter adapter;

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tab = findViewById(R.id.tab);
        viewPagerList = findViewById(R.id.view_pager_list);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager();
        viewPagerList.setOffscreenPageLimit(2);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerList.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPagerList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.setupWithViewPager(viewPagerList);

        viewPagerList.setCurrentItem(0);

    }

    private void setupViewPager() {
        if (SessionHandler.isUserHasShop(this)) {
            adapter.addFragment(BuyerPurchaseFragment.newInstance(), getString(R.string.pembelian));
            adapter.addFragment(SellerPurchaseFragment.newInstance(), getString(R.string.penjualan));
        } else {
            tab.setVisibility(View.GONE);
            adapter.addFragment(BuyerPurchaseFragment.newInstance(), getString(R.string.pembelian));
        }
        viewPagerList.setAdapter(adapter);
    }

    protected class ViewPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public ArrayList<Fragment> getFragments() {
            return fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
