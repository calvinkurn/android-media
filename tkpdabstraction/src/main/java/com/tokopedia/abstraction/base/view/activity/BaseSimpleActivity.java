package com.tokopedia.abstraction.base.view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.R;

/**
 * To Set toolbar color, set the theme to android:theme="@style/Theme.White" or android:theme="@style/Theme.Green"
 * Created by zulfikarrahman on 5/30/17.
 */

public abstract class BaseSimpleActivity extends BaseToolbarActivity {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Nullable
    protected abstract Fragment getNewFragment();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple;
    }

    protected void setupFragment(Bundle savedInstance) {
        if (savedInstance == null) {
            inflateFragment();
        }
    }

    protected void inflateFragment() {
        Fragment newFragment = getNewFragment();
        if (newFragment == null) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(getParentViewResourceID(), newFragment, getTagFragment())
                .commit();
    }

    protected int getParentViewResourceID(){
        return com.tokopedia.abstraction.R.id.parent_view;
    }

    @Nullable
    protected Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag(getTagFragment());
    }

    protected String getTagFragment() {
        return TAG_FRAGMENT;
    }


}