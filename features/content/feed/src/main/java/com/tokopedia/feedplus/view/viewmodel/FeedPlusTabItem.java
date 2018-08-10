package com.tokopedia.feedplus.view.viewmodel;

import android.support.v4.app.Fragment;

/**
 * @author by milhamj on 09/08/18.
 */

public class FeedPlusTabItem {
    private String title;
    private Fragment fragment;

    public FeedPlusTabItem(String title, Fragment fragment) {
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
