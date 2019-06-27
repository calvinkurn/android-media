package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.util.TokenMarginUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hendry on 04/04/18.
 */

public class WidgetTokenOnBoarding extends FrameLayout {

    public static final String CRACK_TOKEN_ONBOARDING_PREF = "crack_token_onboarding.pref";
    public static final String HAS_SEEN = "has_seen";
    public static final int SHORT_ANIM_DURATION = 600;
    public static final int MEDIUM_ANIM_DURATION = 800;

    private View onboardingView;
    private ImageView ivOnboardingCircle;
    private int rootWidth;
    private int rootHeight;
    private AnimatorSet set;

    public WidgetTokenOnBoarding(@NonNull Context context) {
        super(context);
        init();
    }

    public WidgetTokenOnBoarding(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetTokenOnBoarding(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WidgetTokenOnBoarding(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initBound();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    WidgetTokenOnBoarding.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    WidgetTokenOnBoarding.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void initBound() {
        rootWidth = this.getWidth();
        rootHeight = this.getHeight();
    }

    public void showHandOnboarding() {
        if (!hasSeenOnBoardingFromPref() && rootWidth > 0) {
            int imageWidth = TokenMarginUtil.getEggWidth(rootWidth, rootHeight);
            int initialXEgg = rootWidth / 2 + (int) (0.3 * imageWidth);
            int initialYEgg = rootHeight / 2 + (int) (0.05 * imageWidth);
            int delta = (int) (0.1 * imageWidth);

            onboardingView = LayoutInflater.from(getContext()).inflate(R.layout.image_view_hand, this, false);
            ivOnboardingCircle = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.image_view_hand_circle,
                    this, false);
            onboardingView.setX(initialXEgg);
            onboardingView.setY(initialYEgg);

            int circleWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.image_width_hand_circle);
            ivOnboardingCircle.setX(initialXEgg - delta - circleWidth / 2);
            ivOnboardingCircle.setY(initialYEgg - delta - circleWidth / 2);
            ivOnboardingCircle.setVisibility(View.GONE);
            onboardingView.setVisibility(GONE);

            this.addView(ivOnboardingCircle);
            this.addView(onboardingView);

            animateHandOnBoarding(initialXEgg, initialYEgg, delta);
        }
    }

    private void animateHandOnBoarding(int initialXEgg, int initialYEgg, int delta) {
        set = new AnimatorSet();
        PropertyValuesHolder pvhX =
                PropertyValuesHolder.ofFloat(View.X, initialXEgg, initialXEgg - delta);
        PropertyValuesHolder pvhY =
                PropertyValuesHolder.ofFloat(View.Y, initialYEgg, initialYEgg - delta);
        ObjectAnimator translateAnimator = ObjectAnimator.ofPropertyValuesHolder(onboardingView, pvhX, pvhY);
        translateAnimator.setDuration(SHORT_ANIM_DURATION);
        translateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivOnboardingCircle.setVisibility(View.GONE);
                onboardingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ivOnboardingCircle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        PropertyValuesHolder pvhScaleX =
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0, 1);
        PropertyValuesHolder pvhScaleY =
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0, 1);
        PropertyValuesHolder pvhAlpha =
                PropertyValuesHolder.ofFloat(View.ALPHA, 1, 0f);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(ivOnboardingCircle, pvhScaleX, pvhScaleY, pvhAlpha);
        scaleAnimator.setInterpolator(new AccelerateInterpolator());
        scaleAnimator.setDuration(MEDIUM_ANIM_DURATION);
        scaleAnimator.setRepeatCount(1);

        set.playSequentially(translateAnimator, scaleAnimator);
        set.addListener(new Animator.AnimatorListener() {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (getContext() != null)
                        set.start();
                }
            };
            private boolean mCanceled;

            @Override
            public void onAnimationStart(Animator animation) {
                mCanceled = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mCanceled) {
                    handler.postDelayed(runnable, MEDIUM_ANIM_DURATION);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCanceled = true;
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    public void hideHandOnBoarding(boolean saveToPreference) {
        if (onboardingView != null) {
            set.cancel();
            this.removeView(onboardingView);
            this.removeView(ivOnboardingCircle);
            if (saveToPreference) {
                saveSeenOnboardingPreference();
            }
        }
    }

    public boolean hasSeenOnBoardingFromPref() {
        SharedPreferences sharedPreferences = getSharedPref();
        return sharedPreferences.getBoolean(HAS_SEEN, false);
    }

    private void saveSeenOnboardingPreference() {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putBoolean(HAS_SEEN, true);
        editor.apply();
    }

    private SharedPreferences getSharedPref() {
        return getContext().getSharedPreferences(CRACK_TOKEN_ONBOARDING_PREF, MODE_PRIVATE);
    }
}
