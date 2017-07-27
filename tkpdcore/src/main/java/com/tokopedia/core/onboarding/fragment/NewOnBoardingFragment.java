package com.tokopedia.core.onboarding.fragment;

/**
 * Created by hafizh HERDI on 3/21/2016.
 */

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.onboarding.ISlideBackgroundColorHolder;
import com.tokopedia.core.onboarding.OnboardingActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.DEFAULT_ANIMATION_DURATION;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.expandTextView;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.fadeText;
import static com.tokopedia.core.onboarding.animation.OnboardingAnimation.slideToAbove;

public class NewOnBoardingFragment extends OnBoardingFragment {

    private int mScreenWidth;
    private TextView login;
    private ValueAnimator expandAnimator;
    private ObjectAnimator fadeAnimator;
    private AnimatorSet animatorSet;
    private Animation slideAnimator;

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
//        ((OnboardingActivity) getActivity()).showSkipButton(true);
//        ((OnboardingActivity) getActivity()).setSkipText("LEWATI");
        if (getArguments() != null && getArguments().size() != 0) {
//            ((OnboardingActivity) getActivity()).setSeparatorColor(MethodChecker.getColor(getActivity(), bgColor));
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final int position = getArguments().getInt(ARG_POSITION);

        view.setTag(position);
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
    protected View inflateEndingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getEndingView(inflater, container);
        TextView t = (TextView) v.findViewById(R.id.title);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        TextView d = (TextView) v.findViewById(R.id.description);
        main = v.findViewById(R.id.main);

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
                SessionHandler.setFirstTimeUser(getActivity(), false);
                Intent intent = new Intent();
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();

            }
        });

        TextView register = (TextView) v.findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUser(getActivity(), false);
                Intent intent = new Intent();
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.REGISTER);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();


            }
        });
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
        login.setTextColor(MethodChecker.getColor(getActivity(), R.color.transparent));
        if(viewType == VIEW_ENDING){
            expandAnimator = expandTextView(login, mScreenWidth);
            fadeAnimator = fadeText(login, getActivity(), R.color.transparent, R.color.medium_green);
//            slideAnimator = slideToAbove(login);
//            slideAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
//            slideAnimator.start();

            animatorSet = new AnimatorSet();

            animatorSet.playSequentially(expandAnimator, fadeAnimator);
            animatorSet.setDuration(DEFAULT_ANIMATION_DURATION);
            animatorSet.start();
        }
    }
}

