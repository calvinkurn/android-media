package com.tokopedia.gamification.taptap.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkConstant;
import com.tokopedia.gamification.taptap.fragment.TapTapTokenFragment;
import com.tokopedia.gamification.util.TapTapAnalyticsTrackerUtil;

public class TapTapTokenActivity extends BaseSimpleActivity {


    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.GAMIFICATION2)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return TapTapTokenActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, TapTapTokenActivity.class);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, TapTapTokenActivity.class);
    }

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

        } else if (crackTokenFragment != null) {
            crackTokenFragment.clearViewAndAnimations();
            onBackPressedRoot();
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
