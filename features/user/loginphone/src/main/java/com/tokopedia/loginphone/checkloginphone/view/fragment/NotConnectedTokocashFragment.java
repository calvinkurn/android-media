package com.tokopedia.loginphone.checkloginphone.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.checkloginphone.di.DaggerCheckLoginPhoneComponent;
import com.tokopedia.loginphone.checkloginphone.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.common.data.LoginRegisterPhoneUrl;
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;

import javax.inject.Inject;

/**
 * @author by nisie on 12/4/17.
 */

public class NotConnectedTokocashFragment extends BaseDaggerFragment {

    ImageView image;
    TextView title;
    TextView message;
    TextView actionButton;
    TextView loginOtherButton;

    String phoneNumber;
    int type;

    @Inject
    LoginPhoneNumberAnalytics analytics;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new NotConnectedTokocashFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_NOT_CONNECTED_TO_TOKOCASH;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {

            BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication())
                    .getBaseAppComponent();

            LoginRegisterPhoneComponent loginRegisterPhoneComponent =
                    DaggerLoginRegisterPhoneComponent.builder()
                            .baseAppComponent(appComponent).build();

            DaggerCheckLoginPhoneComponent daggerCheckPhoneComponent = (DaggerCheckLoginPhoneComponent)
                    DaggerCheckLoginPhoneComponent.builder()
                            .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
                            .build();

            daggerCheckPhoneComponent.inject(this);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !TextUtils.isEmpty(savedInstanceState.getString
                (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER, ""))) {
            phoneNumber = savedInstanceState.getString
                    (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER);
        } else if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString
                (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER, ""))) {
            phoneNumber = getArguments().getString
                    (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER);
        } else if (getActivity() != null) {
            getActivity().finish();
        }

        if (savedInstanceState != null && savedInstanceState.getInt(
                NotConnectedTokocashActivity.PARAM_TYPE, -1) != -1) {
            type = savedInstanceState.getInt(
                    (NotConnectedTokocashActivity.PARAM_TYPE));
        } else if (getArguments() != null && getArguments().getInt(
                NotConnectedTokocashActivity.PARAM_TYPE, -1) != -1) {
            type = getArguments().getInt
                    (NotConnectedTokocashActivity.PARAM_TYPE);
        } else if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_connected_tokocash, parent, false);
        actionButton = view.findViewById(R.id.action_button);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        image = view.findViewById(R.id.icon);
        loginOtherButton = view.findViewById(R.id.login_other_button);
        prepareView();
        return view;
    }

    private void prepareView() {
        if (type == NotConnectedTokocashActivity.TYPE_PHONE_NOT_CONNECTED) {
            setViewPhoneNotconnected();
        } else {
            setViewNoTokocashAccount();
        }
    }

    private void setViewNoTokocashAccount() {
        ImageHandler.LoadImage(image, LoginRegisterPhoneUrl.URL_IMAGE_TOKOCASH_NO_ACCOUNT);
        title.setText(R.string.no_tokocash_account);
        message.setText(R.string.no_tokocash_account_message);
        actionButton.setText(R.string.login_with_other_method_2);

        actionButton.setOnClickListener(v -> goToLoginPage());

        loginOtherButton.setVisibility(View.GONE);
    }

    private void setViewPhoneNotconnected() {
        ImageHandler.LoadImage(image, LoginRegisterPhoneUrl.URL_IMAGE_TOKOCASH_PHONE_NOT_CONNECTED);
        title.setText(R.string.phone_number_not_connected_to_tokocash);
        message.setText(R.string.not_connected_tokocash_message);
        actionButton.setText(R.string.verify_phone_number);

        actionButton.setOnClickListener(v -> goToVerifyPhoneNumberPage());

        loginOtherButton.setVisibility(View.VISIBLE);
        loginOtherButton.setOnClickListener(v -> goToLoginPage());
    }

    private void goToLoginPage() {
        if (getActivity() != null) {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    private void goToVerifyPhoneNumberPage() {
//        startActivity(TokoCashOtpActivity.getCallingIntent(
//                getActivity(),
//                phoneNumber,
//                RequestOtpUseCase.OTP_TYPE_TOKOCASH,
//                true,
//                RequestOtpUseCase.MODE_SMS
//        ));

        //Flow no longer exist
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NotConnectedTokocashActivity.PARAM_PHONE_NUMBER, phoneNumber);
        outState.putInt(NotConnectedTokocashActivity.PARAM_TYPE, type);
    }
}
