package com.tokopedia.feedplus.view.adapter;

import static com.tokopedia.feedplus.data.pojo.FeedTabs.KEY_TRENDING;
import static com.tokopedia.feedplus.data.pojo.FeedTabs.TYPE_CUSTOM;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.tokopedia.explore.view.fragment.ContentExploreFragment;
import com.tokopedia.feedplus.data.pojo.FeedTabs;
import com.tokopedia.feedplus.view.fragment.DynamicFeedFragment;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.videoTabComponent.view.VideoTabFragment;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author by milhamj on 09/08/18.
 */

public class FeedPlusTabAdapter extends FragmentStatePagerAdapter {

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
        if (data.getType().equals(FeedTabs.TYPE_FEEDS)){
            return FeedPlusFragment.Companion.newInstance(bundle);
        } else if (data.getType().equals(FeedTabs.TYPE_EXPLORE)){
            return ContentExploreFragment.newInstance(bundle);
        } else if (data.getType().equals(TYPE_CUSTOM) && data.getKey().equals(KEY_TRENDING)) {
            return DynamicFeedFragment.Companion.newInstance(data.getKey());
        }  else if (data.getType().equals(TYPE_CUSTOM) && data.getKey().equals(FeedTabs.TYPE_VIDEO)) {
            return VideoTabFragment.newInstance(bundle);
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

    public void setItemList(List<FeedTabs.FeedData> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public Fragment getRegisteredFragment(int pos){
        return registeredFragment.get(pos);
    }

    @Nullable
    public ContentExploreFragment getContentExplore() {
        int index = getContentExploreIndex();
        if (getRegisteredFragment(index) instanceof ContentExploreFragment) {
            return (ContentExploreFragment) getRegisteredFragment(index);
        }
        return null;
    }

    public int getContentExploreIndex() {
        for (int i = 0; i < getCount(); ++i) {
            if (itemList.get(i).getType().equals(FeedTabs.TYPE_EXPLORE)) {
                return i;
            }
        }
        return 0;
    }

    public boolean isContextExploreExist() {
        return getContentExplore() != null;
    }
}
