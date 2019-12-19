package com.tokopedia.events.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.tokopedia.events.view.fragment.CategoryFragment;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 21/11/17.
 */

public class CategoryFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<CategoryViewModel> categoryList;
    private ArrayList<Fragment> fragmentArrayList;

    public CategoryFragmentPagerAdapter(FragmentManager fm, List<CategoryViewModel> categoryList) {
        super(fm);
        this.categoryList = categoryList;
        for (int i = 0; i < this.categoryList.size(); i++) {
            CategoryViewModel model = this.categoryList.get(i);
            if (model.getTitle().equals("Carousel") ||
                    model.getName().equals("carousel") ||
                    model.getName().equalsIgnoreCase("top") ||
                    model.getItems().size() == 0) {
                this.categoryList.remove(i);
                i--;
            }
        }
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
                fragment = CategoryFragment.newInstance(categoryList.get(position),
                        String.valueOf(categoryList.get(position).getCategoryId()));
                fragmentArrayList.remove(position);
                fragmentArrayList.add(position, fragment);
            }
        } catch (IndexOutOfBoundsException e) {
            CategoryFragment categoryFragment = (CategoryFragment) CategoryFragment.newInstance(categoryList.get(position),
                    String.valueOf(categoryList.get(position).getCategoryId()));
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
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_UNCHANGED;
    }
}
