package com.tokopedia.core.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.onboarding.fragment.OnBoardingFragment;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;


/**
 * Created by nisie on 8/26/16.
 */
public class FreeReturnOnboardingActivity extends BaseOnboardingActivity {

    View decorView;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(OnBoardingFragment.newInstance("",
                getString(R.string.onb_free_return_1_desc), R.drawable.ic_free_return_asset_1,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_FREE_RETURN));
        addSlide(OnBoardingFragment.newInstance("",
                getString(R.string.onb_free_return_2_desc), R.drawable.ic_free_return_asset_2,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_FREE_RETURN));
        addSlide(OnBoardingFragment.newInstance("",
                getString(R.string.onb_free_return_3_desc), R.drawable.ic_free_return_asset_3,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_FREE_RETURN));
        addSlide(OnBoardingFragment.newInstance("",
                getString(R.string.onb_free_return_4_desc), R.drawable.ic_free_return_asset_4,
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_bg_color_grery),
                OnBoardingFragment.VIEW_FREE_RETURN));

        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setFlowAnimation();
    }

    @Override
    public void onSkipPressed() {
        onDonePressed();
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
    public void onDonePressed() {
        LocalCacheHandler cache = new LocalCacheHandler(this, ConstantOnBoarding.CACHE_FREE_RETURN);
        cache.putBoolean(ConstantOnBoarding.HAS_SEEN_FREE_RETURN_ONBOARDING, true);
        cache.applyEditor();

        startActivityForResult(
                InboxRouter.getCreateResCenterActivityIntent(this, getIntent().getExtras().getString(InboxRouter.EXTRA_ORDER_ID, "")),
                TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE
        );

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
            finish();
        } else if (requestCode == TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE) {
            finish();
        }
    }
}
