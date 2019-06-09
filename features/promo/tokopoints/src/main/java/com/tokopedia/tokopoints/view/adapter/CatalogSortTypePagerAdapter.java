package com.tokopedia.tokopoints.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tokopedia.tokopoints.view.fragment.CatalogListItemFragment;
import com.tokopedia.tokopoints.view.model.CatalogSubCategory;

import java.util.ArrayList;
import java.util.List;

public class CatalogSortTypePagerAdapter extends FragmentStatePagerAdapter {

    private final int categoryId;
    private boolean isPointsAvailable;
    private List<CatalogSubCategory> mItems;
    private SparseArray<Fragment> mrRegisteredFragments = new SparseArray<>();

    public CatalogSortTypePagerAdapter(FragmentManager fm, int categoryId, List<CatalogSubCategory> items) {
        super(fm);
        if (items == null || items.isEmpty()) {
            this.mItems = new ArrayList<>();
            mItems.add(new CatalogSubCategory());
        } else {
            this.mItems = items;
        }
        this.categoryId=categoryId;
    }

    public void setPointsAvailable(boolean isPointsAvailable){
        this.isPointsAvailable=isPointsAvailable;
    }

    @Override
    public Fragment getItem(int position) {
        return CatalogListItemFragment.newInstance(categoryId,
                mItems.get(position).getId(), isPointsAvailable);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).getName() == null ? "" : mItems.get(position).getName();
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
