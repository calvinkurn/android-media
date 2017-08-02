package com.tokopedia.core.onboarding;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.onboarding.animation.BounceInterpolator;
import com.tokopedia.core.onboarding.fragment.NewOnBoardingFragment;
import com.tokopedia.core.onboarding.fragment.OnBoardingFragment;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
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
    private Integer[] listId;
    private boolean isFirst;
    private Animator bounceAnimator;

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
        addSlides();
        setStepper();
        setSkip();
        setTransitionBackground();
    }

    private void initView() {
        indicator = findViewById(R.id.indicator_container);
        bottom = findViewById(R.id.bottom);
        nextView = (ImageButton) nextButton;
        skipView = (TextView) skipButton;


        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void addSlides() {
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_1_title),
                getString(R.string.nonb_1_desc), R.drawable.onboarding_toped_animation,
                ContextCompat.getColor(getApplicationContext(), R.color.medium_green),
                OnBoardingFragment.VIEW_DEFAULT, 0));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_2_title),
                getString(R.string.nonb_2_desc), R.drawable.ic_yang_jual_banyak,
                ContextCompat.getColor(getApplicationContext(), R.color.light_blue_300),
                OnBoardingFragment.VIEW_DEFAULT, 1));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_3_title),
                getString(R.string.nonb_3_desc), R.drawable.ic_belanja_lebih_mudah,
                ContextCompat.getColor(getApplicationContext(), R.color.orange_300),
                OnBoardingFragment.VIEW_DEFAULT, 2));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_4_title),
                getString(R.string.nonb_4_desc), R.drawable.ic_yuk_belanja,
                ContextCompat.getColor(getApplicationContext(), R.color.medium_green),
                OnBoardingFragment.VIEW_DEFAULT, 3));
        addSlide(NewOnBoardingFragment.newInstance(getString(R.string.nonb_5_title),
                getString(R.string.nonb_5_desc), R.drawable.ic_yuk_belanja,
                ContextCompat.getColor(getApplicationContext(), R.color.orange_300),
                OnBoardingFragment.VIEW_ENDING, 4));
    }

    private void setStepper() {
        BounceInterpolator interpolator = new BounceInterpolator(0.15, 10);
        bounceAnimator = AnimatorInflater.loadAnimator(getBaseContext(), R.animator.bounce_animator);
        bounceAnimator.setInterpolator(interpolator);
        main = (RelativeLayout) pager.getParent();
        main.setId(R.id.main);
        isFirst = true;
        setStepperLayout();
        setSelectedStepper();
        setStepperClickAnimation();

    }

    private void setStepperClickAnimation() {
        listId = new Integer[]{R.id.step_1, R.id.step_2, R.id.step_3, R.id.step_4, R.id.step_5};
        for (int i = 0; i < listId.length; i++) {
            findViewById(listId[i]).setOnClickListener(goToSlide(i));
        }
    }

    private View.OnClickListener goToSlide(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(i);
            }
        };
    }

    private void setStepperLayout() {

        LinearLayout stepper = (LinearLayout) View.inflate(this, R.layout.step_list, null);
        stepper.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        float d = getResources().getDisplayMetrics().density;

        LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (stepper.getMeasuredHeight()+(10*d)));

        params1.setMargins((int) (70 * d), (int) (65 * d), (int) (70 * d), 0);
        params1.addRule(ALIGN_PARENT_TOP);

        stepper.setId(R.id.stepper);
        stepper.setLayoutParams(params1);
        main.addView(stepper);

    }

    private void setSelectedStepper() {

        Drawable drawable = MethodChecker.getDrawable(this, R.drawable.selected);
        final View view = findViewById(R.id.stepper);

        final ImageView selectStepper = new ImageView(this);
        selectStepper.setImageDrawable(drawable);


//        main.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                main.getViewTreeObserver().removeOnPreDrawListener(this);
//                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//                int centreX = (int) (view.getX() );
//                int centreY = (int) (view.getLeft() );
//
//                float d = getResources().getDisplayMetrics().density;
//                params.addRule(ALIGN_PARENT_TOP);
//                params.addRule(ALIGN_PARENT_LEFT);
//                params.setMargins(0, (int) (65 * d), 0, 0);
//
//                main.addView(selectStepper, params);
//
//                pager.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View viewX, MotionEvent motionEvent) {
//                        final View view = main.findViewById(R.id.step_1);
//                        Log.i("onTouch: main", String.valueOf(main.getX()) + " " + String.valueOf(main.getX()));
//                        Log.i("onTouch: step1", String.valueOf(view.getX()) + " " + String.valueOf(view.getX()));
//                        return false;
//                    }
//                });
//                return false;
//            }
//        });
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
                MethodChecker.getColor(this, R.color.medium_green)
                , MethodChecker.getColor(this, R.color.blue_nob)
                , MethodChecker.getColor(this, R.color.orange_nob)
                , MethodChecker.getColor(this, R.color.medium_green)
                , MethodChecker.getColor(this, R.color.orange_nob)};

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

        if (listId != null) {
            for (int i = 0; i < fragments.size(); i++) {
                if(i != pager.getCurrentItem()){
                    Log.i("onSlideChanged: ", pager.getCurrentItem() + " " +i);
                    findViewById(listId[i]).clearAnimation();
                    findViewById(listId[i]).setAlpha(0.65f);
                }
            }

            bounceAnimator.setTarget(findViewById(listId[pager.getCurrentItem()]));
            bounceAnimator.start();

            Log.i("onSlideChanged: ", pager.getCurrentItem() + " animator");
        }

        if (fragments != null && fragments.size() > 0 && pager != null && pager.getCurrentItem() == fragments.size()-1) {
            ((NewOnBoardingFragment) fragments.get(pager.getCurrentItem())).clearAnimation();
            ((NewOnBoardingFragment) fragments.get(pager.getCurrentItem())).playAnimation();
        }
        if (pager.getCurrentItem() == fragments.size() - 1) {
            setButtonVisibility(bottom, GONE);
            setButtonVisibility(skipButton, GONE);
            setButtonVisibility(indicator, GONE);
        } else {
            setButtonVisibility(bottom, VISIBLE);
            setButtonVisibility(skipButton, VISIBLE);
            setButtonVisibility(indicator, VISIBLE);
        }
    }

    private void setButtonVisibility(View view, int visible) {
        if (view != null) {
            view.setVisibility(visible);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        SessionHandler.setFirstTimeUser(this, false);
    }

    public void setNextResource() {
        nextView.setImageResource(R.drawable.next_ic);
        nextView.setMinimumWidth(0);
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) nextView.getLayoutParams();
        float d = getResources().getDisplayMetrics().density;
        params1.rightMargin = (int) (20*d);
    }

}
