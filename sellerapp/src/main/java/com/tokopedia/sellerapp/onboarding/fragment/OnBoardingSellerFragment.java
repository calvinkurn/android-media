package com.tokopedia.sellerapp.onboarding.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.onboarding.fragment.OnBoardingFragment;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.SellerRouterApplication;

public class OnBoardingSellerFragment extends OnBoardingFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_SUB_TITLE = "sub_title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_DRAWABLE = "drawable";
    private static final String ARG_BG_COLOR = "bg_color";
    private static final String ARG_TITLE_COLOR = "title_color";
    private static final String ARG_DESC_COLOR = "desc_color";
    private static final String ARG_VIEW_TYPE = "view_type";

    public static final int VIEW_DEFAULT = 100;
    public static final int VIEW_ENDING = 101;
    public static final int VIEW_FREE_RETURN = 102;
    private static final int REQUEST_ACTIVATE_PHONE_SELLER = 900;


    public static OnBoardingSellerFragment newInstance(CharSequence title, CharSequence subTitle, CharSequence description,
                                                       int imageDrawable, int bgColor, int viewType) {
        return newInstance(title, subTitle, description, imageDrawable, bgColor, 0, 0, viewType);
    }


    public static OnBoardingSellerFragment newInstance(CharSequence title, CharSequence subTitle, CharSequence description,
                                                       int imageDrawable, int bgColor,
                                                       int titleColor, int descColor,
                                                       int viewType) {
        OnBoardingSellerFragment sampleSlide = new OnBoardingSellerFragment();

        Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_SUB_TITLE, subTitle);
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
    private CharSequence title, subTitle, description;

    public OnBoardingSellerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().size() != 0) {
            drawable = getArguments().getInt(ARG_DRAWABLE);
            title = getArguments().getCharSequence(ARG_TITLE);
            subTitle = getArguments().getCharSequence(ARG_SUB_TITLE);
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
        View v = inflater.inflate(R.layout.fragment_onboarding_free_return, container, false);
        TextView d = (TextView) v.findViewById(R.id.description);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        View m = v.findViewById(R.id.main);

        d.setText(description);

        i.setBackgroundResource(drawable);
        if (i.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable notifAnimation = (AnimationDrawable) i.getBackground();
            notifAnimation.start();
        }
        m.setBackgroundColor(bgColor);
        return v;
    }

    protected View inflateEndingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboarding_intro_ending_seller, container, false);
        TextView t = (TextView) v.findViewById(R.id.title);
        TextView subTitleTextView = (TextView) v.findViewById(R.id.sub_title);
        TextView d = (TextView) v.findViewById(R.id.description);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        View m = v.findViewById(R.id.main);

        t.setText(title);
        subTitleTextView.setText(subTitle);
        if (titleColor != 0) {
            t.setTextColor(titleColor);
        }
        d.setText(description);
        i.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawable));
        m.setBackgroundColor(bgColor);

        Button register = (Button) v.findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                UnifyTracking.eventClickCreateShopOnBoardingSellerApp();
                if (SessionHandler.isMsisdnVerified()) {
                    intent = SellerRouter.getActivityShopCreateEdit(getContext());
                    startActivity(intent);
                    getActivity().finish();
                } else if (MainApplication.getAppContext() instanceof SellerModuleRouter){
                    intent =  ((SellerModuleRouter) MainApplication.getAppContext())
                            .getPhoneVerificationActivityIntent(getActivity());
                    startActivityForResult(intent, REQUEST_ACTIVATE_PHONE_SELLER);
                }
            }
        });
        return v;
    }

    protected View inflateDefaultView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboarding_intro_seller, container, false);
        TextView t = (TextView) v.findViewById(R.id.title);
        TextView subTitleTextView = (TextView) v.findViewById(R.id.sub_title);
        TextView d = (TextView) v.findViewById(R.id.description);
        ImageView i = (ImageView) v.findViewById(R.id.image);
        View m = v.findViewById(R.id.main);

        t.setText(title);
        subTitleTextView.setText(subTitle);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACTIVATE_PHONE_SELLER) {
            Intent intent = SellerRouter.getActivityShopCreateEdit(getActivity());
            startActivity(intent);
            getActivity().finish();
        }
    }
}
