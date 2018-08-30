package com.tokopedia.reksadana.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.reksadana.view.fragment.BuyFragment;
import com.tokopedia.reksadana.view.fragment.SellFragment;

public class BuySellTabAdapter extends FragmentStatePagerAdapter {
    boolean buyTab;
    public BuySellTabAdapter(FragmentManager fm, boolean buyTab) {
        super(fm);
        this.buyTab = buyTab;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return BuyFragment.createInstance();
            case 1:
                return SellFragment.createInstance();
        }
        return null;
    }
}
