package com.tokopedia.referral.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.R;
import com.tokopedia.referral.Util;
import com.tokopedia.referral.data.ReferralCodeEntity;
import com.tokopedia.referral.domain.GetReferralDataUseCase;
import com.tokopedia.referral.ReferralRouter;
import com.tokopedia.referral.view.listener.ReferralView;
import com.tokopedia.referral.domain.model.ShareApps;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralPresenter extends BaseDaggerPresenter<ReferralView> implements IReferralPresenter {

    private String contents = "";
    private RemoteConfig remoteConfig;
    private Activity activity;
    private GetReferralDataUseCase getReferralDataUseCase;
    private UserSession userSession;
    private final static int MAX_APPS = 4;
    private String url = "";

    @Inject
    public ReferralPresenter(GetReferralDataUseCase getReferralDataUseCase) {
        this.getReferralDataUseCase = getReferralDataUseCase;
    }


    @Override
    public void initialize() {
        remoteConfig = new FirebaseRemoteConfigImpl(getView().getActivity());
        activity = getView().getActivity();
        userSession = new UserSession(getView().getActivity());
        checkLoginAndFetchReferralCode();
    }

    @Override
    public void checkLoginAndFetchReferralCode() {
        if (userSession.isLoggedIn()) {
            if (isAppShowReferralButtonActivated()) {
                if (userSession.isMsisdnVerified()) {
                    getReferralVoucherCode();
                } else {
                    getView().showVerificationPhoneNumberPage();
                    ((ReferralRouter)activity.getApplicationContext()).sendMoEngageReferralScreenOpen(activity,
                            activity.getString(R.string.referral_phone_number_verify_screen_name));
                }
            }
        } else {
            getView().navigateToLoginPage();
        }
    }

    @Override
    public void shareApp(FragmentManager fragmentManager) {
        String type = Constants.Values.Companion.APP_SHARE_TYPE;
        if (isAppShowReferralButtonActivated()) {
            type = Constants.Values.Companion.REFERRAL_TYPE;
        }
        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put(Constants.Key.Companion.TYPE, type);
        keyValueMap.put(Constants.Key.Companion.REFERRAL_CODE, getView().getReferralCodeFromTextView());
        keyValueMap.put(Constants.Key.Companion.NAME, activity.getString(R.string.app_share_title));
        keyValueMap.put(Constants.Key.Companion.SHARING_CONTENT, formatSharingContents());
        keyValueMap.put(Constants.Key.Companion.URI, Constants.Values.Companion.WEB_PLAYSTORE_BUYER_APP_URL);
        keyValueMap.put(Constants.Key.Companion.URL, url);
        ((ReferralRouter)activity.getApplicationContext()).executeDefaultShare(activity, keyValueMap);
        ((ReferralRouter)activity.getApplicationContext()).sendMoEngageReferralScreenOpen(activity,
                activity.getString(R.string.referral_share_screen_name));
    }

    private String formatSharingContents() {
        if (!isAppShowReferralButtonActivated()) {
            contents = getAppShareDescription();
        } else if (TextUtils.isEmpty(contents)) {
            contents = getAppShareDefaultMessage();
        }
        if (url != null && !contents.contains(url)) {
            contents = contents + url;
        }
        return contents;
    }

    @Override
    public void getReferralVoucherCode() {
        getView().showProcessDialog();
        getReferralDataUseCase.execute(Util.getPostRequestBody(userSession),new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().hideProcessDialog();
                e.printStackTrace();
                if (TextUtils.isEmpty(getVoucherCodeFromCache())) {
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

                    getView().renderErrorGetVoucherCode(message);
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<ReferralCodeEntity>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                DataResponse ticketListResponse = res1.getData();
                ReferralCodeEntity referralCodeEntity = (ReferralCodeEntity) ticketListResponse.getData();
                if (!isViewAttached()) {
                    return;
                }
                if (referralCodeEntity.getErorMessage() == null) {
                    LocalCacheHandler localCacheHandler = new LocalCacheHandler(activity, Constants.Values.Companion.REFERRAL);
                    localCacheHandler.putString(Constants.Key.Companion.REFERRAL_CODE, referralCodeEntity.getPromoContent().getCode());
                    localCacheHandler.applyEditor();
                    contents = referralCodeEntity.getPromoContent().getContent();
                    getView().renderVoucherCodeData(referralCodeEntity);
                    url = referralCodeEntity.getPromoContent().getShareUrl();
                } else {
                    getView().renderErrorGetVoucherCode(referralCodeEntity.getErorMessage());
                }
                getView().hideProcessDialog();

            }
        });
    }

    @Override
    public void copyVoucherCode(String voucherCode) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(getView().getActivity().getString(R.string.copy_coupon_code_text), formatSharingContents());
        clipboard.setPrimaryClip(clip);
        if (TextUtils.isEmpty(voucherCode)) {
            getView().showToastMessage(getView().getActivity().getString(R.string.no_coupon_to_copy_text));
        } else {
            getView().showToastMessage(getView().getActivity().getString(R.string.copy_coupon_code_text) + " " + voucherCode);
        }
        ((ReferralRouter)getView().getActivity().getApplicationContext()).eventReferralAndShare(getView().getActivity(),
                Constants.Action.Companion.CLICK_COPY_REFERRAL_CODE, voucherCode);
    }

    @Override
    public String getReferralSubHeader() {
        if (isAppShowReferralButtonActivated()) {
            return remoteConfig.getString(RemoteConfigKey.REFERRAL_SUBHEADER, getView().getActivity().getString(R.string.app_share_referral_label_desc));
        } else {
            return getView().getActivity().getString(R.string.app_share_label_desc);
        }
    }

    @Override
    public String getHowItWorks() {
        return remoteConfig.getString(RemoteConfigKey.APP_REFERRAL_HOWITWORKS, getView().getActivity().getString(R.string.title_app_referral_howitworks));
    }

    @Override
    public String getVoucherCodeFromCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getView().getActivity(), Constants.Values.Companion.REFERRAL);
        return localCacheHandler.getString(Constants.Key.Companion.REFERRAL_CODE, "");
    }

    @Override
    public Boolean isAppShowReferralButtonActivated() {
        return remoteConfig.getBoolean(RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON);
    }

    private String getAppShareDescription() {
        return remoteConfig.getString(RemoteConfigKey.APP_SHARE_DESCRIPTION, getView().getActivity().getString(R.string.app_share_label_desc));
    }

    private String getAppShareDefaultMessage() {
        String message = remoteConfig.getString(RemoteConfigKey.APP_SHARE_DEFAULT_MESSAGE, getView().getActivity().getString(R.string.app_share_default_msg));
        if (TextUtils.isEmpty(message)) {
            message = getView().getActivity().getString(R.string.app_share_default_msg);
        }
        message = message.replace("%s", getVoucherCodeFromCache());
        return message;
    }

    @Override
    public String getReferralTitleDesc() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_TITLE_DESC, getView().getActivity().getString(R.string.referral_title_desc));
    }

    public ShareApps[] checkInstalledApps() {
        int index = 0;
        ShareApps shareApps;
        ShareApps[] selectedApps = new ShareApps[MAX_APPS];
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.WHATSAPP)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.WHATSAPP, R.drawable.btn_wa);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.LINE)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.LINE, R.drawable.btn_line);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.INSTAGRAM)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.INSTAGRAM, R.drawable.ic_btn_instagram);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.FACEBOOK)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.FACEBOOK, R.drawable.ic_btn_fb);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.GPLUS)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.GPLUS, R.drawable.ic_btn_g);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.TWITTER)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.TWITTER, R.drawable.ic_btn_twitter);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.GMAIL)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.GMAIL, R.drawable.ic_btn_gmail);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.SMS)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.SMS, R.drawable.ic_btn_sms);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(Constants.PackageName.Companion.PINTEREST)) {
            shareApps = new ShareApps(Constants.PackageName.Companion.PINTEREST, R.drawable.ic_pinterest_share);
            selectedApps[index++] = shareApps;
        }
        return selectedApps;
    }

    public void getSharableApps() {
        getShareAppsObservable().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShareApps[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShareApps[] shareApps) {
                        try {
                            int index = 0;
                            if (shareApps != null) {
                                for (ShareApps shareApp : shareApps) {
                                    if (shareApp != null) { //shareApps is an array so it will complete the loop till length
                                        getView().renderSharableApps(shareApp, index++);
                                    } else {
                                        break;
                                    }
                                }
                            }
                            ShareApps shareApp = new ShareApps("", R.drawable.ic_btn_share_more);
                            getView().renderSharableApps(shareApp, index);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getView().getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private Observable<ShareApps[]> getShareAppsObservable() {

        return Observable.just(null).map(new Func1<Object, ShareApps[]>() {
            @Override
            public ShareApps[] call(Object o) {
                return checkInstalledApps();
            }
        });
    }

    public void appShare(ShareApps shareApp, FragmentManager fragmentManager) {
        String type = Constants.Values.Companion.APP_SHARE_TYPE;
        if (isAppShowReferralButtonActivated()) {
            type = Constants.Values.Companion.REFERRAL_TYPE;
        }

        HashMap<String, String> shareData = new HashMap<>();
        shareData.put(Constants.Key.Companion.TYPE, type);
        shareData.put(Constants.Key.Companion.REFERRAL_CODE, getView().getReferralCodeFromTextView());
        shareData.put(Constants.Key.Companion.NAME, activity.getString(R.string.app_share_title));
        shareData.put(Constants.Key.Companion.SHARING_CONTENT, formatSharingContents());
        shareData.put(Constants.Key.Companion.URI, Constants.Values.Companion.WEB_PLAYSTORE_BUYER_APP_URL);
        shareData.put(Constants.Key.Companion.URL, url);


        if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.WHATSAPP)) {
            actionShare(shareData, Constants.PackageName.Companion.WHATSAPP, Constants.Label.Companion.WHATSHAPP);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.LINE)) {
            actionShare(shareData, Constants.PackageName.Companion.LINE, Constants.Label.Companion.LINE);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.INSTAGRAM)) {
            actionShare(shareData, Constants.PackageName.Companion.INSTAGRAM, Constants.Label.Companion.INSTAGRAM);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.FACEBOOK)) {
            actionShare(shareData, Constants.PackageName.Companion.FACEBOOK, Constants.Label.Companion.FACEBOOK);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.GPLUS)) {
            actionShare(shareData, Constants.PackageName.Companion.GPLUS, Constants.Label.Companion.GOOGLE_PLUS);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.TWITTER)) {
            actionShare(shareData, Constants.PackageName.Companion.TWITTER, Constants.Label.Companion.TWITTER);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.GMAIL)) {
            actionShare(shareData, Constants.PackageName.Companion.GMAIL, Constants.Label.Companion.GMAIL);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.SMS)) {
            actionShare(shareData, Constants.PackageName.Companion.SMS, Constants.Label.Companion.SMS);
        } else if (shareApp.getpackageName().equalsIgnoreCase(Constants.PackageName.Companion.PINTEREST)) {
            actionShare(shareData, Constants.PackageName.Companion.PINTEREST, Constants.Label.Companion.PINTEREST);
        } else {
            shareApp(fragmentManager);
            ((ReferralRouter)activity.getApplicationContext()).eventReferralAndShare(activity.getApplicationContext(),
                    Constants.Values.Companion.SELECT_CHANNEL, Constants.Label.Companion.OTHER);

        }
    }

    private void actionShare(HashMap<String, String> data, String packageName, String media) {
        data.put(Constants.Key.Companion.MEDIA, media);

        ((ReferralRouter)getView().getActivity().getApplicationContext()).executeShareSocmedHandler(
                getView().getActivity(), data, packageName
        );
        ((ReferralRouter)getView().getActivity().getApplicationContext()).sendAnalyticsToGTM(getView().getActivity().getApplicationContext(),
                data.get(Constants.Key.Companion.TYPE), media);
    }
}