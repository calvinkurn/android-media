package com.tokopedia.tkpd.home.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.tkpd.home.facade.FacadePromo;
import com.tokopedia.tkpd.home.fragment.FragmentBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erry on 16/01/17.
 */

public class BannerPagerAdapter extends FragmentStatePagerAdapter {

    List<FacadePromo.PromoItem> promoList = new ArrayList<>();

    public BannerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPromoList(List<FacadePromo.PromoItem> promoList) {
        this.promoList = promoList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentBanner.newInstance(promoList.get(position), position, getCount());
    }

    @Override
    public int getCount() {
        return promoList.size();
    }
}
