package com.tokopedia.challenges.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.fragments.ChallengesFragment;
import com.tokopedia.challenges.view.fragments.MySubmissionsFragment;

/**
 * Created by ashwanityagi on 06/08/18.
 */
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
        switch (position) {
            case 0:
                return new ChallengesFragment();
            case 1:
                return new MySubmissionsFragment();
        }
        return new ChallengesFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES;
        } else {
            return ChallengesGaAnalyticsTracker.EVENT_CATEGORY_MYSUBMISSIONS;
        }
    }
}
