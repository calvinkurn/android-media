package com.tokopedia.loyalty.view.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;

import java.util.List;

/**
 * Created by nabillasabbaha on 1/5/18.
 */

public class PromoPagerAdapter extends FragmentStatePagerAdapter {

    private final String autoSelectCategoryId;
    private List<PromoMenuData> promoMenuDataList;

    public PromoPagerAdapter(FragmentManager fm, List<PromoMenuData> promoMenuDataList, String autoSelectedCategoryId) {
        super(fm);
        this.promoMenuDataList = promoMenuDataList;
        this.autoSelectCategoryId = autoSelectedCategoryId;
    }

    @Override
    public Fragment getItem(int position) {
        return PromoListFragment.newInstance(promoMenuDataList.get(position), autoSelectCategoryId);
    }

    /**
     * If "Don't keep activities" is turned on, ViewPager that has FragmentStatePagerAdapter
     * doesn't recreate fragments.
     * This method is used to prevent Android from recreating fragment.
     *
     * @return null
     */
    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getCount() {
        return promoMenuDataList.size();
    }
}
