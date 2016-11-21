package com.tokopedia.core.session;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.var.TkpdState;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author m.normansyah
 * @version 2
 *          because it is too simple than maybe after this commit, it change MVP
 *          <p>
 *          migrate retrofit 2 by Angga.Prasetiyo
 * @since 16/11/2015.
 */
public class ActivationResentFragment extends Fragment implements BaseView {

    String format = "Petunjuk aktivasi akun Tokopedia telah kami kirimkan\n" +
            "ke email %s. Silahkan periksa email Anda.\n" +
            "\n" +
            "Jika email tidak ditemukan atau belum menerima email aktivasi silakan klik tombol di bawah ini:";

    @Bind(R2.id.head) TextView nameEditText;
    @Bind(R2.id.email)
    TextView email;
    @Bind(R2.id.resend_button)
    TextView resendButton;

    CompositeSubscription compositeSubscription;
    AccountsService accountsService;
    ResentActivationListener listener;

    String emailText = "example@test.com";

    public static final String EMAIL_KEY = "EMAIL_KEY";
    TkpdProgressDialog mProgressDialog;

    interface ResentActivationListener {
        void onSuccess(JSONObject result, List<String> statusMessages, List<String> errorMessages);

        void onError(String s);

        void onTimeout();

        void onThrowable(Throwable e);
    }

    public static Fragment newInstance(String email) {
        Bundle bundle = new Bundle();
        bundle.putString(EMAIL_KEY, email);
        Fragment fragment = new ActivationResentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void alertboxNavigateToParentIndexHome(String mymessage) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(mymessage)
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                Intent intent =
//                                        new Intent(ActivationResentFragment.this.getActivity(),
//                                                ParentIndexHome.class)
//                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                ActivationResentFragment.this.getActivity().finish();
                            }
                        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emailText = getArguments().getString(EMAIL_KEY);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.getString(EMAIL_KEY) != null) {
                emailText = savedInstanceState.getString(EMAIL_KEY);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        compositeSubscription = new CompositeSubscription();
        accountsService = new AccountsService(bundle);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.activity_activation_resent, container, false);
        ButterKnife.bind(this, parent);
        resendButton.setBackgroundResource(R.drawable.bg_rounded_corners);
        nameEditText.setText("Halo, ");
        SnackbarManager.make(getActivity(),"Akun anda belum diaktivasi. Cek email anda untuk mengaktivasi akun.",Snackbar.LENGTH_LONG).show();
        return parent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getActivity().getCurrentFocus();
        KeyboardHandler.DropKeyboard(getActivity(), view);
        email.setText(emailText);
        setListener();
        ScreenTracking.screen(this);
    }

    private void setListener() {
        listener = new ResentActivationListener() {
            @Override
            public void onSuccess(JSONObject result, List<String> statusMessages, List<String> errorMessages) {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();

                boolean isSuccess = "1".equals(result.optString("is_success"));
                if (isSuccess) {
                    String t1 = ActivationResentFragment.this.getActivity()
                            .getString(R.string.alert_account_pending_first_half);
                    String t3 = ActivationResentFragment.this.getActivity().getString(R.string
                            .alert_account_pending_second_half);
                    alertboxNavigateToParentIndexHome(
                            t1 + "" + email.getText() + " " + t3
                    );
                } else {
                    SnackbarManager.make(getActivity(), errorMessages.toString(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                SnackbarManager.make(getActivity(), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onTimeout() {
                onError(getResources().getString(R.string.msg_connection_timeout));
            }

            @Override
            public void onThrowable(Throwable e) {
                onError(getResources().getString(R.string.msg_connection_timeout));
            }
        };

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.email || id == EditorInfo.IME_NULL) {
                    resendActivation();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EMAIL_KEY, email.getText().toString());
    }

    @OnClick(R2.id.resend_button)
    public void resendActivation() {
        KeyboardHandler.DropKeyboard(getActivity(), email);
        mProgressDialog = new TkpdProgressDialog(getActivity(),
                TkpdProgressDialog.NORMAL_PROGRESS);
        mProgressDialog.showDialog();
        Map<String, String> params = new HashMap<>();
        params.put("email", email.getText().toString());
        resendButton.setText("Kirim Ulang");
        if (compositeSubscription.isUnsubscribed()) {
            this.compositeSubscription = new CompositeSubscription();
        }

        UnifyTracking.eventResendNotification();

        Observable<Response<TkpdResponse>> observable = accountsService.getApi()
                .resentActivation(MapNulRemover.removeNull(params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("ActivationResent", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        listener.onSuccess(result, tkpdResponse.getStatusMessages(), tkpdResponse.getErrorMessages());
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(getActivity().getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(getActivity().getString(R.string.default_request_error_timeout));
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(getActivity().getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(getActivity().getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(getActivity().getString(R.string.default_request_error_forbidden_auth));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        compositeSubscription.unsubscribe();
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.ACTIVATION_RESENT;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void onMessageError(int type, Object... data) {
        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void setData(int type, Bundle data) {
        throw new UnsupportedOperationException("need to implement this !!");
    }
}
