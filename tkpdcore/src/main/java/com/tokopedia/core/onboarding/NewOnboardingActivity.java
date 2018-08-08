package com.tokopedia.core.onboarding;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.onboarding.fragment.NewOnBoardingFragment;
import com.tokopedia.core.onboarding.fragment.OnBoardingFragment;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by steven on 7/25/2017.
 */

public class NewOnboardingActivity extends OnboardingActivity {

    private static final String SCREEN_NAME = "Screen OnBoarding - ";
    protected View indicator;
    protected View bottom;
    private TextView skipView;
    private ImageButton nextView;
    private int[] fragmentColor;
    private boolean isNextPressed = false;

    @Override
    public void init(Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        initView();
        addSlides();
        pager.setOffscreenPageLimit(1);
        setSkip();
        setNext();
        pager.setPageTransformer(false, new CustomAnimationPageTransformer());
    }

    private void initView() {
        indicator = findViewById(R.id.indicator_container);
        bottom = findViewById(R.id.bottom);
        nextView = (ImageButton) nextButton;
        skipView = (TextView) skipButton;

        decorView = getWindow().getDecorView();
        showStatusBar(false);

        fragmentColor = new int[]{R.color.green_nob,
                R.color.blue_nob,
                R.color.orange_nob,
                R.color.green_nob,
                R.color.blue_nob};
    }

    private void addSlides() {
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_1_title),
                getString(R.string.nonb_1_desc), "onboarding1.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[0]),
                OnBoardingFragment.VIEW_DEFAULT, 0));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_2_title),
                getString(R.string.nonb_2_desc), "onboarding2.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[1]),
                OnBoardingFragment.VIEW_DEFAULT, 1));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_3_title),
                getString(R.string.nonb_3_desc), "onboarding3.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[2]),
                OnBoardingFragment.VIEW_DEFAULT, 2));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_4_title),
                getString(R.string.nonb_4_desc), "onboarding4.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[3]),
                OnBoardingFragment.VIEW_DEFAULT, 3));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_5_title),
                getString(R.string.nonb_5_desc), "onboarding5.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[4]),
                OnBoardingFragment.VIEW_ENDING, 4));
    }

    private void setSkip() {
        showSkipButton(false);
        setSkipText("Lewati");
        skipView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
        skipView.setTypeface(Typeface.DEFAULT_BOLD);
        skipView.setGravity(Gravity.LEFT | Gravity.CENTER);
        skipView.setAllCaps(false);

        float density = getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) skipView.getLayoutParams();
        params.leftMargin = (int) (20 * density);
        setSeparatorColor(MethodChecker.getColor(this, R.color.transparent));
    }

    @Override
    public void onSkipPressed() {
        onDonePressed();
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onDonePressed() {
        UnifyTracking.eventOnboardingSkip(pager.getCurrentItem() + 1);
        SessionHandler.setFirstTimeUserNewOnboard(this, false);
        Intent intent = new Intent(this, HomeRouter.getHomeActivityClass());
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged() {
        if (pager.getCurrentItem() == fragments.size() - 1) {
            setButtonVisibility(bottom, GONE);
            setButtonVisibility(skipButton, GONE);
            setButtonVisibility(indicator, GONE);
        } else {
            setButtonVisibility(bottom, VISIBLE);
            setButtonVisibility(skipButton, VISIBLE);
            setButtonVisibility(indicator, VISIBLE);
        }
        int pageNumber = pager.getCurrentItem() + 1;
        ScreenTracking.screen(SCREEN_NAME + pageNumber);
    }

    private void setButtonVisibility(View view, int visible) {
        if (view != null) {
            view.setVisibility(visible);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDonePressed();
    }

    private void setNext() {
        nextView.setImageResource(R.drawable.next_ic);
        nextView.setMinimumWidth(0);
        FrameLayout.LayoutParams nextViewLayoutParams =
                (FrameLayout.LayoutParams) nextView.getLayoutParams();
        float density = getResources().getDisplayMetrics().density;
        nextViewLayoutParams.rightMargin = (int) (20 * density);
    }

    public void setNextResource() {
        if (nextView != null) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View v) {
                    if (!isNextPressed) {
                        isNextPressed = true;

                        if (isVibrateOn) {
                            mVibrator.vibrate(vibrateIntensity);
                        }

                        boolean requestPermission = false;
                        int position = 0;

                        for (int i = 0; i < permissionsArray.size(); i++) {
                            requestPermission = pager.getCurrentItem() + 1 == permissionsArray.get(i).getPosition();
                            position = i;
                            break;
                        }

                        if (requestPermission) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionsArray.get(position).getPermission(), 1);
                                permissionsArray.remove(position);
                                isNextPressed = false;
                            } else {
                                NewOnBoardingFragment currentFragment = ((NewOnBoardingFragment) fragments.get(pager.getCurrentItem()));
                                if (currentFragment != null) currentFragment.animateOut();
                                pager.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                                        isNextPressed = false;
                                    }
                                }, 1250);
                                onNextPressed();
                            }
                        } else {
                            NewOnBoardingFragment currentFragment = ((NewOnBoardingFragment) fragments.get(pager.getCurrentItem()));
                            if (currentFragment != null) currentFragment.animateOut();
                            pager.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                                    isNextPressed = false;
                                }
                            }, 1250);
                            onNextPressed();
                        }
                    }
                }
            });
        }
    }
}
