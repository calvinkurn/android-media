package com.tokopedia.abstraction.base.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.R;

/**
 * To Set toolbar color, set the theme to android:theme="@style/Theme.White" or android:theme="@style/Theme.Green"
 * Created by zulfikarrahman on 5/30/17.
 */

public abstract class BaseSimpleDFActivity extends BaseSimpleActivity {

    @Override
    protected abstract int getLayoutRes();

    protected abstract int getParentViewResourceID();

}