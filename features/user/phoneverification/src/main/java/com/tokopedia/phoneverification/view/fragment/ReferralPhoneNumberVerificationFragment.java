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
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.view.activity.ChangePhoneNumberActivity;

/**
 * Created by ashwanityagi on 12/12/17.
 */

public class ReferralPhoneNumberVerificationFragment extends TkpdBaseV4Fragment {

    public interface ReferralPhoneNumberVerificationFragmentListener {
        void onSkipVerification();

        void onClickVerification(String phoneNumber);
    }


    EditText tvPhoneNumber;
    TextView btnActivation;
    private ReferralPhoneNumberVerificationFragmentListener listener;
    private SessionHandler sessionHandler;

    public static ReferralPhoneNumberVerificationFragment newInstance() {
        ReferralPhoneNumberVerificationFragment fragment = new ReferralPhoneNumberVerificationFragment();
        return fragment;
    }

    public static ReferralPhoneNumberVerificationFragment createInstance(ReferralPhoneNumberVerificationFragmentListener listener) {
        ReferralPhoneNumberVerificationFragment fragment = new ReferralPhoneNumberVerificationFragment();
        fragment.setReferralPhoneVerificationListener(listener);
        return fragment;
    }

    public void setReferralPhoneVerificationListener(ReferralPhoneNumberVerificationFragmentListener listener) {
        this.listener = listener;
    }

    public ReferralPhoneNumberVerificationFragmentListener getListener() {
        return listener;
    }


    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_referral_phone_number_verification, container, false);
        initView(view);
        return view;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (sessionHandler == null) {
            sessionHandler = new SessionHandler(getActivity());
        }
        tvPhoneNumber.setText(CustomPhoneNumberUtil.transform(
                sessionHandler.getPhoneNumber()));
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_referral_phone_number_verification;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initView(View view) {
        if (sessionHandler == null) {
            sessionHandler = new SessionHandler(getActivity());
        }
        tvPhoneNumber = (EditText) view.findViewById(R.id.tv_phone_number);
        tvPhoneNumber.setText(CustomPhoneNumberUtil.transform(
                sessionHandler.getPhoneNumber()));
        btnActivation = (TextView) view.findViewById(R.id.btn_activation);
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

    @Override
    protected void setViewListener() {
        btnActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_VERIFY_NUMBER, tvPhoneNumber.getText().toString().replace("-", ""));

                    listener.onClickVerification(tvPhoneNumber.getText().toString());
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            tvPhoneNumber.setText(data.getStringExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}