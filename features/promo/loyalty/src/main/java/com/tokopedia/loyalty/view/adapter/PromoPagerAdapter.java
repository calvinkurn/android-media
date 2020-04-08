package com.tokopedia.loyalty.view.adapter;

import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;

import java.util.List;

/**
 * Created by nabillasabbaha on 1/5/18.
 */

public class PromoPagerAdapter extends FragmentStatePagerAdapter {

    private final String autoSelectCategoryId;
    private List<PromoMenuData> promoMenuDataList;
    private PromoListFragment.OnFragmentInteractionListener promoListActionListener;

    public PromoPagerAdapter(PromoListFragment.OnFragmentInteractionListener promoListActionListener,
                             FragmentManager fm, List<PromoMenuData> promoMenuDataList,
                             String autoSelectedCategoryId) {
        super(fm);
        this.promoListActionListener = promoListActionListener;
        this.promoMenuDataList = promoMenuDataList;
        this.autoSelectCategoryId = autoSelectedCategoryId;
    }

    @Override
    public Fragment getItem(int position) {
        return PromoListFragment.newInstance(promoListActionListener,
                promoMenuDataList.get(position), autoSelectCategoryId);
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
