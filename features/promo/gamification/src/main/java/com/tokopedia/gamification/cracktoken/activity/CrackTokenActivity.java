package com.tokopedia.gamification.cracktoken.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.ApplinkConstant;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.fragment.CrackEmptyTokenFragment;
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment;

public class CrackTokenActivity extends BaseSimpleActivity implements CrackTokenFragment.ActionListener {

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.GAMIFICATION)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return CrackTokenActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, CrackTokenActivity.class);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, CrackTokenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle("TokoPoints");
    }

    @Override
    protected Fragment getNewFragment() {
        return CrackTokenFragment.newInstance();
    }

    @Override
    public void directPageToCrackEmpty() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_view);
        if (fragment == null || !(fragment instanceof CrackEmptyTokenFragment))
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_view,
                    CrackEmptyTokenFragment.newInstance()).commit();
    }

}
