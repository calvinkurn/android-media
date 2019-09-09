package com.tokopedia.feedplus.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tokopedia.feedplus.view.viewmodel.FeedPlusTabItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 09/08/18.
 */

public class FeedPlusTabAdapter extends FragmentPagerAdapter {

    private List<FeedPlusTabItem> itemList = new ArrayList<>();

    public FeedPlusTabAdapter(FragmentManager fm){
        super(fm);
    }

    public void setItemList(List<FeedPlusTabItem> itemList){
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void addItem(int position, FeedPlusTabItem item) {
        this.itemList.add(position, item);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return itemList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        int position = POSITION_NONE;
        for (int i = 0; i < itemList.size(); i++) {
            if (object.equals(itemList.get(i))) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return itemList.get(position).getTitle();
    }
}
