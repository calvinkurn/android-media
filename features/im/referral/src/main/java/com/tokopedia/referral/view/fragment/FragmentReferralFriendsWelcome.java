package com.tokopedia.referral.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.R;
import com.tokopedia.referral.di.ReferralComponent;
import com.tokopedia.referral.ReferralRouter;
import com.tokopedia.referral.view.listener.FriendsWelcomeView;
import com.tokopedia.referral.view.presenter.ReferralFriendsWelcomePresenter;

import javax.inject.Inject;

/**
 * Created by ashwanityagi on 06/12/17.
 */

public class FragmentReferralFriendsWelcome extends BaseDaggerFragment implements FriendsWelcomeView{
    private TextView btnReferralExplore;
    private TextView welcomeMessageSubHearer;
    private TextView TextViewHelpLink;
    private TextView referralCodeTextView;
    private ImageView imgTick;
    private TextView btnCopyReferralCode;

    @Inject
    ReferralFriendsWelcomePresenter presenter;


    public static FragmentReferralFriendsWelcome newInstance() {
        FragmentReferralFriendsWelcome fragmentFriendsWelcome = new FragmentReferralFriendsWelcome();
        return fragmentFriendsWelcome;
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(false);
    }


    protected void initialPresenter() {
        presenter.attachView(this);
        presenter.initialize();
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_referral_welcome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(getFragmentLayout(), container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initInjector();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialPresenter();
        initView(view);
    }

    protected void initView(View view) {
        referralCodeTextView = view.findViewById(R.id.txtv_referral_code);
        btnCopyReferralCode = view.findViewById(R.id.btn_copy);
        imgTick = view.findViewById(R.id.img_check);
        btnReferralExplore = view.findViewById(R.id.btn_referral_explore);
        welcomeMessageSubHearer = view.findViewById(R.id.tv_referral_subheader);
        TextViewHelpLink = view.findViewById(R.id.tv_referral_help_link);;

        referralCodeTextView.setText(getActivity().getIntent().getStringExtra(Constants.Key.Companion.CODE));

        btnReferralExplore.setOnClickListener(v -> {
            ((ReferralRouter)getActivity().getApplicationContext()).eventReferralAndShare(getActivity(),
                    Constants.Action.Companion.CLICK_EXPLORE_TOKOPEDIA, Constants.EventLabel.Companion.HOME);
            closeView();
        });
        btnCopyReferralCode.setOnClickListener(v -> {
            clickOnCopyButton();
        });
        TextViewHelpLink.setVisibility(presenter.isShowReferralHelpLink() ? View.VISIBLE : View.GONE);
        TextViewHelpLink.setText(Html.fromHtml(presenter.getHowItWorks()));
        TextViewHelpLink.setOnClickListener(view1 -> {
            ((ReferralRouter)getActivity().getApplicationContext()).eventReferralAndShare(getActivity(),
                    Constants.Action.Companion.CLICK_KNOW_MORE,"");
            showOnBoardingTooltip(presenter.getHelpButtonContentTitle(), presenter.getHelpButtonContentSubtitle());
        });
        welcomeMessageSubHearer.setText(Html.fromHtml(presenter.getSubHeaderFromFirebase()));

    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showOnBoardingTooltip(String title, String content) {
        BottomSheetView mToolTip = new BottomSheetView(getActivity());
        mToolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(content)
                .build());

        mToolTip.show();
    }

    @Override
    protected void initInjector() {
        getComponent(ReferralComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    public void clickOnCopyButton() {
        presenter.copyVoucherCode(referralCodeTextView.getText().toString());
        if (!TextUtils.isEmpty(referralCodeTextView.getText().toString())) {
            btnCopyReferralCode.setText(R.string.copied);
            btnCopyReferralCode.setTextColor(getResources().getColor(R.color.green_250));
            imgTick.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}