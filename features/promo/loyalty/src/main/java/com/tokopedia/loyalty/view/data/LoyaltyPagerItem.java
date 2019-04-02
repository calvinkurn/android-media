package com.tokopedia.loyalty.view.data;


import android.support.v4.app.Fragment;

/**
 * @author anggaprasetiyo on 30/11/17.
 */

public class LoyaltyPagerItem {

    private Fragment fragment;
    private String tabTitle;
    private int position;

    private LoyaltyPagerItem(Builder builder) {
        fragment = builder.fragment;
        tabTitle = builder.tabTitle;
        position = builder.position;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public int getPosition() {
        return position;
    }


    public static final class Builder {
        private Fragment fragment;
        private String tabTitle;
        private int position;

        public Builder() {
        }

        public Builder fragment(Fragment val) {
            fragment = val;
            return this;
        }

        public Builder tabTitle(String val) {
            tabTitle = val;
            return this;
        }

        public Builder position(int val) {
            position = val;
            return this;
        }

        public LoyaltyPagerItem build() {
            return new LoyaltyPagerItem(this);
        }
    }
}
