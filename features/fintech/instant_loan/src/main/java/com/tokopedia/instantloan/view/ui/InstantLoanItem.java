package com.tokopedia.instantloan.view.ui;

import android.support.v4.app.Fragment;

/**
 * Created by sachinbansal on 6/12/18.
 */

public class InstantLoanItem {
    CharSequence title;
    Fragment fragment;

    public InstantLoanItem(CharSequence title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}