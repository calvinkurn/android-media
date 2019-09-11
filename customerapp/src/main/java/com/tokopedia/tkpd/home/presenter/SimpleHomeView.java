package com.tokopedia.tkpd.home.presenter;

import androidx.fragment.app.Fragment;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public interface SimpleHomeView {
    String TAG = "MNORMANSYAH";
    String messageTAG = "SimpleHomeView : ";

    void initToolbar();

    void setTitle(int fragmentType);

    void initFragment(int fragmentType);

    void moveToFragment(Fragment fragment, boolean isAddToBackStack, String TAG);

    boolean isFragmentCreated(String tag);
}
