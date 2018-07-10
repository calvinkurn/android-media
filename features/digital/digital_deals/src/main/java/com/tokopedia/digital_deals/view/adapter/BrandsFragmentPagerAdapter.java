package com.tokopedia.digital_deals.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.model.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

public class BrandsFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<CategoriesModel> categoryList;
    private ArrayList<Fragment> fragmentArrayList;

    public BrandsFragmentPagerAdapter(FragmentManager fm, List<CategoriesModel> categoryList) {
        super(fm);
        this.categoryList = categoryList;
        fragmentArrayList = new ArrayList<>();
        for (int i = 0; i < this.categoryList.size(); i++)
            fragmentArrayList.add(null);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        try {
            fragment = fragmentArrayList.get(position);
            if (fragment == null) {
                fragment = AllBrandsFragment.newInstance(categoryList.get(position));
                fragmentArrayList.remove(position);
                fragmentArrayList.add(position, fragment);
            }
        } catch (IndexOutOfBoundsException e) {
            AllBrandsFragment categoryFragment = (AllBrandsFragment) AllBrandsFragment.newInstance(categoryList.get(position));
            fragmentArrayList.add(position, categoryFragment);
            fragment = fragmentArrayList.get(position);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_UNCHANGED;
    }
}
