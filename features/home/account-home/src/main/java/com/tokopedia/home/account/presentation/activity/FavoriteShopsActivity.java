package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.presentation.fragment.FavoriteShopsFragment;

/**
 * @author okasurya on 7/26/18.
 */
public class FavoriteShopsActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context){
        return new Intent(context, FavoriteShopsActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return ((AccountHomeRouter) getApplicationContext()).getFavoriteFragment();
    }

    @Override
    protected String getTagFragment() {
        return FavoriteShopsFragment.TAG;
    }

    @DeepLink(ApplinkConst.FAVORITE)
    public static Intent getCallingFavShop(Context context, Bundle bundle) {
        return FavoriteShopsActivity.newInstance(context);
    }
}
