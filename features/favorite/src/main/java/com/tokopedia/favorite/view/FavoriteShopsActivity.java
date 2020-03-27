package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;

/**
 * @author okasurya on 7/26/18.
 */
public class FavoriteShopsActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return FragmentFavorite.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return FragmentFavorite.TAG;
    }
}
