package com.tokopedia.digital_deals.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.model.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

public class BrandsFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<CategoriesModel> categoryList;
    private ArrayList<Fragment> fragmentArrayList;

    public BrandsFragmentPagerAdapter(FragmentManager fm, List<CategoriesModel> categoryList, String searchText) {
        super(fm);
        this.categoryList = categoryList;
        fragmentArrayList = new ArrayList<>();
        for (CategoriesModel categoriesModel : categoryList) {
            Fragment fragment = AllBrandsFragment.newInstance(categoriesModel, searchText);
            fragmentArrayList.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
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

    public Fragment getSelectedFragment(int position){
        return fragmentArrayList.get(position);
    }
}
