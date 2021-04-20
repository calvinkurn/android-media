package com.tokopedia.home;

import androidx.fragment.app.Fragment;

import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment;
import com.tokopedia.home.util.HomeRevampUtilsKt;

public class HomeInternalRouter {
    public static Fragment getHomeFragment(boolean scrollToRecommendList) {
        try {
            if (HomeRevampUtilsKt.isHomeRevampInstance()) {
                return HomeRevampFragment.newInstance(scrollToRecommendList);
            }
        } catch (Exception e) {

        }
        return HomeFragment.newInstance(scrollToRecommendList);

    }
}
