package com.tokopedia.core.referral.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.referral.ReferralService;
import com.tokopedia.core.network.apiservices.referral.apis.ReferralApi;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.referral.data.ReferralCodeEntity;
import com.tokopedia.core.referral.data.TkpdReferralResponse;
import com.tokopedia.core.referral.listner.ReferralView;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralPresenter implements IReferralPresenter {

    private Activity activity;
    private ReferralView view;
    private String contents = "";
    private RemoteConfig remoteConfig;
    public static final String ShareScreenName = "Share Channel";
    public static final String phoneVerificationScreenName = "Phone Number Verification";



    public ReferralPresenter(ReferralView view) {
        this.activity = view.getActivity();
        this.view = view;
        remoteConfig = new FirebaseRemoteConfigImpl(view.getActivity());
    }

    @Override
    public void initialize() {
        if (view.isUserLoggedIn()) {
            if(isAppShowReferralButtonActivated()) {
                if (view.isUserPhoneNumberVerified()) {
                    getReferralVoucherCode();

                } else {
                    view.showVerificationPhoneNumberPage();
                    TrackingUtils.sendMoEngageReferralScreenOpen(phoneVerificationScreenName);

                }
            }
        } else {
            view.navigateToLoginPage();
        }
    }

    @Override
    public void shareApp() {

        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.APP_SHARE_TYPE)
                .setId(view.getReferralCodeFromTextView())
                .setName(activity.getString(R.string.app_share_title))
                .setTextContent(activity.getString(R.string.app_share_title) + "\n " + contents + " ")
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .build();
        activity.startActivity(ShareActivity.createIntent(activity, shareData));
        TrackingUtils.sendMoEngageReferralScreenOpen(ShareScreenName);


    }

    @Override
    public void getReferralVoucherCode() {
        if (getVoucherCodeFromCache() == null || "".equalsIgnoreCase(getVoucherCodeFromCache())) {
            view.showProcessDialog();
        }
        Observable<ReferralCodeEntity> observer = processGetReferralVoucherCode();
        observer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<ReferralCodeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProcessDialog();
                        e.printStackTrace();
                        if (getVoucherCodeFromCache() == null || "".equalsIgnoreCase(getVoucherCodeFromCache())) {
                            return;
                        }
                        String message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                        if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            message = ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL;
                        } else if (e instanceof SocketTimeoutException) {
                            message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                        } else if (e instanceof ResponseErrorException
                                || e instanceof ResponseDataNullException
                                || e instanceof HttpErrorException) {
                            message = e.getMessage();
                        }
                        NetworkErrorHelper.createSnackbarWithAction(view.getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getReferralVoucherCode();
                            }
                        }).showRetrySnackbar();
                    }

                    @Override
                    public void onNext(ReferralCodeEntity referralCodeEntity) {
                        if (referralCodeEntity.getErorMessage() == null) {
                            LocalCacheHandler localCacheHandler = new LocalCacheHandler(activity, TkpdCache.REFERRAL);
                            localCacheHandler.putString(TkpdCache.Key.REFERRAL_CODE, referralCodeEntity.getPromoContent().getCode());
                            localCacheHandler.applyEditor();
                            contents = referralCodeEntity.getPromoContent().getContent();
                            view.renderVoucherCode(referralCodeEntity.getPromoContent().getCode());
                        } else {
                            NetworkErrorHelper.createSnackbarWithAction(view.getActivity(), referralCodeEntity.getErorMessage(), new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getReferralVoucherCode();
                                }
                            }).showRetrySnackbar();
                        }
                        view.hideProcessDialog();
                    }
                });

    }

    private Observable<ReferralCodeEntity> processGetReferralVoucherCode() {
        JsonObject requestBody = new JsonObject();
        JsonObject requestBody1 = new JsonObject();
        requestBody.addProperty("user_id", Integer.parseInt(SessionHandler.getLoginID(activity)));
        requestBody.addProperty("msisdn", SessionHandler.getPhoneNumber());
        requestBody1.add("data", requestBody);
        ReferralApi referralApi = new ReferralService().getApi();
        return referralApi.getReferralVoucherCode(requestBody1.toString()).map(new Func1<String, ReferralCodeEntity>() {
            @Override
            public ReferralCodeEntity call(String tkpdResponseResponse) {
                Log.e("response", tkpdResponseResponse);
                TkpdReferralResponse factory = TkpdReferralResponse.factory(tkpdResponseResponse);
                ReferralCodeEntity referralCodeEntity = new ReferralCodeEntity();
                if (factory.isError() && factory.getErrorMessages() != null && factory.getErrorMessages().size() > 0) {
                    referralCodeEntity.setErorMessage(factory.getErrorMessages().get(0));
                } else {
                    referralCodeEntity = factory.convertToObj(ReferralCodeEntity.class);// new Gson().fromJson(tkpdResponseResponse, ReferralCodeEntity.class);

                }
                return referralCodeEntity;
            }
        });
    }

    @Override
    public void copyVoucherCode(String voucherCode) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(view.getActivity().getString(R.string.copied_to_clipboard), voucherCode);
        clipboard.setPrimaryClip(clip);
        view.showToastMessage(view.getActivity().getString(R.string.copied_to_clipboard) + " " + voucherCode);
        UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_COPY_REFERRAL_CODE, voucherCode);

    }

    @Override
    public String getReferralContents() {
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFFERAL_CONTENT, view.getActivity().getString(R.string.app_share_label_desc));
    }

    @Override
    public String getHowItWorks() {
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFERRAL_HOWITWORKS, view.getActivity().getString(R.string.title_app_referral_howitworks));
    }

    @Override
    public String getVoucherCodeFromCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), TkpdCache.REFERRAL);
        return localCacheHandler.getString(TkpdCache.Key.REFERRAL_CODE, "");
    }

    @Override
    public Boolean isAppShowReferralButtonActivated(){
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON);

    }
}