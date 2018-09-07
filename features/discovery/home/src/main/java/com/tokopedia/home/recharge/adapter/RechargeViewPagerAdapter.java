package com.tokopedia.home.recharge.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.tokopedia.core.customView.WrapContentViewPager;
import com.tokopedia.digital.widget.view.fragment.WidgetAllStyleRechargeFragment;
import com.tokopedia.digital.widget.view.model.category.Category;

import java.util.List;

/**
 * @author kulomady 05 on 7/11/2016.
 *         Modified by Nabilla Sabbaha on 4/10/2017
 */
public class RechargeViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Category> categoryList;
    private int currentPosition = -1;

    public RechargeViewPagerAdapter(FragmentManager fm, List<Category> categoryList) {
        super(fm);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        Category category = categoryList.get(position);
        return WidgetAllStyleRechargeFragment.newInstance(category, position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != currentPosition) {
            Fragment fragment = (Fragment) object;
            WrapContentViewPager pager = (WrapContentViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                currentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }

    public void addFragments(List<Category> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }
}