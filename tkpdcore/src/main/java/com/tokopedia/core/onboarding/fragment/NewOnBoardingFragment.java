package com.tokopedia.core.onboarding.fragment;

/**
 * Created by hafizh HERDI on 3/21/2016.
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.core.R;
import com.tokopedia.core.onboarding.CustomAnimationPageTransformerDelegate;
import com.tokopedia.core.onboarding.NewOnboardingActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.DEFAULT_ANIMATION_DURATION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.DOWN_DIRECTION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.UP_DIRECTION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.appearText;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.expandTextView;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.fadeText;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.setVisibilityGone;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideReverseX;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideToX;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideToY;

public class NewOnBoardingFragment extends OnBoardingFragment implements CustomAnimationPageTransformerDelegate {

    private static final String ARG_LOTTIE = "lottie";
    private int mScreenWidth;
    private int mScreenHeight;
    private TextView login;
    private ValueAnimator expandAnimator;
    private ObjectAnimator fadeAnimator;
    private AnimatorSet animatorSet;
    private ObjectAnimator slideAnimator;
    private ValueAnimator slideAnimator2;
    private TextView skip;
    private ObjectAnimator fadeAnimator2;
    private ValueAnimator slideAnimatorX;
    private ObjectAnimator goneAnimation;
    private View footer;
    private View next;
    private LottieAnimationView lottieAnimationView;
    private TextView descView;
    private ImageView i;
    private TextView titleView;
    private int position;
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
            case VIEW_FREE_RETURN:
                return inflateFreeReturnView(inflater, container, savedInstanceState);
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
        position = getArguments().getInt(ARG_POSITION);
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
        mScreenHeight = displaymetrics.heightPixels;
    }

    @Override
    protected View inflateDefaultView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getDefaultView(inflater, container);
        main = v.findViewById(R.id.main);
        lottieAnimationView = (LottieAnimationView) v.findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation(lottieAsset, LottieAnimationView.CacheStrategy.Strong);


        titleView = (TextView) v.findViewById(R.id.title);
        descView = (TextView) v.findViewById(R.id.description);
        i = (ImageView) v.findViewById(R.id.image);
        main = v.findViewById(R.id.main);


        titleView.setText(title);
        if (titleColor != 0) {
            titleView.setTextColor(titleColor);
        }

        descView.setText(description);

        i.setBackgroundResource(drawable);
        if (i.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable notifAnimation = (AnimationDrawable) i.getBackground();
            notifAnimation.start();
        }
        main.setBackgroundColor(bgColor);
        return v;
    }

    @Override
    protected View inflateEndingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getEndingView(inflater, container);
        titleView = (TextView) v.findViewById(R.id.title);
        i = (ImageView) v.findViewById(R.id.image);
        descView = (TextView) v.findViewById(R.id.description);
        main = v.findViewById(R.id.main);
        lottieAnimationView = (LottieAnimationView) v.findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation(lottieAsset, LottieAnimationView.CacheStrategy.Strong);
        lottieAnimationView.playAnimation();

        titleView.setText(title);
        if (titleColor != 0) {
            titleView.setTextColor(titleColor);
        }

        descView.setText(description);

        main.setBackgroundColor(bgColor);

        login = (TextView) v.findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUserNewOnboard(getActivity(), false);
                Intent intent = new Intent();
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        skip = (TextView) v.findViewById(R.id.button_register);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUserNewOnboard(getActivity(), false);
                Intent intent = new Intent(getActivity(), HomeRouter.getHomeActivityClass());
                startActivity(intent);
                getActivity().finish();
            }
        });

        footer = v.findViewById(R.id.footer);
        return v;
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
            login.clearAnimation();
            skip.clearAnimation();
        }

    }

    @Override
    public void onPageSelected() {
        playLottie();
        clearAnimation();
        lottieAnimationView.setVisibility(View.VISIBLE);
        playAnimation();
    }

    @Override
    public void onPageScrolled(View page, float position) {
        if (position < -1) {
            main.setAlpha(0);

        } else if (position <= 1) {
            // Counteract the default slide transition
            page.setTranslationX(page.getWidth() * -position);

            // set Y position to swipe in from top
            float yPosition = position * page.getHeight();
            page.setTranslationY(yPosition);

        } else {
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
    }

    public void playLottie() {
        if (lottieAnimationView != null)
            lottieAnimationView.playAnimation();
    }

    public void playAnimation() {
        final int viewType = getArguments().getInt(ARG_VIEW_TYPE);

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

            next = getView().findViewById(R.id.dummy_next);
            resetAnimation();

            slideAnimatorX = slideToX(next, -1, 0, mScreenWidth / 2);
            goneAnimation = setVisibilityGone(next);
            expandAnimator = expandTextView(login, mScreenWidth);
            fadeAnimator = fadeText(login, getActivity(), R.color.transparent, R.color.medium_green);
            slideAnimator = slideToY(login, UP_DIRECTION);

            fadeAnimator2 = fadeText(skip, getActivity(), R.color.transparent, R.color.white);
            slideAnimator2 = slideToY(skip, DOWN_DIRECTION);


            goneAnimation.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.25));
            expandAnimator.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.5));
            fadeAnimator.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.5));

            slideAnimator.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.5));
            fadeAnimator2.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.75));
            slideAnimator2.setStartDelay((long) (DEFAULT_ANIMATION_DURATION * 0.75));

            animatorSet.playTogether(slideAnimatorX, goneAnimation, expandAnimator, fadeAnimator, slideAnimator, fadeAnimator2, slideAnimator2);

            if (!animatorSet.isRunning()) {
                animatorSet.start();
            }
        }
    }

    private void resetAnimation() {
        next.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = login.getLayoutParams();
        layoutParams.width = 0;
        login.setLayoutParams(layoutParams);
        login.setTextColor(MethodChecker.getColor(getActivity(), R.color.transparent));
        skip.setTextColor(MethodChecker.getColor(getActivity(), R.color.transparent));
    }

    public void clearAnimation() {
        if (animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        if (viewType == VIEW_ENDING) {
            login.setVisibility(View.INVISIBLE);
            skip.setVisibility(View.INVISIBLE);
        }
    }

    public void animateOut() {
        lottieAnimationView.setProgress(0.5f);
        lottieAnimationView.reverseAnimation();
        ValueAnimator slideTitle = slideToX(titleView, 1, 0, mScreenWidth);
        slideTitle.start();
        ValueAnimator slideDesc = slideToX(descView, 1, 0, mScreenWidth);
        slideDesc.start();
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(slideTitle, slideDesc);
        animatorSet.start();
    }
}

