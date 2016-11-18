package com.tokopedia.core.rescenter.onboarding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.onboarding.BaseOnboardingActivity;
import com.tokopedia.core.onboarding.fragment.OnBoardingFragment;
import com.tokopedia.core.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;


/**
 * Created by nisie on 8/26/16.
 */
public class FreeReturnOnboardingActivity extends BaseOnboardingActivity {

    public static final String HAS_SEEN_ONBOARDING = "HAS_SEEN_ONBOARDING";
    public static final String CACHE_FREE_RETURN = "CACHE_FREE_RETURN";

    View decorView;

    public static Intent newInstance(Context context, String orderID) {
        Intent intent = new Intent(context, FreeReturnOnboardingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CreateResCenterActivity.KEY_PARAM_ORDER_ID, orderID);
        intent.putExtras(bundle);
        return intent;
    }

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
        LocalCacheHandler cache = new LocalCacheHandler(this, CACHE_FREE_RETURN);
        cache.putBoolean(HAS_SEEN_ONBOARDING, true);
        cache.applyEditor();

        startActivityForResult(CreateResCenterActivity.newInstance(this,
                getIntent().getExtras().getString(CreateResCenterActivity.KEY_PARAM_ORDER_ID, "")),
                TransactionRouter.CREATE_RESCENTER_REQUEST_CODE);

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
        if (requestCode == TransactionRouter.CREATE_RESCENTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
            finish();
        } else if (requestCode == TransactionRouter.CREATE_RESCENTER_REQUEST_CODE) {
            finish();
        }
    }
}
