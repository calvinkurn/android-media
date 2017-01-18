package com.tokopedia.core.session;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.core.network.apiservices.user.SessionService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONObject;

import java.util.ArrayList;
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
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
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
                                        LocalCacheHandler fetchEtalaseTimer = AddProductPresenterImpl.initCacheIfNotNull(activity,
                                                AddProductPresenterImpl.FETCH_ETALASE);
                                        fetchEtalaseTimer.setExpire(0);
                                        DbManagerImpl.getInstance().removeAllEtalase();
                                        SessionHandler.clearUserData(activity);
                                        NotificationModHandler notif = new NotificationModHandler(activity);
                                        notif.cancelNotif();
                                        SessionHandler.onLogoutListener logout = (SessionHandler.onLogoutListener) activity;
                                        if (logout != null)
                                            logout.onLogout(true);
                                        progressDialog.dismiss();
                                        dismiss();
                                    } else {
                                        Toast.makeText(activity, response.getErrorMessages().get(0), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    new ErrorHandler(new ErrorListener() {
                                        @Override
                                        public void onUnknown() {
                                            Toast.makeText(activity, "Network Unknown Error!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onTimeout() {
                                            Toast.makeText(activity, "Network Timeout Error!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onServerError() {
                                            Toast.makeText(activity, "Network Internal Server Error!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onBadRequest() {
                                            Toast.makeText(activity, "Network Bad Request Error!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onForbidden() {
                                            Toast.makeText(activity, "Network Forbidden Error!", Toast.LENGTH_LONG).show();
                                        }
                                    }, responseData.code());
                                }
                            }
                        })
        );
    }

    private boolean sessionIsNotExist(TkpdResponse response) {
        String temp = "Sesi anda telah habis, silahkan login kembali.";
        return response.getErrorMessages().contains(temp);
    }

    private void logoutWsNew(Context context) {
        com.tokopedia.core.network.NetworkHandler network = new com.tokopedia.core.network.NetworkHandler(context, TkpdUrl.GCM_HELPER);
        network.AddParam("act", "remove_user_id_from_gcm");
        network.Commit(new com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
            }

            @Override
            public void getResponse(JSONObject Result) {
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
            }
        });
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
                logoutWsNew(getActivity());
                okButton.setClickable(false);

                UnifyTracking.eventLogoutLoca();
            }
        });
        super.onResume();
    }


    @Override
    public void onPause() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        super.onPause();
    }
}
