package com.tokopedia.profile.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.profile.view.viewmodel.TopProfileSectionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvinatin on 21/02/18.
 */

public class TopProfileTabPagerAdapter extends FragmentPagerAdapter {

    private List<TopProfileSectionItem> topProfileSectionItemList = new ArrayList<>();

    public TopProfileTabPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public void setItemList(List<TopProfileSectionItem> topProfileSectionItemList){
        this.topProfileSectionItemList = topProfileSectionItemList;
        notifyDataSetChanged();
    }

    public void addItem(int position, TopProfileSectionItem topProfileSectionItem) {
        this.topProfileSectionItemList.add(position, topProfileSectionItem);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return topProfileSectionItemList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return topProfileSectionItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return topProfileSectionItemList.get(position).getTitle();
    }
}
