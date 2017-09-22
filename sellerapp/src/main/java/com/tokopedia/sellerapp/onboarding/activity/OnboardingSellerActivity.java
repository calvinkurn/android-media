package com.tokopedia.sellerapp.onboarding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.onboarding.OnboardingActivity;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;
import com.tokopedia.sellerapp.onboarding.fragment.OnBoardingSellerFragment;

public class OnboardingSellerActivity extends OnboardingActivity {
    private static final int REQUEST_ACTIVATE_PHONE_SELLER = 900;
    private SessionHandler sessionHandler;

    @Override
    public void init(Bundle savedInstanceState) {
        sessionHandler = new SessionHandler(this);
        addSlide(OnBoardingSellerFragment.newInstance(getString(R.string.seller_onb_1_title), getString(R.string.seller_onb_1_sub_title),
                getString(R.string.onb_1_desc), R.drawable.cover_onboard_1,
                ContextCompat.getColor(getApplicationContext(), R.color.white),
                OnBoardingSellerFragment.VIEW_DEFAULT));

        // If user already have store, skip open store page
        if (!isUserHasShop()) {
            addSlide(OnBoardingSellerFragment.newInstance(getString(R.string.seller_onb_3_title), getString(R.string.seller_onb_3_sub_title),
                    getString(R.string.onb_3_desc), R.drawable.cover_onboard_3,
                    ContextCompat.getColor(getApplicationContext(), R.color.white),
                    OnBoardingSellerFragment.VIEW_ENDING));
        }

        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setFlowAnimation();
        try {
            setIndicatorColor(ContextCompat.getColor(this, R.color.orange),
                    ContextCompat.getColor(this, R.color.orange_300));
        } catch (NoSuchMethodError error) {
            Log.d(getClass().getSimpleName(), error.toString());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        if (isUserHasShop() && SessionHandler.isMsisdnVerified()) {
            startActivity(new Intent(this, SellerHomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            SessionHandler.setFirstTimeUser(this, false);
        } else if (SessionHandler.isMsisdnVerified()) {
            Intent intent = SellerRouter.getAcitivityShopCreateEdit(this);
            intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                    SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
            intent.putExtra(SellerRouter.ShopSettingConstant.ON_BACK,
                    SellerRouter.ShopSettingConstant.LOG_OUT);
            startActivity(intent);
            finish();
        } else {
            Intent intent = SessionRouter.getPhoneVerificationActivationActivityIntent(this);
            intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                    SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
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
        return !TextUtils.isEmpty(sessionHandler.getShopID())
                && !sessionHandler.getShopID().equals("0");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACTIVATE_PHONE_SELLER && isUserHasShop()) {
            startActivity(new Intent(this, SellerHomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } else if (requestCode == REQUEST_ACTIVATE_PHONE_SELLER) {
            Intent intent = SellerRouter.getAcitivityShopCreateEdit(this);
            intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                    SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
            intent.putExtra(SellerRouter.ShopSettingConstant.ON_BACK,
                    SellerRouter.ShopSettingConstant.LOG_OUT);
            startActivity(intent);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
