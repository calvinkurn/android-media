package com.tokopedia.saldodetails.view.ui;

import android.support.v4.app.Fragment;


public class SaldoHistoryTabItem {

    private String title;
    private Fragment fragment;

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
