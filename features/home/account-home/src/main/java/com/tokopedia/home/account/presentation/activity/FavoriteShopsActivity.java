package com.tokopedia.home.account.presentation.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.home.account.presentation.fragment.FavoriteShopsFragment;

/**
 * @author okasurya on 7/26/18.
 */
public class FavoriteShopsActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return FavoriteShopsFragment.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return FavoriteShopsFragment.TAG;
    }
}
