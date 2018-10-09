package com.tokopedia.sellerapp.onboarding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.sellerapp.onboarding.fragment.OnBoardingSellerFragment;
import com.tokopedia.user.session.UserSession;

public class OnboardingSellerActivity extends AppIntro {
    private static final int REQUEST_ACTIVATE_PHONE_SELLER = 900;
    private UserSession userSession;

    @Override
    public void init(Bundle savedInstanceState) {
        userSession = new UserSession(this);
        addSlide(OnBoardingSellerFragment.newInstance(getString(R.string.seller_onb_1_title), getString(R.string.seller_onb_1_sub_title),
                getString(R.string.onb_1_desc), R.drawable.cover_onboard_1,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingSellerFragment.VIEW_DEFAULT));

        // If user already have store, skip open store page
        if (!isUserHasShop()) {
            addSlide(OnBoardingSellerFragment.newInstance(getString(R.string.seller_onb_3_title), getString(R.string.seller_onb_3_sub_title),
                    getString(R.string.onb_3_desc), R.drawable.cover_onboard_3,
                    ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                    OnBoardingSellerFragment.VIEW_ENDING));
            showPagerIndicator(true);
        } else {
            showPagerIndicator(false);
        }

        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        try {
            setIndicatorColor(ContextCompat.getColor(this, R.color.orange),
                    ContextCompat.getColor(this, R.color.orange_300));
        } catch (NoSuchMethodError error) {
            Log.d(getClass().getSimpleName(), error.toString());
        }

        setFlowAnimation();
        showStatusBar(false);
        showSkipButton(false);
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

    }

    @Override
    public void onDonePressed() {
        if (isUserHasShop() && userSession.isMsisdnVerified()) {
            startActivity(DashboardActivity.createInstance(this)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            userSession.setFirstTimeUser(false);
        } else if (userSession.isMsisdnVerified() && getApplicationContext() instanceof SellerModuleRouter) {
            Intent intent =  ((SellerModuleRouter)getApplicationContext()).getIntentCreateShop
                    (this);
            startActivity(intent);
            finish();
        } else if (getApplicationContext() instanceof SellerModuleRouter){
            Intent intent =  ((SellerModuleRouter) getApplicationContext())
                    .getPhoneVerificationActivityIntent(this);
            startActivityForResult(intent, REQUEST_ACTIVATE_PHONE_SELLER);
        }
    }

    @Override
    public void onSlideChanged() {

    }

    @Override
    public void onBackPressed() {
        onDonePressed();
    }

    private boolean isUserHasShop() {
        return !TextUtils.isEmpty(userSession.getShopId())
                && !userSession.getShopId().equals("0");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACTIVATE_PHONE_SELLER && isUserHasShop()) {
            startActivity(DashboardActivity.createInstance(this)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } else if (requestCode == REQUEST_ACTIVATE_PHONE_SELLER && getApplicationContext() instanceof SellerModuleRouter ) {
            Intent intent =  ((SellerModuleRouter)getApplicationContext()).getIntentCreateShop
                    (this);
            startActivity(intent);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
