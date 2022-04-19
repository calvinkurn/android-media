package com.tokopedia.home;

import androidx.fragment.app.Fragment;

import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment;

public class HomeInternalRouter {
    public static Fragment getHomeFragment(boolean scrollToRecommendList) {
        return HomeRevampFragment.newInstance(scrollToRecommendList);
    }
}
