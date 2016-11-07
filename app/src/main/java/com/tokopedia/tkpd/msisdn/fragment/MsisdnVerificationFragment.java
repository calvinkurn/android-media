package com.tokopedia.tkpd.msisdn.fragment;

import android.app.DialogFragment;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.msisdn.IncomingSms;
import com.tokopedia.tkpd.msisdn.MSISDNConstant;
import com.tokopedia.tkpd.msisdn.listener.MsisdnVerificationFragmentView;
import com.tokopedia.tkpd.msisdn.presenter.MsisdnVerificationFragmentPresenter;
import com.tokopedia.tkpd.msisdn.presenter.MsisdnVerificationFragmentPresenterImpl;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.util.PhoneVerificationUtil;
import com.tokopedia.tkpd.util.SessionHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 7/13/16.
 */
public class MsisdnVerificationFragment extends DialogFragment
        implements MsisdnVerificationFragmentView, IncomingSms.ReceiveSMSListener, MSISDNConstant {

    @Bind(R2.id.view_verification)
    View verificationView;

    @Bind(R2.id.view_phone_number)
    View phoneNumberView;

    @Bind(R2.id.send_otp)
    Button sendOtp;

    @Bind(R2.id.verify_otp)
    Button verifyOtp;

    @Bind(R2.id.close_button)
    Button closeButton;

    @Bind(R2.id.input_phone)
    EditText phoneNumberEditText;

    @Bind(R2.id.input_otp)
    EditText otpEditText;

    @Bind(R2.id.logo)
    ImageView logo;

    @Bind(R2.id.btn_no_thanks)
    TextView noThanksButton;

    @Bind(R2.id.username)
    TextView username;

    @Bind(R2.id.verification_instruction)
    TextView instruction;

    MsisdnVerificationFragmentPresenter presenter;
    TkpdProgressDialog progressDialog;
    private PhoneVerificationUtil.MSISDNListener listener;
    LocalCacheHandler cacheHandler;

    public static MsisdnVerificationFragment createInstance() {
        MsisdnVerificationFragment fragment = new MsisdnVerificationFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("VerificationView", verificationView.getVisibility());
        outState.putBoolean("phoneNumberEnabled", phoneNumberEditText.isEnabled());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            phoneNumberEditText.setEnabled(savedInstanceState.getBoolean("phoneNumberEnabled"));
            setVisibilityVerificationView(savedInstanceState.getInt("VerificationView"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
        setViewListener();
        initialVar();
        setActionVar();

        setCache();

    }

    private void setCache() {
        if(!cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_REQUEST_OTP, false)){
            setFinishRequestOTP();
        }
    }

    private void setVisibilityVerificationView(int view) {
        verificationView.setVisibility(view);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void initialPresenter() {
        presenter = new MsisdnVerificationFragmentPresenterImpl(this);
    }

    private int getFragmentLayout() {
        return R.layout.fragment_msisdn_verification;
    }

    private void initView(View view) {
        ImageHandler.loadImageWithId(logo, R.drawable.ic_verifikasi);
        phoneNumberEditText.setText(SessionHandler.getPhoneNumber());
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        username.setText(getActivity().getString(R.string.hello) + ", " + SessionHandler.getLoginName(getActivity()));


    }

    private void setViewListener() {
        noThanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setReminder(true);

            }
        });
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(),sendOtp);
                presenter.requestOTP(phoneNumberEditText.getText().toString().trim());

            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(),verifyOtp);
                presenter.verifyOTP(otpEditText.getText().toString().trim());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDismiss();
            }
        });
    }

    private void initialVar() {
        cacheHandler = new LocalCacheHandler(getActivity(), PHONE_VERIFICATION);
    }

    private void setActionVar() {

    }

    public void showError(String errorMessage) {
        if (errorMessage.equals("")) {
            CommonUtils.UniversalToast(getActivity(),getActivity().getString(R.string.msg_network_error));
        } else {
            CommonUtils.UniversalToast(getActivity(),errorMessage);
        }
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumberEditText.getText().toString();
    }

    @Override
    public String getVerificationCode() {
        return otpEditText.getText().toString();
    }

    @Override
    public void setError(EditText editText, String error) {
        editText.setError(error);
        editText.requestFocus();
    }

    @Override
    public void removeError() {
        otpEditText.setError(null);
        phoneNumberEditText.setError(null);
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void finishLoading() {
        if (progressDialog.isProgress())
            progressDialog.dismiss();
    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    @Override
    public EditText getOtpEditText() {
        return otpEditText;
    }

    @Override
    public EditText getPhoneEditText() {
        return phoneNumberEditText;
    }

    @Override
    public void onSuccessRequestOTP() {
        CommonUtils.UniversalToast(getActivity(),getActivity().getString(R.string.success_send_otp));
        finishLoading();
        setFinishRequestOTP();
        setSuccessRequestOTPCache();
    }

    private void setFinishRequestOTP() {
        phoneNumberEditText.setEnabled(false);
        verificationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessVerifyOTP() {
        finishLoading();
        phoneNumberView.setVisibility(View.GONE);
        verificationView.setVisibility(View.GONE);
        noThanksButton.setVisibility(View.GONE);
        instruction.setText(getActivity().getString(R.string.verify_success));
        closeButton.setVisibility(View.VISIBLE);
        if(listener!=null)
            listener.onMSISDNVerified();
    }

    @Override
    public void setSuccessRequestOTPCache() {
        cacheHandler.putBoolean(HAS_REQUEST_OTP, true);
        cacheHandler.setExpire(3600);
        cacheHandler.applyEditor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.onDestroyView();
    }

    @Override
    public void onReceiveSMS(String message) {
        if (message.contains("Verifikasi Nomor Handphone")) {
            CommonUtils.dumper("NISNISSMS " + message);
            String regexString = Pattern.quote("kode") + "(.*?)" + Pattern.quote("sebelum");
            Pattern pattern = Pattern.compile(regexString);
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String otpCode = matcher.group(1).trim();
                if (otpEditText != null)
                    otpEditText.setText(otpCode);
            }
        }
    }

    public void setListener(PhoneVerificationUtil.MSISDNListener listener) {
        this.listener = listener;
    }

    public PhoneVerificationUtil.MSISDNListener getListener() {
        return listener;
    }


}
