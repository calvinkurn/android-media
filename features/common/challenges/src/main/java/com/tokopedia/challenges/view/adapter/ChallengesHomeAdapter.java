package com.tokopedia.challenges.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.tokopedia.challenges.view.fragments.ChallengesFragment;
import com.tokopedia.challenges.view.fragments.MySubmissionsFragment;

public class ChallengesHomeAdapter extends FragmentStatePagerAdapter {

    public ChallengesHomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChallengesFragment();
            case 1:
                return new MySubmissionsFragment();
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

}
