package com.tokopedia.core.msisdn.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.facade.FacadePhoneVerification;
import com.tokopedia.core.fragment.VerificationDialog;
import com.tokopedia.core.interfaces.PhoneVerificationInterfaces;
import com.tokopedia.core.msisdn.presenter.MsisdnView;
import com.tokopedia.core.service.CheckVerification;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 23/11/2015.
 */
public class PhoneManualVerificationDialog extends DialogFragment{
    public static final String FRAGMENT_TAG = "PhoneManualVerificationDialog";
    public static final String VERIFICATION_NUMBER = "VERIFICATION_NUMBER";

    @Bind(R2.id.normal_verification_procedure)
    LinearLayout VerificationFromSettingsLayout;
    @Bind(R2.id.phone_number_manual_form)
    TextView PhoneNumberManualVerify;
    @Bind(R2.id.loading_manual_form)
    ProgressBar circleManualForm;
    @Bind(R2.id.verify_from_android)
    LinearLayout VerifyFromAndroid;
    @Bind(R2.id.new_number)
    EditText newPhoneNumber;
    @Bind(R2.id.phone_otp_layout)
    LinearLayout phoneOtpLayout;
    @Bind(R2.id.confirmation_number)
    EditText confirmationCode;
    @Bind(R2.id.verify_sms_text)
    TextView sendSmsVerificationLink;
    @Bind(R2.id.sending_sms_info)
    LinearLayout SendingSMSNotificationLayout ;
    @Bind(R2.id.manual_abort_button)
    TextView abortManualButton;
    @Bind(R2.id.code_confirm_button_manual)
    TextView manualVerifyCodeButton;
    @Bind(R2.id.close_button) View closeButton;

    FacadePhoneVerification facadePhoneVerification;

    public static Fragment newInstance(int type, String phoneNumber){
        Bundle bundle = new Bundle();
        bundle.putInt("auto_verify", type);
        bundle.putString("current_phone", phoneNumber);
        PhoneManualVerificationDialog phoneManualVerificationDialog = new PhoneManualVerificationDialog();
        phoneManualVerificationDialog.setArguments(bundle);
        return phoneManualVerificationDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        int verificationType = getArguments().getInt("auto_verify");
        if(verificationType== VerificationDialog.VerificationFromProfileSettings){
            Log.d("MNORMANSYAH", "show manual pop verification !!!");
        }
        String phoneNumber = getArguments().getString("current_phone");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.phone_manual_verification_form_layout, container, false);
        ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        VerificationFromSettingsLayout.setVisibility(View.GONE);
        VerifyFromAndroid.setVisibility(View.GONE);
        phoneOtpLayout.setVisibility(View.GONE);
        SendingSMSNotificationLayout.setVisibility(View.GONE);
        abortManualButton.setVisibility(View.GONE);
        manualVerifyCodeButton.setVisibility(View.GONE);
        closeButton.setVisibility(View.VISIBLE);
//        manualVerifyCodeButton.setClickable(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R2.id.manual_abort_button)
    public void onDismiss(){
        dismiss();
    }

    @OnClick(R2.id.close_button)
    public void onDismissDialog(){
        dismiss();
    }

    @OnClick(R2.id.code_confirm_button_manual)
    public void DoVerification(){
        String inputConfirmationCode = confirmationCode.getText().toString();
        if(PhoneNumberValidation(ChooseNumber()) && VerificationCodeValidation(inputConfirmationCode)){
//            mProgressDialog.showDialog();// SHOW LOADING
            facadePhoneVerification.sendVerification(inputConfirmationCode, ChooseNumber(), doVerificationListener());
        }else{
            dismiss();
        }
    }

    private boolean VerificationCodeValidation(String verificationCode){
        boolean Validation = true;
        confirmationCode.setError(null);
        if(verificationCode.length() == 0){
            confirmationCode.setError(getString(R.string.error_field_required));
            confirmationCode.requestFocus();
            Validation = false;
        }
        return Validation;
    }

    private PhoneVerificationInterfaces.sendVerificationListener doVerificationListener(){
        return new PhoneVerificationInterfaces.sendVerificationListener() {
            @Override
            public void onVerificationSuccess() {
                if(getActivity()!= null && getActivity() instanceof MsisdnView){
                    ((MsisdnView)getActivity()).moveToThankYou(-99);
                }
                LocalCacheHandler.clearCache(getActivity(), VERIFICATION_NUMBER);
                LocalCacheHandler cache = new LocalCacheHandler(getActivity(), CheckVerification.VERIFICATION_STATUS);
                cache.putBoolean(CheckVerification.VERIFIED, true);
                cache.putBoolean(CheckVerification.ALLOW_SHOP, true);
                cache.applyEditor();

            }

            @Override
            public void onProcessDone() {
                // dismiss dialog
//                mProgressDialog.dismiss();
            }
        };
    }

    @OnClick(R2.id.verify_sms_text)
    public void requestVerificationSMSListener(View v) {
        if(PhoneNumberValidation(ChooseNumber())){
            facadePhoneVerification.requestVerificationCode(ChooseNumber(), requestListener());
        }
    }

    private PhoneVerificationInterfaces.requestCodeListener requestListener(){
        return new PhoneVerificationInterfaces.requestCodeListener() {
            @Override
            public void onProcessDone() {
                SendingSMSNotificationLayout.setVisibility(View.GONE);
                sendSmsVerificationLink.setClickable(true);
            }
        };
    }

    private boolean PhoneNumberValidation(String phoneNumber){
        boolean Validation = true;
        newPhoneNumber.setError(null);
        if(phoneNumber == null) {
            Toast.makeText(getActivity(), R.string.error_connection, Toast.LENGTH_SHORT).show();
            Validation = false;
        }else if(phoneNumber.length() == 0){
            newPhoneNumber.setError(getString(R.string.error_field_required));
            newPhoneNumber.requestFocus();
            Validation = false;
        }else if(!phoneNumber.startsWith("0")){
            newPhoneNumber.setError(getString(R.string.error_phone_wrong_format));
            newPhoneNumber.requestFocus();
            Validation = false;
        }else if(phoneNumber.length()>16){
            newPhoneNumber.setError(getString(R.string.error_phone_too_long));
            newPhoneNumber.requestFocus();
            Validation = false;
        }else if(phoneNumber.length()<6){
            newPhoneNumber.setError(getString(R.string.error_phone_too_short));
            newPhoneNumber.requestFocus();
            Validation = false;
        }
        return Validation;
    }

    private String ChooseNumber(){
        String SelectedNumber = null;
        if(VerifyFromAndroid.isShown()){
            SelectedNumber = newPhoneNumber.getText().toString();
        }
        return SelectedNumber;
    }
}
