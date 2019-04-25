package com.tokopedia.tokopoints.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tokopedia.tokopoints.view.fragment.CouponListingStackedFragment;
import com.tokopedia.tokopoints.view.fragment.MyCouponListingFragment;
import com.tokopedia.tokopoints.view.model.CouponFilterItem;

import java.util.List;

public class StackedCouponFilterPagerAdapter extends FragmentStatePagerAdapter {

    private List<CouponFilterItem> mItems;
    private SparseArray<Fragment> mrRegisteredFragments = new SparseArray<>();

    public StackedCouponFilterPagerAdapter(FragmentManager fm, List<CouponFilterItem> items) {
        super(fm);
        this.mItems = items;
    }

    @Override
    public Fragment getItem(int position) {
        return CouponListingStackedFragment.newInstance();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).getName();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mrRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mrRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mrRegisteredFragments.get(position);
    }
}
