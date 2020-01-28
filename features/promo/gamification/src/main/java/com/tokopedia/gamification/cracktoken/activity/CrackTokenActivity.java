package com.tokopedia.gamification.cracktoken.activity;

import android.media.AudioManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.fragment.CrackEmptyTokenFragment;
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.promogamification.common.GamificationRouter;

public class CrackTokenActivity extends BaseSimpleActivity implements CrackTokenFragment.ActionListener {


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_token_crack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected Fragment getNewFragment() {
        return CrackTokenFragment.newInstance();
    }

    @Override
    public void directPageToCrackEmpty(TokenDataEntity tokenData) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_view);
        if (fragment == null || !(fragment instanceof CrackEmptyTokenFragment))
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_view,
                    CrackEmptyTokenFragment.newInstance(tokenData)).commit();
    }

    private CrackTokenFragment getCrackFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_view);
        if (fragment != null && fragment instanceof CrackTokenFragment) {
            return (CrackTokenFragment) fragment;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        CrackTokenFragment crackTokenFragment = getCrackFragment();
        if (crackTokenFragment != null && crackTokenFragment.isShowReward()) {
            crackTokenFragment.dismissReward();
        } else {
            onBackPressedRoot();
        }
    }

    private void onBackPressedRoot() {
        if (isTaskRoot()) {
            ((GamificationRouter) getApplication()).goToHome(this);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getScreenName() {
        return CrackTokenActivity.class.getName();
    }
}
