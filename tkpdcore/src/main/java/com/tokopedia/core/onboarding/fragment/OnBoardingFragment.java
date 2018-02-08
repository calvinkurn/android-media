package com.tokopedia.core.onboarding.fragment;

/**
 * Created by hafizh HERDI on 3/21/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.onboarding.ISlideBackgroundColorHolder;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

public class OnBoardingFragment extends TkpdBaseV4Fragment implements ISlideBackgroundColorHolder{

    protected static final String ARG_TITLE = "title";
    protected static final String ARG_DESC = "desc";
    protected static final String ARG_DRAWABLE = "drawable";
    protected static final String ARG_BG_COLOR = "bg_color";
    protected static final String ARG_TITLE_COLOR = "title_color";
    protected static final String ARG_DESC_COLOR = "desc_color";
    protected static final String ARG_VIEW_TYPE = "view_type";
    protected static final String ARG_POSITION = "position";

    public static final int VIEW_DEFAULT = 100;
    public static final int VIEW_ENDING = 101;
    public static final int VIEW_FREE_RETURN = 102;
    protected View main;

    public static OnBoardingFragment newInstance(CharSequence title, CharSequence description,
                                                 int imageDrawable, int bgColor, int viewType) {
        return newInstance(title, description, imageDrawable, bgColor, 0, 0, viewType);
    }


    public static OnBoardingFragment newInstance(CharSequence title, CharSequence description,
                                                 int imageDrawable, int bgColor,
                                                 int titleColor, int descColor,
                                                 int viewType) {
        OnBoardingFragment sampleSlide = new OnBoardingFragment();

        Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_DESC, description);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        args.putInt(ARG_VIEW_TYPE, viewType);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    protected int drawable, bgColor, titleColor, descColor, viewType;
    protected CharSequence title, description;

    public OnBoardingFragment() {
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

    protected View inflateFreeReturnView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View freeReturnView = inflater.inflate(R.layout.fragment_onboarding_free_return, container, false);
        TextView description = (TextView) freeReturnView.findViewById(R.id.description);
        ImageView image = (ImageView) freeReturnView.findViewById(R.id.image);
        main = freeReturnView.findViewById(R.id.main);


        description.setText(this.description);

        image.setBackgroundResource(drawable);
        if (image.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable notifAnimation = (AnimationDrawable) image.getBackground();
            notifAnimation.start();
        }
        main.setBackgroundColor(bgColor);

        return freeReturnView;
    }

    protected View inflateEndingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View endingView = getEndingView(inflater, container);
        TextView title = (TextView) endingView.findViewById(R.id.title);
        ImageView image = (ImageView) endingView.findViewById(R.id.image);
        TextView description = (TextView) endingView.findViewById(R.id.description);
        main = endingView.findViewById(R.id.main);

        title.setText(this.title);
        if (titleColor != 0) {
            title.setTextColor(titleColor);
        }

        description.setText(this.description);

        image.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawable));
        main.setBackgroundColor(bgColor);

        Button login = (Button) endingView.findViewById(R.id.button_start_now);
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

        Button register = (Button) endingView.findViewById(R.id.button_register);
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

        TextView search = (TextView) endingView.findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUser(getActivity(), false);
                Intent intent = new Intent(getActivity(), HomeRouter.getHomeActivityClass());
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        return endingView;
    }

    protected View getEndingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_onboarding_intro_ending, container, false);
    }

    protected View inflateDefaultView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View defaultView = getDefaultView(inflater,container);
        TextView title = (TextView) defaultView.findViewById(R.id.title);
        TextView description = (TextView) defaultView.findViewById(R.id.description);
        ImageView image = (ImageView) defaultView.findViewById(R.id.image);
        main = defaultView.findViewById(R.id.main);


        title.setText(this.title);
        if (titleColor != 0) {
            title.setTextColor(titleColor);
        }

        description.setText(this.description);

        image.setBackgroundResource(drawable);
        if (image.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable notifAnimation = (AnimationDrawable) image.getBackground();
            notifAnimation.start();
        }
        main.setBackgroundColor(bgColor);
        return defaultView;
    }

    protected View getDefaultView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_onboarding_intro, container, false);
    }

    @Override
    public int getDefaultBackgroundColor() {
        // Return the default background color of the slide.
        return MethodChecker.getColor(getActivity(),R.color.black);
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        // Set the background color of the view within your slide to which the transition should be applied.
        if (main != null) {
            main.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    protected String getScreenName() {
        return "Screen OnBoarding";
    }
}

