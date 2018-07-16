package com.tokopedia.home.account.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.tokopedia.home.account.presentation.fragment.BuyerAccountFragment;
import com.tokopedia.home.account.presentation.fragment.SellerAccountFragment;

/**
 * @author okasurya on 7/16/18.
 */
public class AccountHomePagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles;

    public AccountHomePagerAdapter(FragmentManager fragmentManager, String[] titles) {
        super(fragmentManager);
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return BuyerAccountFragment.newInstance();
        } else {
            return SellerAccountFragment.newInstance();
        }
    }
}
