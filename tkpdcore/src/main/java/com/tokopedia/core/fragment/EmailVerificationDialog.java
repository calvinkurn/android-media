package com.tokopedia.core.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core2.R;
import com.tokopedia.core.manage.people.profile.interactor.ManagePeopleProfileInteractor;
import com.tokopedia.core.manage.people.profile.interactor.ManagePeopleProfileInteractorImpl;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.HashMap;
import java.util.Map;

public class EmailVerificationDialog extends DialogFragment {

    private Activity context;
    private EmailChangeConfirmation emailChangeComm;
    private TextView CurrentEmail;
    private EditText EmailInput;
    private EditText UserPassword;
    private EditText inputOtpCodeField;
    private TextView confirmButton;
    private TextView cancelButton;
    private TextView closeButton;
    private TextView requestOTPButton;
    private String UserEmail;
    private LinearLayout CheckEmailInstruction, ChangeEmailLayout;
    private TkpdProgressDialog mTkpdProgressDialog;
    private LocalCacheHandler handler;
    private static int EXPIRE_TIME = 30;
    public static String FRAGMENT_TAG = "dialog_email";
    ManagePeopleProfileInteractor networkInteractor;

    public interface EmailChangeConfirmation {
        public void onEmailChanged();
    }

    public static Fragment newInstance(String email) {
        EmailVerificationDialog dialog = new EmailVerificationDialog();
        dialog.UserEmail = email;
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        context = getActivity();
        emailChangeComm = (EmailChangeConfirmation) context;
        handler = new LocalCacheHandler(getActivity(), "SEND_OTP_EMAIL");
        super.onAttach(activity);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        networkInteractor = ManagePeopleProfileInteractorImpl.createInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.dialog_email_verification, container, false);

        mTkpdProgressDialog = new TkpdProgressDialog(context,
                TkpdProgressDialog.NORMAL_PROGRESS);

        CurrentEmail = (TextView) view.findViewById(R.id.current_email);
        EmailInput = (EditText) view.findViewById(R.id.email_input);
        UserPassword = (EditText) view.findViewById(R.id.password);
        inputOtpCodeField = (EditText) view.findViewById(R.id.input_otp_code);
        confirmButton = (TextView) view.findViewById(R.id.code_confirm_button);
        ChangeEmailLayout = (LinearLayout) view.findViewById(R.id.change_email_layout);
        CheckEmailInstruction = (LinearLayout) view.findViewById(R.id.instruction_check_new_email);

        cancelButton = (TextView) view.findViewById(R.id.abort_button);
        closeButton = (TextView) view.findViewById(R.id.close_button);
        requestOTPButton = (TextView) view.findViewById(R.id.request_otp_code);

        confirmButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (EmailFormValidation()) {

                    mTkpdProgressDialog.showDialog();
                    SendEmailVerificationCode();
                }
            }
        });
        cancelButton.setOnClickListener(CloseListener);
        closeButton.setOnClickListener(CloseListener);
        CurrentEmail.setText(UserEmail);
        requestOTPButton.setOnClickListener(requestOTPCodeListener());

        return view;
    }

    private Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
        param.put("new_email", EmailInput.getText().toString());
        param.put("password", UserPassword.getText().toString());
        param.put("otp_code", inputOtpCodeField.getText().toString());
        return param;
    }


    private void SendEmailVerificationCode() {
        networkInteractor.editEmail(getActivity(), getParam(), new ManagePeopleProfileInteractor.EditEmailListener() {
            @Override
            public void onSuccess() {
                ChangeEmailLayout.setVisibility(View.GONE);
                CheckEmailInstruction.setVisibility(View.VISIBLE);
                mTkpdProgressDialog.dismiss();
                emailChangeComm.onEmailChanged();
            }

            @Override
            public void onTimeout() {
                mTkpdProgressDialog.dismiss();
                SnackbarManager.make(context, getResources().getString(R.string.msg_connection_timeout),
                        Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailAuth() {
                mTkpdProgressDialog.dismiss();
                NetworkErrorHelper.showSnackbar(getActivity());
            }

            @Override
            public void onThrowable(Throwable e) {
                if (e.getMessage().contains("timeout")) {
                    onTimeout();
                } else {
                    mTkpdProgressDialog.dismiss();
                    SnackbarManager.make(context, getResources().getString(R.string.msg_no_connection),
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String error) {
                mTkpdProgressDialog.dismiss();
                SnackbarManager.make(context, error,
                        Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNullData() {
                mTkpdProgressDialog.dismiss();
                NetworkErrorHelper.showSnackbar(getActivity());
            }

            @Override
            public void onNoConnection() {
                mTkpdProgressDialog.dismiss();
                NetworkErrorHelper.showSnackbar(getActivity());
            }
        });
    }

    private void RequestOTPCode() {
        networkInteractor.sendOTPEditEmail(getActivity(), new HashMap<String, String>(), new ManagePeopleProfileInteractor.RequestOTPListener() {
            @Override
            public void onSuccess() {
                mTkpdProgressDialog.dismiss();
                Toast.makeText(context, "Kode OTP telah dikirim ke nomor Handphone teregistrasi", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onTimeout() {
                mTkpdProgressDialog.dismiss();
                NetworkErrorHelper.showSnackbar(context);
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                mTkpdProgressDialog.dismiss();
                NetworkErrorHelper.showSnackbar(context, error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                mTkpdProgressDialog.dismiss();
                NetworkErrorHelper.showSnackbar(context);
            }
        });

    }

    public boolean EmailFormValidation() {
        boolean Validation = true;
        if (EmailInput.getText().toString().length() == 0) {
            EmailInput.setError(getString(R.string.error_field_required));
            EmailInput.requestFocus();
            Validation = false;
        } else if (UserPassword.getText().toString().length() == 0) {
            UserPassword.setError(getString(R.string.error_field_required));
            Validation = false;
        } else if (inputOtpCodeField.getText().toString().length() == 0) {
            inputOtpCodeField.setError(getString(R.string.error_field_required));
            Validation = false;
        }
        return Validation;
    }

    public OnClickListener CloseListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            dismiss();
        }
    };

    private OnClickListener requestOTPCodeListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTkpdProgressDialog.showDialog();
                if (handler.isExpired()) {
                    RequestOTPCode();
                } else {
                    Long diff = System.currentTimeMillis() / 1000 - handler.getLong("timestamp");
                    int interval = handler.getInt("expired_time");
                    int remainingTime = interval - diff.intValue();
                    CommonUtils.UniversalToast(getActivity(), "Silahkan coba " + remainingTime + " detik lagi");
                    mTkpdProgressDialog.dismiss();
                }
                inputOtpCodeField.setEnabled(true);
            }
        };
    }
}
