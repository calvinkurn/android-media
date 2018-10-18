package com.tokopedia.digital.newcart.presentation.fragment.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDealsListFragment;

import java.util.List;

public class DigitalDealsPagerAdapter extends FragmentPagerAdapter {

    private final List<DealCategoryViewModel> dealCategoryViewModels;

    public DigitalDealsPagerAdapter(List<DealCategoryViewModel> dealCategoryViewModels, FragmentManager fm) {
        super(fm);
        this.dealCategoryViewModels = dealCategoryViewModels;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return dealCategoryViewModels.get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {
        return DigitalCartDealsListFragment.newInstance(dealCategoryViewModels.get(position).getUrl());
    }

    @Override
    public int getCount() {
        return dealCategoryViewModels.size();
    }
}
