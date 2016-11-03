package com.tokopedia.tkpd.onboarding.fragment;

/**
 * Created by hafizh HERDI on 3/21/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.session.presenter.SessionView;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.TkpdState;

public class OnBoardingFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_DRAWABLE = "drawable";
    private static final String ARG_BG_COLOR = "bg_color";
    private static final String ARG_TITLE_COLOR = "title_color";
    private static final String ARG_DESC_COLOR = "desc_color";
    private static final String ARG_VIEW_TYPE = "view_type";

    public static final int VIEW_DEFAULT = 100;
    public static final int VIEW_ENDING = 101;
    public static final int VIEW_FREE_RETURN = 102;

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

    private int drawable, bgColor, titleColor, descColor, viewType;
    private CharSequence title, description;

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

    private View inflateFreeReturnView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboarding_free_return, container, false);
        TextView d = (TextView) v.findViewById(R.id.description);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        RelativeLayout m = (RelativeLayout) v.findViewById(R.id.main);


        d.setText(description);

        i.setBackgroundResource(drawable);
        if (i.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable notifAnimation = (AnimationDrawable) i.getBackground();
            notifAnimation.start();
        }
        m.setBackgroundColor(bgColor);

        return v;
    }

    private View inflateEndingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboarding_intro_ending, container, false);
        TextView t = (TextView) v.findViewById(R.id.title);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        ImageView logo = (ImageView) v.findViewById(R.id.logo);
        RelativeLayout m = (RelativeLayout) v.findViewById(R.id.main);

        ImageHandler.loadImageWithId(logo, R.drawable.ic_tokopedia_logo_02);

        t.setText(title);
        if (titleColor != 0) {
            t.setTextColor(titleColor);
        }

        i.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawable));
        m.setBackgroundColor(bgColor);

        Button login = (Button) v.findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUser(getActivity(), false);
                Intent intent = new Intent();
                intent.putExtra(com.tokopedia.tkpd.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();

            }
        });

        Button register = (Button) v.findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUser(getActivity(), false);
                Intent intent = new Intent();
                intent.putExtra(com.tokopedia.tkpd.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.REGISTER);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();


            }
        });

        TextView search = (TextView) v.findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setFirstTimeUser(getActivity(), false);
                Intent intent = new Intent(getActivity(), ParentIndexHome.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }

    private View inflateDefaultView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboarding_intro, container, false);
        TextView t = (TextView) v.findViewById(R.id.title);
        TextView d = (TextView) v.findViewById(R.id.description);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        ImageView logo = (ImageView) v.findViewById(R.id.logo);
        LinearLayout m = (LinearLayout) v.findViewById(R.id.main);

        ImageHandler.loadImageWithId(logo, R.drawable.ic_tokopedia_logo_02);

        t.setText(title);
        if (titleColor != 0) {
            t.setTextColor(titleColor);
        }

        d.setText(description);

        i.setBackgroundResource(drawable);
        if (i.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable notifAnimation = (AnimationDrawable) i.getBackground();
            notifAnimation.start();
        }
        m.setBackgroundColor(bgColor);

        return v;
    }

}

