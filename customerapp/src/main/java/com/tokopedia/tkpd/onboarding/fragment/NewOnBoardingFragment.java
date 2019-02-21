package com.tokopedia.tkpd.onboarding.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.onboarding.util.CustomAnimationPageTransformerDelegate;
import com.tokopedia.tkpd.ConsumerRouterApplication;
import com.tokopedia.tkpd.onboarding.analytics.ConsumerOnboardingAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import static com.tokopedia.onboarding.animation.OnboardingAnimation.DEFAULT_ANIMATION_DURATION;
import static com.tokopedia.onboarding.animation.OnboardingAnimation.appearText;
import static com.tokopedia.onboarding.animation.OnboardingAnimation.expandTextView;
import static com.tokopedia.onboarding.animation.OnboardingAnimation.fadeText;
import static com.tokopedia.onboarding.animation.OnboardingAnimation.setVisibilityGone;
import static com.tokopedia.onboarding.animation.OnboardingAnimation.slideReverseX;
import static com.tokopedia.onboarding.animation.OnboardingAnimation.slideToX;
import static com.tokopedia.onboarding.animation.OnboardingAnimation.slideToXFromCurrentHeight;

/**
 * Created by hafizh HERDI on 3/21/2016.
 */
public class NewOnBoardingFragment extends Fragment implements CustomAnimationPageTransformerDelegate {

    private static final String ARG_LOTTIE = "lottie";
    private static final String SCREEN_NAME = "Screen OnBoarding - ";
    private int mScreenWidth;
    private TextView startNow;
    private AnimatorSet animatorSet;
    private View next;
    private LottieAnimationView lottieAnimationView;
    private TextView descView;
    private ImageView imageView;
    private TextView titleView;
    private int position;

    private boolean isAnimationPlayed = false;
    private String lottieAsset;
    private ConsumerOnboardingAnalytics analytics;
    private UserSessionInterface userSession;

    protected static final String ARG_TITLE = "title";
    protected static final String ARG_DRAWABLE = "drawable";
    protected static final String ARG_DESC = "desc";
    protected static final String ARG_BG_COLOR = "bg_color";
    protected static final String ARG_TITLE_COLOR = "title_color";
    protected static final String ARG_DESC_COLOR = "desc_color";
    protected static final String ARG_VIEW_TYPE = "view_type";
    protected static final String ARG_POSITION = "position";

    public static final int VIEW_DEFAULT = 100;
    public static final int VIEW_ENDING = 101;
    protected View main;
    protected int drawable;
    protected int bgColor;
    protected int titleColor;
    protected int viewType;
    protected int descColor;
    protected CharSequence title, description;

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
        if (getArguments() != null && getArguments().size() != 0) {
            drawable = getArguments().getInt(ARG_DRAWABLE);
            title = getArguments().getCharSequence(ARG_TITLE);
            description = getArguments().getCharSequence(ARG_DESC);
            bgColor = getArguments().getInt(ARG_BG_COLOR);
            titleColor = getArguments().containsKey(ARG_TITLE_COLOR) ? getArguments().getInt(ARG_TITLE_COLOR) : 0;
            descColor = getArguments().containsKey(ARG_DESC_COLOR) ? getArguments().getInt(ARG_DESC_COLOR) : 0;
            viewType = descColor = getArguments().containsKey(ARG_VIEW_TYPE) ? getArguments().getInt(ARG_VIEW_TYPE) : VIEW_DEFAULT;
        }

        analytics = new ConsumerOnboardingAnalytics();
        animatorSet = new AnimatorSet();
        if (getActivity() != null) {
            userSession = new UserSession(getActivity().getApplicationContext());
        }

        if (getArguments() != null && getArguments().size() != 0) {
            position = getArguments().getInt(ARG_POSITION);
            lottieAsset = getArguments().getString(ARG_LOTTIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (viewType) {
            case VIEW_ENDING:
                return inflateEndingView(inflater, container);
            default:
                return inflateDefaultView(inflater, container);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        view.setTag(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            mScreenWidth = displaymetrics.widthPixels;
        }
    }

    protected View inflateDefaultView(LayoutInflater inflater, ViewGroup container) {
        View defaultView = getDefaultView(inflater, container);
        main = defaultView.findViewById(R.id.main);
        lottieAnimationView = defaultView.findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation(lottieAsset, LottieAnimationView.CacheStrategy.Strong);


        titleView = defaultView.findViewById(R.id.title);
        descView = defaultView.findViewById(R.id.description);
        imageView = defaultView.findViewById(R.id.image);
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

    protected View inflateEndingView(LayoutInflater inflater, ViewGroup container) {
        View endingView = getEndingView(inflater, container);
        titleView = endingView.findViewById(R.id.title);
        imageView = endingView.findViewById(R.id.image);
        descView = endingView.findViewById(R.id.description);
        main = endingView.findViewById(R.id.main);
        lottieAnimationView = endingView.findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation(lottieAsset, LottieAnimationView.CacheStrategy.Strong);
        lottieAnimationView.playAnimation();

        titleView.setText(title);
        if (titleColor != 0) {
            titleView.setTextColor(titleColor);
        }

        descView.setText(Html.fromHtml(description.toString()));

        main.setBackgroundColor(bgColor);

        startNow = endingView.findViewById(R.id.button_start_now);
        startNow.setOnClickListener(v -> {
            userSession.setFirstTimeUserOnboarding(false);
            if (getActivity() != null) {
                analytics.eventOnboardingStartNow(getActivity().getApplicationContext());
                Intent intent = ((ConsumerRouterApplication) getActivity().getApplicationContext())
                        .getHomeIntent
                        (getActivity());
                if (!TextUtils.isEmpty(((ConsumerRouterApplication) getActivity()
                	.getApplicationContext()).getDefferedDeeplinkPathIfExists())) {
                    intent.putExtra("EXTRA_APPLINK", ((ConsumerRouterApplication) getActivity().getApplicationContext())
                        .getDefferedDeeplinkPathIfExists());
                }
                startActivity(intent);
                getActivity().finish();
            }
        });
        startNow.setVisibility(View.GONE);
        startNow.setWidth(0);
        return endingView;
    }

    protected View getEndingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_onboarding_intro_ending_new, container, false);
    }

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
        if (viewType == VIEW_ENDING && getView() != null) {
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
            ValueAnimator slideAnimatorX = slideToXFromCurrentHeight(next, -1, mScreenWidth / 2);
            ObjectAnimator goneAnimation = setVisibilityGone(next);
            ValueAnimator expandAnimator = expandTextView(startNow, mScreenWidth);
            ObjectAnimator fadeAnimator = fadeText(startNow, getActivity(), R.color.transparent, R.color.blue_nob);

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

    protected String getScreenName() {
        int pageNumber = position + 1;
        return SCREEN_NAME + pageNumber;
    }
}

