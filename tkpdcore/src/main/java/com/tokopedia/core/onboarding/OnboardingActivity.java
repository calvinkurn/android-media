package com.tokopedia.core.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.onboarding.fragment.OnBoardingFragment;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by hafizh HERDI on 3/7/2016.
 */
public class OnboardingActivity extends BaseOnboardingActivity {

    protected View decorView;

    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(OnBoardingFragment.newInstance(getString(R.string.onb_1_title),
                getString(R.string.onb_1_desc), R.drawable.onboarding_toped_animation,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_DEFAULT));
        addSlide(OnBoardingFragment.newInstance(getString(R.string.onb_2_title),
                getString(R.string.onb_2_desc), R.drawable.ic_yang_jual_banyak,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_DEFAULT));
        addSlide(OnBoardingFragment.newInstance(getString(R.string.onb_3_title),
                getString(R.string.onb_3_desc), R.drawable.ic_belanja_lebih_mudah,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_DEFAULT));
        addSlide(OnBoardingFragment.newInstance(getString(R.string.onb_4_title),
                getString(R.string.onb_4_desc), R.drawable.ic_buka_toko_gratis,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_DEFAULT));
        addSlide(OnBoardingFragment.newInstance(getString(R.string.onb_5_title),
                getString(R.string.onb_5_desc), R.drawable.onboarding_notification_animation,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_DEFAULT));
        addSlide(OnBoardingFragment.newInstance(getString(R.string.onb_6_title),
                "", R.drawable.ic_yuk_belanja,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_ENDING));

//        showStatusBar(false);
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


        setFlowAnimation();
        showStatusBar(false);
        setProgressButtonEnabled(true);
        showSkipButton(false);
        ScreenTracking.screen(AppScreen.SCREEN_ONBOARDING+1);
    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onNextPressed() {
//        if (pager.getCurrentItem() == 5) {
//            this.setProgressButtonEnabled(false);
//        }
    }

    @Override
    public void onDonePressed() {
        SessionHandler.setFirstTimeUser(this, false);
        Intent intent = new Intent(this, HomeRouter.getHomeActivityClass());
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged() {
//        if (pager.getCurrentItem() == 5) {
//            this.setProgressButtonEnabled(false);
//        } else {
//            this.setProgressButtonEnabled(true);
//
//        }
        ScreenTracking.screen(AppScreen.SCREEN_ONBOARDING+(pager.getCurrentItem()+1));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        SessionHandler.setFirstTimeUser(this, false);
    }

//    public void nextPage() {
//        if (pager.getCurrentItem() < pager.getAdapter().getCount()) {
//            pager.setCurrentItem(pager.getCurrentItem() + 1);
//            onNextPressed();
//        }
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_onboarding;
    }
}
