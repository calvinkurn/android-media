package com.tokopedia.phoneverification.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.phoneverification.PhoneVerificationAnalytics;
import com.tokopedia.phoneverification.PhoneVerificationConst;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.di.DaggerPhoneVerificationComponent;
import com.tokopedia.phoneverification.di.PhoneVerificationComponent;
import com.tokopedia.phoneverification.util.CustomPhoneNumberUtil;
import com.tokopedia.phoneverification.view.activity.ChangePhoneNumberActivity;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;
import com.tokopedia.phoneverification.view.presenter.VerifyPhoneNumberPresenter;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationFragment extends BaseDaggerFragment
        implements PhoneVerification.View{

    @Override
    protected String getScreenName() {
        return PhoneVerificationConst.SCREEN_PHONE_VERIFICATION;
    }

    @Override
    public void initInjector() {
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

    public interface PhoneVerificationFragmentListener {
        void onSkipVerification();

        void onSuccessVerification();
    }

    private static final String EXTRA_PARAM_PHONE_NUMBER = "EXTRA_PARAM_PHONE_NUMBER";
    private static final int RESULT_PHONE_VERIFICATION = 11;

    protected TextView skipButton;
    protected TextView phoneNumberEditText;
    protected TextView changePhoneNumberButton;
    protected TextView requestOtpButton;

    protected View progressDialog;
    PhoneVerificationFragmentListener listener;
    private String phoneNumber;

    PhoneVerificationAnalytics analytics;

    @Inject
    public UserSession userSession;
    @Inject
    public VerifyPhoneNumberPresenter presenter;

    private boolean isMandatory = false;

    public static PhoneVerificationFragment createInstance(PhoneVerificationFragmentListener listener) {
        PhoneVerificationFragment fragment = new PhoneVerificationFragment();
        fragment.setPhoneVerificationListener(listener);
        return fragment;
    }


    public static PhoneVerificationFragment createInstance(PhoneVerificationFragmentListener listener, boolean canSkip) {
        PhoneVerificationFragment fragment = new PhoneVerificationFragment();
        Bundle args = new Bundle();
        args.putBoolean(PhoneVerificationActivationActivity.EXTRA_IS_MANDATORY, canSkip);
        fragment.setArguments(args);
        fragment.setPhoneVerificationListener(listener);
        return fragment;
    }

    public static PhoneVerificationFragment createInstance(PhoneVerificationFragmentListener listener, String phoneNumber) {
        PhoneVerificationFragment fragment = new PhoneVerificationFragment();
        fragment.setPhoneVerificationListener(listener);
        Bundle args = new Bundle();
        args.putString(EXTRA_PARAM_PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = PhoneVerificationAnalytics.createInstance();
        if (getArguments() != null) {
            if (getArguments().containsKey(PhoneVerificationActivationActivity.EXTRA_IS_MANDATORY)) {
                isMandatory = getArguments().getBoolean(PhoneVerificationActivationActivity.EXTRA_IS_MANDATORY);
            }
            this.phoneNumber = getArguments().getString(EXTRA_PARAM_PHONE_NUMBER);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            this.phoneNumber = savedInstanceState.getString(EXTRA_PARAM_PHONE_NUMBER);
        }
    }

    public void setPhoneVerificationListener(PhoneVerificationFragmentListener listener) {
        this.listener = listener;
    }

    public PhoneVerificationFragmentListener getListener() {
        return listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_verification, container, false);
        findView(view);
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.attachView(this);
        setViewListener();
        return view;
    }

    public void findView(View view) {
        skipButton = (TextView) view.findViewById(R.id.skip_button);
        phoneNumberEditText = (TextView) view.findViewById(R.id.phone_number);
        changePhoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);
        requestOtpButton = (TextView) view.findViewById(R.id.send_otp);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(
                userSession.getPhoneNumber()));

        if (phoneNumber != null && "".equalsIgnoreCase(phoneNumber.trim())) {
            phoneNumberEditText.setText(phoneNumber);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PARAM_PHONE_NUMBER, phoneNumber);
    }

    public void setRequestOtpButtonListener(){
        requestOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    Intent intent = VerificationActivity.getCallingIntent(
                            getActivity(),
                            getPhoneNumber(),
                            RequestOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION,
                            true,
                            RequestOtpUseCase.MODE_SMS
                    );
                    startActivityForResult(intent, RESULT_PHONE_VERIFICATION);
                }
            }
        });
    }


    public void setViewListener() {

        setRequestOtpButtonListener();

        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() instanceof PhoneVerificationActivationActivity) {
                    analytics.eventClickChangePhoneRegister();
                }

                startActivityForResult(
                        ChangePhoneNumberActivity.getChangePhoneNumberIntent(
                                getActivity(),
                                phoneNumberEditText.getText().toString()),
                        ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() instanceof PhoneVerificationActivationActivity) {
                    analytics.eventClickSkipRegister();
                }

                if (listener != null)
                    listener.onSkipVerification();
                else {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            }
        });

        if (isMandatory) {
            skipButton.setVisibility(View.INVISIBLE);
        } else {
            skipButton.setVisibility(View.VISIBLE);
        }
    }

    public String getPhoneNumber() {
        return phoneNumberEditText.getText().toString().replace("-", "");
    }

    @Override
    public void onSuccessVerifyPhoneNumber() {
        userSession.setIsMSISDNVerified(true);
        if (listener != null)
            listener.onSuccessVerification();
        else {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }

        ToasterNormal.show(getActivity(), getString(R.string.success_verify_phone_number));
    }

    private void showErrorPhoneNumber(String errorMessage) {
        phoneNumberEditText.setError(errorMessage);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            phoneNumberEditText.setText(data.getStringExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER));
        } else if (requestCode == RESULT_PHONE_VERIFICATION &&
                resultCode == Activity.RESULT_OK){
            presenter.verifyPhoneNumber(getPhoneNumber());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onErrorVerifyPhoneNumber(String errorMessage) {
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public UserSession getUserSession() {
        return this.userSession;
    }

    public boolean isValid() {
        boolean isValid = true;
        if (getPhoneNumber().length() == 0) {
            showErrorPhoneNumber(getString(R.string
                    .error_field_required));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
