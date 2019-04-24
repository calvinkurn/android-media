package com.tokopedia.phoneverification.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.phoneverification.PhoneVerificationAnalytics;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.di.DaggerPhoneVerificationComponent;
import com.tokopedia.phoneverification.di.PhoneVerificationComponent;
import com.tokopedia.phoneverification.util.CustomPhoneNumberUtil;
import com.tokopedia.phoneverification.view.activity.ChangePhoneNumberActivity;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import static com.tokopedia.phoneverification.PhoneVerificationConst.URL_TOKOCASH_SHARE;

/**
 * Created by ashwanityagi on 12/12/17.
 */

public class ReferralPhoneNumberVerificationFragment extends BaseDaggerFragment {

    public interface ReferralPhoneNumberVerificationFragmentListener {
        void onSkipVerification();

        void onClickVerification(String phoneNumber);
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null && getActivity().getApplication() != null) {
            BaseAppComponent baseAppComponent =
                    ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();

            PhoneVerificationComponent phoneVerificationComponent =
                    DaggerPhoneVerificationComponent.
                            builder().
                            baseAppComponent(baseAppComponent).
                            build();


            phoneVerificationComponent.inject(this);
        }
    }

    EditText tvPhoneNumber;
    TextView btnActivation;
    ImageView ivTokocash;
    PhoneVerificationAnalytics analytics;
    private ReferralPhoneNumberVerificationFragmentListener listener;
    @Inject
    UserSession userSession;

    public static ReferralPhoneNumberVerificationFragment newInstance() {
        ReferralPhoneNumberVerificationFragment fragment = new
                ReferralPhoneNumberVerificationFragment();
        return fragment;
    }

    public static ReferralPhoneNumberVerificationFragment createInstance
            (ReferralPhoneNumberVerificationFragmentListener listener) {
        ReferralPhoneNumberVerificationFragment fragment =
                new ReferralPhoneNumberVerificationFragment();
        fragment.setReferralPhoneVerificationListener(listener);
        return fragment;
    }

    public void setReferralPhoneVerificationListener
            (ReferralPhoneNumberVerificationFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = PhoneVerificationAnalytics.createInstance();
    }

    public ReferralPhoneNumberVerificationFragmentListener getListener() {
        return listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_referral_phone_number_verification,
                container, false);
        initView(view);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void initView(View view) {
        tvPhoneNumber = (EditText) view.findViewById(R.id.tv_phone_number);
        tvPhoneNumber.setText(CustomPhoneNumberUtil.transform(
                userSession.getPhoneNumber()));
        btnActivation = (TextView) view.findViewById(R.id.btn_activation);
        ivTokocash = (ImageView) view.findViewById(R.id.img_app_share);
        ImageHandler.loadImage2(ivTokocash, URL_TOKOCASH_SHARE, R.drawable.loading_page);
        setViewListener();
        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        ChangePhoneNumberActivity.getChangePhoneNumberIntent(
                                getActivity(),
                                tvPhoneNumber.getText().toString()),
                        ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER);
            }
        });
    }

    private void setViewListener() {
        btnActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    analytics.eventReferralAndShare(
                            PhoneVerificationAnalytics.Action.CLICK_VERIFY_NUMBER,
                            tvPhoneNumber.getText().toString().replace("-", ""));
                    listener.onClickVerification(tvPhoneNumber.getText().toString());
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            tvPhoneNumber.setText(data.getStringExtra(ChangePhoneNumberFragment
                    .EXTRA_PHONE_NUMBER));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}