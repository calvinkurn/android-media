package com.tokopedia.core.onboarding.fragment;

/**
 * Created by hafizh HERDI on 3/21/2016.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.onboarding.OnboardingActivity;
import com.tokopedia.core.util.MethodChecker;

import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.DEFAULT_ANIMATION_DURATION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.DOWN_DIRECTION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.UP_DIRECTION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.expandTextView;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.fadeText;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.setVisibilityGone;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideToX;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideToY;

public class NewOnBoardingFragment extends OnBoardingFragment {

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
    private LinearLayout stepper;
    private ValueAnimator slideAnimatorX;
    private ObjectAnimator goneAnimation;
    private View footer;

    public static NewOnBoardingFragment newInstance(CharSequence title, CharSequence description,
                                                    int imageDrawable, int bgColor, int viewType, int position) {
        return newInstance(title, description, imageDrawable, bgColor, 0, 0, viewType, position);
    }

    public static NewOnBoardingFragment newInstance(CharSequence title, CharSequence description,
                                                    int imageDrawable, int bgColor,
                                                    int titleColor, int descColor,
                                                    int viewType, int position) {
        NewOnBoardingFragment sampleSlide = new NewOnBoardingFragment();

        Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_DESC, description);
        args.putInt(ARG_DRAWABLE, imageDrawable);
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
        ((OnboardingActivity)(getActivity())).setNextResource();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final int position = getArguments().getInt(ARG_POSITION);
        view.setTag(position);
//        stepper.getChildAt(position).setAlpha(1.00f);
//        stepper.requestLayout();
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
//        stepper = (LinearLayout) v.findViewById(R.id.stepper);
        return super.inflateDefaultView(inflater, container, savedInstanceState);
    }

    @Override
    protected View inflateEndingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getEndingView(inflater, container);
        TextView t = (TextView) v.findViewById(R.id.title);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        TextView d = (TextView) v.findViewById(R.id.description);
        main = v.findViewById(R.id.main);
//        stepper = (LinearLayout) v.findViewById(R.id.stepper);

        t.setText(title);
        if (titleColor != 0) {
            t.setTextColor(titleColor);
        }

        d.setText(description);

        i.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawable));
        main.setBackgroundColor(bgColor);

        login = (TextView) v.findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SessionHandler.setFirstTimeUser(getActivity(), false);
//                Intent intent = new Intent();
//                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
//                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
//                getActivity().setResult(Activity.RESULT_OK, intent);
//                getActivity().finish();
                Toast.makeText(v.getContext(), "login", Toast.LENGTH_LONG).show();
            }
        });

        skip = (TextView) v.findViewById(R.id.button_register);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SessionHandler.setFirstTimeUser(getActivity(), false);
//                Intent intent = new Intent();
//                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.REGISTER);
//                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
//                getActivity().setResult(Activity.RESULT_OK, intent);
//                getActivity().finish();
                Toast.makeText(v.getContext(), "skip", Toast.LENGTH_LONG).show();
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


    public void playAnimation() {
        final int viewType = getArguments().getInt(ARG_VIEW_TYPE);

        if(viewType == VIEW_ENDING){
            login.setTextColor(MethodChecker.getColor(getActivity(), R.color.transparent));

            View next = getView().findViewById(R.id.dummy_next);
            slideAnimatorX = slideToX(next, -1, mScreenWidth/2);
            goneAnimation = setVisibilityGone(next);
            expandAnimator = expandTextView(login, mScreenWidth);
            fadeAnimator = fadeText(login, getActivity(), R.color.transparent, R.color.medium_green);
            slideAnimator = slideToY(login, UP_DIRECTION, footer);

            fadeAnimator2 = fadeText(skip, getActivity(), R.color.transparent, R.color.white);
            slideAnimator2 = slideToY(skip, DOWN_DIRECTION, footer);


            goneAnimation.setStartDelay((long) (DEFAULT_ANIMATION_DURATION*0.75));
            expandAnimator.setStartDelay(DEFAULT_ANIMATION_DURATION);
            fadeAnimator.setStartDelay(DEFAULT_ANIMATION_DURATION);

            slideAnimator.setStartDelay(DEFAULT_ANIMATION_DURATION*2);
            fadeAnimator2.setStartDelay((long)(DEFAULT_ANIMATION_DURATION * 2.5));
            slideAnimator2.setStartDelay((long)(DEFAULT_ANIMATION_DURATION * 2.5));

            animatorSet = new AnimatorSet();
            animatorSet.playTogether(slideAnimatorX, goneAnimation, expandAnimator, fadeAnimator, slideAnimator, fadeAnimator2, slideAnimator2);
            animatorSet.setDuration(DEFAULT_ANIMATION_DURATION);
            animatorSet.start();
        }
        else {
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(viewType == VIEW_ENDING) {
            login.clearAnimation();
            skip.clearAnimation();
        }

    }
}

