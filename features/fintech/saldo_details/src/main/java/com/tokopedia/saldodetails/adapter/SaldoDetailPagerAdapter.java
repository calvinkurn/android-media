package com.tokopedia.saldodetails.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.saldodetails.view.ui.SaldoTabItem;

import java.util.ArrayList;
import java.util.List;

public class SaldoDetailPagerAdapter extends FragmentPagerAdapter {

    private List<SaldoTabItem> items;

    public SaldoDetailPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.items = new ArrayList<>();
    }

    public void setItems(List<SaldoTabItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position).getFragment();
    }
}
