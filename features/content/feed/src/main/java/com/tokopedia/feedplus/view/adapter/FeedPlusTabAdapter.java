package com.tokopedia.feedplus.view.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.tokopedia.explore.view.fragment.ContentExploreFragment;
import com.tokopedia.feedplus.data.pojo.FeedTabs;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;

import java.util.List;

/**
 * @author by milhamj on 09/08/18.
 */

public class FeedPlusTabAdapter extends FragmentStatePagerAdapter {
    private static final String TYPE_FEEDS = "feeds";
    private static final String TYPE_EXPLORE = "explore";

    private List<FeedTabs.FeedData> itemList;
    private Bundle bundle;
    private SparseArrayCompat<Fragment> registeredFragment = new SparseArrayCompat<>();

    public FeedPlusTabAdapter(FragmentManager fm, List<FeedTabs.FeedData> itemList, Bundle bundle){
        super(fm);
        this.itemList = itemList;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        FeedTabs.FeedData data = itemList.get(position);
        if (data.getType().equals(TYPE_FEEDS)){
            return FeedPlusFragment.newInstance(bundle);
        } else if (data.getType().equals(TYPE_EXPLORE)){
            return ContentExploreFragment.newInstance(bundle);
        } else {
            /* Will be override for next to handle custom tab */
            return new Fragment();
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return itemList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        registeredFragment.put(position, f);
        return f;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragment.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int pos){
        return registeredFragment.get(pos);
    }

    public ContentExploreFragment getContentExplorer() {
        for (int i = 0; i < getCount(); ++i){
            if (itemList.get(i).getType().equals(TYPE_EXPLORE)){
                return (ContentExploreFragment) getRegisteredFragment(i);
            }
        }
        return null;
    }
}
