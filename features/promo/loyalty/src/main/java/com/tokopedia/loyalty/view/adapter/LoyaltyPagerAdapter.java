package com.tokopedia.loyalty.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tokopedia.loyalty.view.data.LoyaltyPagerItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 30/11/17.
 */

public class LoyaltyPagerAdapter extends FragmentStatePagerAdapter {

    List<LoyaltyPagerItem> loyaltyPagerItemList = new ArrayList<>();

    @Inject
    public LoyaltyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return loyaltyPagerItemList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return loyaltyPagerItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return loyaltyPagerItemList.get(position).getTabTitle();
    }

    public void addAllItem(List<LoyaltyPagerItem> loyaltyPagerItemList) {
        this.loyaltyPagerItemList.addAll(loyaltyPagerItemList);
        notifyDataSetChanged();
    }
}
