package com.tokopedia.tkpd.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.tkpd.network.apiservices.user.PeopleActService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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
    private int timeoutCounter;
    private Handler timeOutHandler;
    private TkpdProgressDialog mTkpdProgressDialog;
    private LocalCacheHandler handler;
    private static int EXPIRE_TIME = 30;
    public static String FRAGMENT_TAG = "dialog_email";
    PeopleActService peopleActService;
    CompositeSubscription compositeSubscription;

    public interface EmailChangeConfirmation {
        public void onEmailChanged();
    }

    public static Fragment newInstance(String email) {
        EmailVerificationDialog dialog = new EmailVerificationDialog();
        dialog.UserEmail = email;
        return dialog;
    }

    interface EditEmailListener {
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(JSONObject result);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        context = getActivity();
        emailChangeComm = (EmailChangeConfirmation) context;
        timeOutHandler = new Handler();
        handler = new LocalCacheHandler(getActivity(), "SEND_OTP_EMAIL");
        super.onAttach(activity);
    }

    @Override
    public void onStop() {
        timeOutHandler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        peopleActService = new PeopleActService();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        timeoutCounter = 0;

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
        timeOutHandler.post(runnable);
//		NetworkHandler network = new NetworkHandler(context, TkpdUrl.GET_PEOPLE);
//		String newEmail = EmailInput.getText().toString();
//		network.AddParam("act", "edit_email");
//		network.AddParam("new_email", newEmail);
//		network.AddParam("password", UserPassword.getText().toString());
//        network.AddParam("otp_code", inputOtpCodeField.getText().toString());
//
//		network.Commit(new NetworkHandlerListener() {
//
//			@Override
//			public void onSuccess(Boolean status) {
//				timeOutHandler.removeCallbacks(runnable);
//				mTkpdProgressDialog.dismiss();
//				emailChangeComm.onEmailChanged();
//			}
//
//			@Override
//			public void getResponse(JSONObject Result) {
//				try {
//					int Status = Result.getInt("is_success");
//					if (Status == 1) {
//						ChangeEmailLayout.setVisibility(View.GONE);
//						CheckEmailInstruction.setVisibility(View.VISIBLE);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void getMessageError(ArrayList<String> MessageError) {
//				// TODO Auto-generated method stub
//				printErrorMessage(MessageError);
//			}
//		});
        editEmail(listener());
    }

    private void editEmail(final EditEmailListener listener) {
        Observable<Response<TkpdResponse>> observable = peopleActService.getApi()
                .editEmail(AuthUtil.generateParams(context, getParam()));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("ASDASDM", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        listener.onSuccess(result);
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {

                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public EditEmailListener listener() {
        return new EditEmailListener() {
            @Override
            public void onError(String error) {
                mTkpdProgressDialog.dismiss();
                SnackbarManager.make(context, error,
                        Snackbar.LENGTH_LONG).show();
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
            public void onTimeout() {
                mTkpdProgressDialog.dismiss();
                SnackbarManager.make(context, getResources().getString(R.string.msg_connection_timeout),
                        Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(JSONObject result) {
                int status = result.optInt("is_success");
                if (status == 1) {
                    ChangeEmailLayout.setVisibility(View.GONE);
                    CheckEmailInstruction.setVisibility(View.VISIBLE);
                }
                timeOutHandler.removeCallbacks(runnable);
                mTkpdProgressDialog.dismiss();
                emailChangeComm.onEmailChanged();
            }
        };
    }

    private void RequestOTPCode() {
        timeOutHandler.post(runnable);
        NetworkHandler network = new NetworkHandler(context, TkpdUrl.GET_PEOPLE);
        network.AddParam("act", "send_otp_edit_email");
        network.Commit(new NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
                mTkpdProgressDialog.dismiss();
                timeoutCounter = 0;
                timeOutHandler.removeCallbacks(runnable);
                handler.setExpire(EXPIRE_TIME);
            }

            @Override
            public void getResponse(JSONObject Result) {
                try {
                    int Status = Result.getInt("is_success");
                    if (Status == 1) {

                        Toast.makeText(context, "Kode OTP telah dikirim ke nomor Handphone teregistrasi", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Kode OTP gagal dikirim", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                mTkpdProgressDialog.dismiss();
                String messageError = "";
                for (int i = 0; i < MessageError.size(); i++) {
                    messageError += MessageError.get(i);
                    if ((i + 1) < MessageError.size())
                        messageError += "\n";
                }
                NetworkErrorHelper.showSnackbar(context, messageError);
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeoutCounter++;
            if (timeoutCounter == 5) {
                Toast.makeText(context, "Sedang Terjadi Gangguan Koneksi, Mohon Tunggu", Toast.LENGTH_LONG).show();
            } else if (timeoutCounter == 20) {
                Toast.makeText(context, "Terjadi Masalah Koneksi, Silahkan coba di lain kesempatan", Toast.LENGTH_LONG).show();
                mTkpdProgressDialog.dismiss();
                dismiss();
            }
            timeOutHandler.postDelayed(this, 3000);
        }
    };
}
