package com.tokopedia.sellerapp.onboarding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.onboarding.OnboardingActivity;
import com.tokopedia.core.shop.ShopEditorActivity;
import com.tokopedia.core.shop.presenter.ShopSettingView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;
import com.tokopedia.sellerapp.onboarding.fragment.OnBoardingSellerFragment;

public class OnBoardingSellerActivity extends OnboardingActivity {

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
            setIndicatorColor(ContextCompat.getColor(this, R.color.orange), ContextCompat.getColor(this, R.color.orange_300));
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
        if (isUserHasShop()) {
            startActivity(new Intent(this, SellerHomeActivity.class));
        } else {
            Intent intent = new Intent(this, ShopEditorActivity.class);
            intent.putExtra(ShopSettingView.FRAGMENT_TO_SHOW, ShopSettingView.CREATE_SHOP_FRAGMENT_TAG);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onSlideChanged() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        SessionHandler.setFirstTimeUser(this, false);
    }

    private boolean isUserHasShop() {
        return !TextUtils.isEmpty(sessionHandler.getShopID()) && !sessionHandler.getShopID().equals("0");
    }
}
