package com.tokopedia.core.onboarding.fragment;

/**
 * Created by hafizh HERDI on 3/21/2016.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.onboarding.CustomAnimationPageTransformerDelegate;
import com.tokopedia.core.onboarding.NewOnboardingActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;

import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.DEFAULT_ANIMATION_DURATION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.appearText;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.expandTextView;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.fadeText;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.setVisibilityGone;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideReverseX;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideToX;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideToXFromCurrentHeight;

public class NewOnBoardingFragment extends OnBoardingFragment implements CustomAnimationPageTransformerDelegate {

    private static final String ARG_LOTTIE = "lottie";
    private static final String SCREEN_NAME = "Screen OnBoarding - ";
    private int mScreenWidth;
    private TextView startNow;
    private ValueAnimator expandAnimator;
    private ObjectAnimator fadeAnimator;
    private AnimatorSet animatorSet;
    private ValueAnimator slideAnimatorX;
    private ObjectAnimator goneAnimation;
    private View next;
    private LottieAnimationView lottieAnimationView;
    private TextView descView;
    private ImageView imageView;
    private TextView titleView;
    private int position;
    private boolean isAnimationPlayed = false;
    private String lottieAsset;


    public static NewOnBoardingFragment newInstance(CharSequence title, CharSequence description,
                                                    String assetName, int bgColor, int viewType, int position) {
        return newInstance(title, description, assetName, bgColor, 0, 0, viewType, position);
    }

    public static NewOnBoardingFragment newInstance(CharSequence title, CharSequence description,
                                                    String assetName, int bgColor,
                                                    int titleColor, int descColor,
                                                    int viewType, int position) {
        NewOnBoardingFragment sampleSlide = new NewOnBoardingFragment();

        Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_DESC, description);
        args.putString(ARG_LOTTIE, assetName);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        args.putInt(ARG_VIEW_TYPE, viewType);
        args.putInt(ARG_POSITION, position);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NewOnboardingActivity) (getActivity())).setNextResource();
        animatorSet = new AnimatorSet();

        if (getArguments() != null && getArguments().size() != 0) {
            position = getArguments().getInt(ARG_POSITION);
            lottieAsset = getArguments().getString(ARG_LOTTIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (viewType) {
            case VIEW_ENDING:
                return inflateEndingView(inflater, container, savedInstanceState);
            default:
                return inflateDefaultView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        view.setTag(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenWidth = displaymetrics.widthPixels;
    }

    @Override
    protected View inflateDefaultView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View defaultView = getDefaultView(inflater, container);
        main = defaultView.findViewById(R.id.main);
        lottieAnimationView = (LottieAnimationView) defaultView.findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation(lottieAsset, LottieAnimationView.CacheStrategy.Strong);


        titleView = (TextView) defaultView.findViewById(R.id.title);
        descView = (TextView) defaultView.findViewById(R.id.description);
        imageView = (ImageView) defaultView.findViewById(R.id.image);
        main = defaultView.findViewById(R.id.main);


        titleView.setText(title);
        if (titleColor != 0) {
            titleView.setTextColor(titleColor);
        }
        descView.setText(Html.fromHtml(description.toString()));

        imageView.setBackgroundResource(drawable);
        if (imageView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable notifAnimation = (AnimationDrawable) imageView.getBackground();
            notifAnimation.start();
        }
        main.setBackgroundColor(bgColor);

        return defaultView;
    }

    @Override
    protected View inflateEndingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View endingView = getEndingView(inflater, container);
        titleView = (TextView) endingView.findViewById(R.id.title);
        imageView = (ImageView) endingView.findViewById(R.id.image);
        descView = (TextView) endingView.findViewById(R.id.description);
        main = endingView.findViewById(R.id.main);
        lottieAnimationView = (LottieAnimationView) endingView.findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation(lottieAsset, LottieAnimationView.CacheStrategy.Strong);
        lottieAnimationView.playAnimation();

        titleView.setText(title);
        if (titleColor != 0) {
            titleView.setTextColor(titleColor);
        }

        descView.setText(Html.fromHtml(description.toString()));

        main.setBackgroundColor(bgColor);

        startNow = (TextView) endingView.findViewById(R.id.button_start_now);
        startNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUserNewOnboard(getActivity(), false);
                UnifyTracking.eventOnboardingStartNow();
                Intent intent = new Intent(getActivity(), HomeRouter.getHomeActivityClass());
                startActivity(intent);
                getActivity().finish();
            }
        });
        startNow.setVisibility(View.GONE);
        startNow.setWidth(0);
        return endingView;
    }

    @Override
    protected View getEndingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_onboarding_intro_ending_new, container, false);
    }

    @Override
    protected View getDefaultView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_onboarding_intro_new, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewType == VIEW_ENDING) {
            startNow.clearAnimation();
        }

    }

    @Override
    public void onPageSelected() {
        if (!isAnimationPlayed) {
            isAnimationPlayed = true;
            playLottie();
            clearAnimation();
            lottieAnimationView.setVisibility(View.VISIBLE);
            playAnimation();
        }
    }

    @Override
    public void onPageScrolled(View page, float position) {
        if (position < -1) {
            main.setAlpha(0);

        } else if (position > 1) {
            main.setAlpha(0);

            titleView.setTranslationX(titleView.getWidth() * -position);
            descView.setTranslationX(descView.getWidth() * -position);
            titleView.setTranslationY(page.getHeight() * -position);
            descView.setTranslationY(page.getHeight() * -position);
        }

    }

    @Override
    public void onPageInvisible(float position) {
        clearAnimation();
        lottieAnimationView.setVisibility(View.INVISIBLE);
        titleView.setVisibility(View.INVISIBLE);
        descView.setVisibility(View.INVISIBLE);
        if (viewType == VIEW_ENDING) {
            startNow.setVisibility(View.GONE);
            startNow.setWidth(0);
        }
        isAnimationPlayed = false;
    }

    public void playLottie() {
        if (lottieAnimationView != null)
            lottieAnimationView.playAnimation();
    }

    public void playAnimation() {
        if (viewType == VIEW_ENDING) {
            next = getView().findViewById(R.id.dummy_next);
            resetAnimation();
        }

        titleView.setVisibility(View.VISIBLE);
        descView.setVisibility(View.VISIBLE);

        ValueAnimator slideTitle = slideReverseX(titleView);
        ValueAnimator slideDesc = slideReverseX(descView);

        ValueAnimator fadeTitle = appearText(titleView);
        ValueAnimator fadeDesc = appearText(descView);
        slideDesc.setStartDelay(100L);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(slideTitle, slideDesc, fadeTitle, fadeDesc);
        set.setDuration(1000L);
        set.start();

        if (viewType == VIEW_ENDING) {
            slideAnimatorX = slideToXFromCurrentHeight(next, -1, mScreenWidth / 2);
            goneAnimation = setVisibilityGone(next);
            expandAnimator = expandTextView(startNow, mScreenWidth);
            fadeAnimator = fadeText(startNow, getActivity(), R.color.transparent, R.color.blue_nob);

            goneAnimation.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.25));
            expandAnimator.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.5));
            fadeAnimator.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.5));


            animatorSet.playTogether(slideAnimatorX, goneAnimation, expandAnimator, fadeAnimator);

            if (!animatorSet.isRunning()) {
                animatorSet.start();
            }
        }
    }

    private void resetAnimation() {
        ViewGroup.LayoutParams layoutParams = startNow.getLayoutParams();
        layoutParams.width = 0;
        startNow.setLayoutParams(layoutParams);
        startNow.setTextColor(MethodChecker.getColor(getActivity(), R.color.transparent));
        startNow.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
    }

    public void clearAnimation() {
        if (animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        if (viewType == VIEW_ENDING) {
            startNow.setVisibility(View.INVISIBLE);
        }
    }

    public void animateOut() {
        try {
            ValueAnimator slideTitle = slideToX(titleView, 1, 0, mScreenWidth);
            ValueAnimator slideDesc = slideToX(descView, 1, 0, mScreenWidth);
            animatorSet.playTogether(slideTitle, slideDesc);
            if (!animatorSet.isRunning()) {
                animatorSet.start();

                lottieAnimationView.setProgress(0.4f);
                lottieAnimationView.reverseAnimation();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getScreenName() {
        int pageNumber = position + 1;
        return SCREEN_NAME + pageNumber;
    }
}

