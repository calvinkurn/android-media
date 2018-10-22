package com.tokopedia.tokopoints.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tokopedia.tokopoints.view.fragment.CatalogListItemFragment;
import com.tokopedia.tokopoints.view.model.CatalogSortType;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;

import java.util.List;

public class CatalogSortTypePagerAdapter extends FragmentStatePagerAdapter {

    private List<CatalogSortType> mItems;
    private CatalogListingPresenter mPresenter;
    private SparseArray<Fragment> mrRegisteredFragments = new SparseArray<>();

    public CatalogSortTypePagerAdapter(FragmentManager fm, List<CatalogSortType> sortTypes, CatalogListingPresenter presenter) {
        super(fm);
        this.mItems = sortTypes;
        this.mPresenter = presenter;
    }

    @Override
    public Fragment getItem(int position) {
        return CatalogListItemFragment.newInstance(mPresenter.getSelectedCategoryId(), mItems.get(position).getId());
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).getText();
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
