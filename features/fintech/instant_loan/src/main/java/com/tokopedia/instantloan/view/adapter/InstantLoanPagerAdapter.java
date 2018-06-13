package com.tokopedia.instantloan.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.instantloan.view.ui.InstantLoanItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachinbansal on 6/12/18.
 */

public class InstantLoanPagerAdapter extends FragmentPagerAdapter {

    private List<InstantLoanItem> searchSectionItemList = new ArrayList<>();

    public InstantLoanPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<InstantLoanItem> searchSectionItemList) {
        this.searchSectionItemList = searchSectionItemList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return searchSectionItemList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return searchSectionItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return searchSectionItemList.get(position).getTitle();
    }
}
