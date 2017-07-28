package com.tokopedia.core.onboarding;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.onboarding.fragment.NewOnBoardingFragment;
import com.tokopedia.core.onboarding.fragment.OnBoardingFragment;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;

import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.LayoutParams;

/**
 * Created by hafizh HERDI on 3/7/2016.
 */
public class OnboardingActivity extends BaseOnboardingActivity {

    private RelativeLayout main;
    protected View decorView;
    protected View indicator;
    protected View bottom;
    private TextView skipView;
    private ImageButton nextView;
    private ValueAnimator mColorAnimation;
    private Integer[] colors;

    @Override
    public void init(Bundle savedInstanceState) {

        setStepper();

        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_1_title),
                getString(R.string.nonb_1_desc), R.drawable.onboarding_toped_animation,
                ContextCompat.getColor(getApplicationContext(), R.color.medium_green),
                OnBoardingFragment.VIEW_DEFAULT,0));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_2_title),
                getString(R.string.nonb_2_desc), R.drawable.ic_yang_jual_banyak,
                ContextCompat.getColor(getApplicationContext(), R.color.light_blue_300),
                OnBoardingFragment.VIEW_DEFAULT,1));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_3_title),
                getString(R.string.nonb_3_desc), R.drawable.ic_belanja_lebih_mudah,
                ContextCompat.getColor(getApplicationContext(), R.color.orange_300),
                OnBoardingFragment.VIEW_DEFAULT,2));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_4_title),
                getString(R.string.nonb_4_desc), R.drawable.ic_yuk_belanja,
                ContextCompat.getColor(getApplicationContext(), R.color.medium_green),
                OnBoardingFragment.VIEW_DEFAULT,3));

        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_5_title),
                getString(R.string.nonb_5_desc), R.drawable.ic_yuk_belanja,
                ContextCompat.getColor(getApplicationContext(), R.color.orange_300),
                OnBoardingFragment.VIEW_ENDING,4));

        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        indicator = findViewById(R.id.indicator_container);
        bottom = findViewById(R.id.bottom);
        nextView = (ImageButton) nextButton;
        skipView = (TextView) skipButton;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            nextView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.fab_button_diameter_nob);
//            nextView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.fab_button_diameter_nob);
//            nextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            nextView.setBackground(MethodChecker.getDrawable(this, R.drawable.circle));
            nextView.setColorFilter(MethodChecker.getColor(this, R.color.transparent));
        }

        setSkip();
        setTransitionBackground();
    }

    private void setStepper() {
        main = (RelativeLayout) pager.getParent();
        main.setId(R.id.main);

        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        float d = getResources().getDisplayMetrics().density;
        params1.setMargins((int)(70*d), (int)(65*d), (int)(70*d), 0);
        params1.addRule(ALIGN_PARENT_TOP);

        RelativeLayout.LayoutParams params2 = (LayoutParams) pager.getLayoutParams();
        params2.addRule(BELOW, R.id.main);

        LinearLayout stepper = (LinearLayout) View.inflate(this, R.layout.step_list, null);
        Log.i("setStepper: ", String.valueOf(main.getChildCount()));
        main.addView(stepper, params1);
        Log.i("setStepper: ", String.valueOf(main.getChildCount()));
    }

    private void setSkip() {
        showSkipButton(true);
        setSkipText("Lewati");
        skipView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
        skipView.setTypeface(Typeface.DEFAULT_BOLD);
        skipView.setGravity(Gravity.LEFT | Gravity.CENTER);
        skipView.setAllCaps(false);

        float d = getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) skipView.getLayoutParams();
        params.leftMargin = (int) (20 * d);
        setSeparatorColor(MethodChecker.getColor(this, R.color.transparent));
    }

    private void setTransitionBackground() {
        Integer[] list = new Integer[]{
                MethodChecker.getColor(this,R.color.medium_green)
                , MethodChecker.getColor(this,R.color.blue_nob)
                , MethodChecker.getColor(this,R.color.orange_nob)
                , MethodChecker.getColor(this,R.color.medium_green)
                , MethodChecker.getColor(this,R.color.orange_nob)};

        pager.setPageTransformer(false, new ColorTransformer(list));
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

        if (pager.getCurrentItem() == fragments.size()-1) {
//            showSkipButton(false);
//            indicator.setVisibility(View.GONE);
            if(bottom!=null) bottom.setVisibility(View.GONE);
            ((NewOnBoardingFragment)fragments.get(pager.getCurrentItem())).playAnimation();
        } else {
//            showSkipButton(true);
//            indicator.setVisibility(View.VISIBLE);
            if(bottom!=null) bottom.setVisibility(View.VISIBLE);
        }

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

}
