package com.tokopedia.core.session;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.Router;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.network.apiservices.user.SessionService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import java.util.HashMap;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by m.normansyah on 11/12/2015.
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class DialogLogoutFragment extends DialogFragment {
    public static final String FRAGMENT_TAG = "DialogLogoutFragment";
    CompositeSubscription compositeSubscription = new CompositeSubscription();
    Button okButton;
    TkpdProgressDialog progressDialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        progressDialog = new TkpdProgressDialog(activity, TkpdProgressDialog.NORMAL_PROGRESS);
        return new AlertDialog.Builder(getActivity())
                .setIcon(getDrawable())
                .setTitle(getString(R.string.action_logout) + " dari Tokopedia")
                .setMessage(getString(R.string.message_confirmation_logout))
                .setPositiveButton(R.string.action_logout, null)
                .setNegativeButton(R.string.title_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dismiss();
                            }
                        }).create();
    }

    private int getDrawable() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.qc_launcher2;
        else
            return R.drawable.qc_launcher;
    }

    public void logoutToTheInternet(final Activity activity) {
        SessionService sessionService = new SessionService();
        compositeSubscription.add(
                sessionService.getApi().logout(AuthUtil.generateParams(activity, new HashMap<String, String>()))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                LoginManager.getInstance().logOut();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("MNORMANSYAH", "DialogLogoutFragment : " + e.getLocalizedMessage());
                                if (e.getLocalizedMessage().contains("Unable to resolve host")) {
                                    Toast.makeText(activity, activity.getString(R.string.msg_no_connection), Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                                okButton.setClickable(true);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();
                                    if (!response.isError() || sessionIsNotExist(response)) {
//                                        CacheHomeInteractorImpl.deleteAllCache();
                                        new GlobalCacheManager().deleteAll();
                                        // clear etalase
                                        Router.clearEtalase(getActivity());
                                        DbManagerImpl.getInstance().removeAllEtalase();
                                        TrackingUtils.eventMoEngageLogoutUser();
                                        SessionHandler.clearUserData(activity);
                                        NotificationModHandler notif = new NotificationModHandler(activity);
                                        notif.dismissAllActivedNotifications();

                                        NotificationModHandler.clearCacheAllNotification(getActivity());
                                        SessionHandler.onLogoutListener logout = (SessionHandler.onLogoutListener) activity;
                                        if (logout != null)
                                            logout.onLogout(true);
                                        progressDialog.dismiss();

                                        if (getActivity() instanceof BaseActivity) {
                                            AppComponent component = ((BaseActivity) getActivity()).getApplicationComponent();
                                            Router.onLogout(getActivity(), component);
                                        }

                                        dismiss();
                                    } else {
                                        progressDialog.dismiss();
                                        dismiss();
                                        SnackbarManager.make(activity, response.getErrorMessages().get(0), Snackbar.LENGTH_LONG).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    dismiss();
                                    SnackbarManager.make(activity, getString(R.string.default_request_error_timeout), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        })
        );
    }

    private boolean sessionIsNotExist(TkpdResponse response) {
        String temp = "Sesi anda telah habis, silahkan login kembali.";
        return response.getErrorMessages().contains(temp);
    }


    @Override
    public void onResume() {
        RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        AlertDialog alertDialog = (AlertDialog) getDialog();
        okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.showDialog();
                logoutToTheInternet(getActivity());
                okButton.setClickable(false);
            }
        });
        super.onResume();
    }


    @Override
    public void onPause() {
        dismiss();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        super.onPause();
    }
}
