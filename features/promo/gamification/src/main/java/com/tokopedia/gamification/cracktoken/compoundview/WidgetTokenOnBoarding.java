package com.tokopedia.gamification.cracktoken.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.tokopedia.gamification.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hendry on 04/04/18.
 */

public class WidgetTokenOnBoarding extends FrameLayout {

    public static final String CRACK_TOKEN_ONBOARDING_PREF = "crack_token_onboarding.pref";
    public static final String HAS_SEEN = "has_seen";

    private View onboardingView;
    private int rootWidth;
    private int rootHeight;

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
            int imageWidth = (int) (0.5 * Math.min(rootWidth, rootHeight));
            int initialXEgg = rootWidth / 2 + (int) (0.9 * imageWidth);
            int initialYEgg = rootHeight / 2 + (int) (0.2 * imageWidth);

            onboardingView = LayoutInflater.from(getContext()).inflate(R.layout.image_view_hand, this, false);
            onboardingView.setX(initialXEgg);
            onboardingView.setY(initialYEgg);
            this.addView(onboardingView);

            animateHandOnBoarding();
        }
    }

    private void animateHandOnBoarding() {
        //TODO animate

    }

    public void hideHandOnBoarding() {
        if (onboardingView != null) {
            this.removeView(onboardingView);
            saveSeenOnboardingPreference();
        }
    }

    private boolean hasSeenOnBoardingFromPref() {
        SharedPreferences sharedPreferences = getSharedPref();
        return sharedPreferences.getBoolean(HAS_SEEN, false);
    }

    private void saveSeenOnboardingPreference() {
        //SharedPreferences.Editor editor = getSharedPref().edit();
        //editor.putBoolean(HAS_SEEN, true);
        //editor.apply();
    }

    private SharedPreferences getSharedPref() {
        return getContext().getSharedPreferences(CRACK_TOKEN_ONBOARDING_PREF, MODE_PRIVATE);
    }
}
