package com.tokopedia.home.account.presentation.adapter;

import androidx.fragment.app.Fragment;

/**
 * @author okasurya on 7/21/18.
 */
public class AccountFragmentItem {
    private Fragment fragment;
    private String title;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
