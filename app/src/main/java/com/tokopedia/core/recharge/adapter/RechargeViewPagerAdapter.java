package com.tokopedia.core.recharge.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.tokopedia.core.recharge.fragment.RechargeFragment;
import com.tokopedia.core.recharge.model.category.Category;
import com.tokopedia.core.recharge.view.widget.WrapContentViewPager;

import java.util.List;

/**
 * @author kulomady 05 on 7/11/2016.
 */
public class RechargeViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Category> categoryList;
    private int currentPosition = -1;
    private Bundle extraData = new Bundle();

    public RechargeViewPagerAdapter(FragmentManager fm, List<Category> categoryList) {
        super(fm);
        this.categoryList = categoryList;
    }

    public RechargeViewPagerAdapter(FragmentManager fm, List<Category> categoryList, Bundle extra) {
        super(fm);
        this.categoryList = categoryList;
        this.extraData = extra;
    }

    @Override
    public Fragment getItem(int position) {
        Category category = categoryList.get(position);
        return RechargeFragment.newInstance(category, this.extraData);
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

    @Override
    public int getCount() {
        return categoryList.size();
    }
}
