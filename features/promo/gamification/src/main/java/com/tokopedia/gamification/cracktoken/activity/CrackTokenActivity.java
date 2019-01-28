package com.tokopedia.gamification.cracktoken.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.applink.ApplinkConstant;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.fragment.CrackEmptyTokenFragment;
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment;
import com.tokopedia.gamification.floating.view.model.TokenData;

import static android.view.View.VISIBLE;

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
    protected int getLayoutRes() {
        return R.layout.activity_token_crack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            toolbar.setElevation(getResources().getDimensionPixelOffset(R.dimen.dp_4));
//            toolbar.setOutlineProvider(ViewOutlineProvider.BOUNDS);
//            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
//            int margin = getResources().getDimensionPixelOffset(R.dimen.dp_5);
//            layoutParams.setMargins(-margin, - margin, -margin, 0);
//        }


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected Fragment getNewFragment() {
        return CrackTokenFragment.newInstance();
    }

    @Override
    public void directPageToCrackEmpty(TokenData tokenData) {
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

    @Override
    public void hideToolbar() {
//        getSupportActionBar().hide();
//        ivCoverShadow.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
//        getSupportActionBar().show();
//        ivCoverShadow.setVisibility(VISIBLE);
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

    @Override
    public void showRemainingToken(String smallImageUrl, String remainingTokenString) {
//        ImageHandler.loadImageAndCache(imageRemainingToken, smallImageUrl);
//        if (TextUtils.isEmpty(remainingTokenString)) {
//            tvCounter.setVisibility(View.GONE);
//            flRemainingToken.setVisibility(View.GONE);
//
//        } else {
//            tvCounter.setText(remainingTokenString);
//            tvCounter.setVisibility(VISIBLE);
//            flRemainingToken.setVisibility(View.VISIBLE);
//
//        }
    }
}
