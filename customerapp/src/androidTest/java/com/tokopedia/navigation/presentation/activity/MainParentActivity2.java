package com.tokopedia.navigation.presentation.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.navigation.presentation.fragment.EmptyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * cannot run in androidTest
 */
public class MainParentActivity2 extends MainParentActivity {

    @Override
    List<Fragment> createFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(EmptyFragment.newInstance(0));
        fragmentList.add(EmptyFragment.newInstance(1));
        fragmentList.add(EmptyFragment.newInstance(2));
        return fragmentList;
    }
}
