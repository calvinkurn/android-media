package com.tokopedia.instantloan.view.ui;

import android.support.v4.app.Fragment;

/**
 * Created by sachinbansal on 6/12/18.
 */

public class InstantLoanItem {
    String title;
    Fragment fragment;

    public InstantLoanItem(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}