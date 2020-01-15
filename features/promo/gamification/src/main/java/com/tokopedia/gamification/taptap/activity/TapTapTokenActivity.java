package com.tokopedia.gamification.taptap.activity;

import android.media.AudioManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.taptap.fragment.TapTapTokenFragment;
import com.tokopedia.gamification.taptap.utils.TapTapAnalyticsTrackerUtil;
import com.tokopedia.promogamification.common.GamificationRouter;

public class TapTapTokenActivity extends BaseSimpleActivity {


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
        return TapTapTokenFragment.newInstance();
    }

    private TapTapTokenFragment getCrackFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_view);
        if (fragment != null && fragment instanceof TapTapTokenFragment) {
            return (TapTapTokenFragment) fragment;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        TapTapTokenFragment crackTokenFragment = getCrackFragment();
        if (crackTokenFragment != null && crackTokenFragment.isShowBackPopup()) {
            crackTokenFragment.showBackPopup();
        } else {
            onBackPressedRoot();
        }
        TapTapAnalyticsTrackerUtil.sendEvent(this,
                TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                TapTapAnalyticsTrackerUtil.ActionKeys.TAP_EGG_CLICK,
                TapTapAnalyticsTrackerUtil.LabelKeys.PRESS_BACK_BUTTON);
    }

    private void onBackPressedRoot() {
        if (isTaskRoot()) {
            ((GamificationRouter) getApplication()).goToHome(this);
        }
        super.onBackPressed();
    }

    @Override
    public String getScreenName() {
        return TapTapTokenActivity.class.getName();
    }
}
