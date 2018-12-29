package com.tokopedia.core.referral.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.referral.data.ReferralCodeEntity;
import com.tokopedia.core.referral.domain.GetReferralDataUseCase;
import com.tokopedia.core.referral.listener.ReferralView;
import com.tokopedia.core.referral.model.ShareApps;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.ShareSocmedHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
    private TokoCashUseCase tokoCashUseCase;
    private SessionHandler sessionHandler;
    private final static int MAX_APPS = 4;
    private String url = "";

    @Inject
    public ReferralPresenter(GetReferralDataUseCase getReferralDataUseCase, TokoCashUseCase tokoCashUseCase, SessionHandler sessionHandler) {
        this.getReferralDataUseCase = getReferralDataUseCase;
        this.tokoCashUseCase = tokoCashUseCase;
        this.sessionHandler = sessionHandler;
    }


    @Override
    public void initialize() {
        remoteConfig = new FirebaseRemoteConfigImpl(getView().getActivity());
        activity = getView().getActivity();
        checkLoginAndFetchReferralCode();
    }

    @Override
    public void checkLoginAndFetchReferralCode() {
        if (sessionHandler.isV4Login(getView().getActivity())) {
            if (isAppShowReferralButtonActivated()) {
                if (sessionHandler.isMsisdnVerified()) {
                    getReferralVoucherCode();
                } else {
                    getView().showVerificationPhoneNumberPage();
                    TrackingUtils.sendMoEngageReferralScreenOpen(activity, activity.getString(R.string.referral_phone_number_verify_screen_name));
                }
            }
        } else {
            getView().navigateToLoginPage();
        }
    }

    @Override
    public void shareApp(FragmentManager fragmentManager) {
        String type = ShareData.APP_SHARE_TYPE;
        if (isAppShowReferralButtonActivated()) {
            type = ShareData.REFERRAL_TYPE;
        }
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(type)
                .setId(getView().getReferralCodeFromTextView())
                .setName(activity.getString(R.string.app_share_title))
                .setTextContent(formatSharingContents())
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .setShareUrl(url)
                .build();

        new DefaultShare(activity, shareData).show();
        TrackingUtils.sendMoEngageReferralScreenOpen(activity, activity.getString(R.string.referral_share_screen_name));
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
        getReferralDataUseCase.execute(RequestParams.EMPTY, new Subscriber<ReferralCodeEntity>() {
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
            public void onNext(ReferralCodeEntity referralCodeEntity) {
                if (!isViewAttached()) {
                    return;
                }
                if (referralCodeEntity.getErorMessage() == null) {
                    LocalCacheHandler localCacheHandler = new LocalCacheHandler(activity, TkpdCache.REFERRAL);
                    localCacheHandler.putString(TkpdCache.Key.REFERRAL_CODE, referralCodeEntity.getPromoContent().getCode());
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
        UnifyTracking.eventReferralAndShare(getView().getActivity(), AppEventTracking.Action.CLICK_COPY_REFERRAL_CODE, voucherCode);

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
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getView().getActivity(), TkpdCache.REFERRAL);
        return localCacheHandler.getString(TkpdCache.Key.REFERRAL_CODE, "");
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
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Whatsapp)) {
            shareApps = new ShareApps(TkpdState.PackageName.Whatsapp, R.drawable.btn_wa);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Line)) {
            shareApps = new ShareApps(TkpdState.PackageName.Line, R.drawable.btn_line);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Instagram)) {
            shareApps = new ShareApps(TkpdState.PackageName.Instagram, R.drawable.ic_btn_instagram);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Facebook)) {
            shareApps = new ShareApps(TkpdState.PackageName.Facebook, R.drawable.ic_btn_fb);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Gplus)) {
            shareApps = new ShareApps(TkpdState.PackageName.Gplus, R.drawable.ic_btn_g);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Twitter)) {
            shareApps = new ShareApps(TkpdState.PackageName.Twitter, R.drawable.ic_btn_twitter);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Gmail)) {
            shareApps = new ShareApps(TkpdState.PackageName.Gmail, R.drawable.ic_btn_gmail);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Sms)) {
            shareApps = new ShareApps(TkpdState.PackageName.Sms, R.drawable.ic_btn_sms);
            selectedApps[index++] = shareApps;
        }
        if (index < MAX_APPS && appInstalledOrNot(TkpdState.PackageName.Pinterest)) {
            shareApps = new ShareApps(TkpdState.PackageName.Pinterest, R.drawable.ic_pinterest_share);
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
        String type = ShareData.APP_SHARE_TYPE;
        if (isAppShowReferralButtonActivated()) {
            type = ShareData.REFERRAL_TYPE;
        }
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(type)
                .setId(getView().getReferralCodeFromTextView())
                .setName(activity.getString(R.string.app_share_title))
                .setTextContent(formatSharingContents())
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .setShareUrl(url)
                .build();

        if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Whatsapp)) {
            actionShare(shareData, TkpdState.PackageName.Whatsapp, AppEventTracking.SOCIAL_MEDIA.WHATSHAPP);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Line)) {
            actionShare(shareData, TkpdState.PackageName.Line, AppEventTracking.SOCIAL_MEDIA.LINE);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Instagram)) {
            actionShare(shareData, TkpdState.PackageName.Instagram, AppEventTracking.SOCIAL_MEDIA.INSTAGRAM);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Facebook)) {
            actionShare(shareData, TkpdState.PackageName.Facebook, AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Gplus)) {
            actionShare(shareData, TkpdState.PackageName.Gplus, AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Twitter)) {
            actionShare(shareData, TkpdState.PackageName.Twitter, AppEventTracking.SOCIAL_MEDIA.TWITTER);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Gmail)) {
            actionShare(shareData, TkpdState.PackageName.Gmail, AppEventTracking.SOCIAL_MEDIA.GMAIL);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Sms)) {
            actionShare(shareData, TkpdState.PackageName.Sms, AppEventTracking.SOCIAL_MEDIA.SMS);
        } else if (shareApp.getpackageName().equalsIgnoreCase(TkpdState.PackageName.Pinterest)) {
            actionShare(shareData, TkpdState.PackageName.Pinterest, AppEventTracking.SOCIAL_MEDIA.PINTEREST);
        } else {
            shareApp(fragmentManager);
            UnifyTracking.eventReferralAndShare(MainApplication.getAppContext(), AppEventTracking.Action.SELECT_CHANNEL, AppEventTracking.SOCIAL_MEDIA.OTHER);

        }
    }

    private void actionShare(ShareData data, String packageName, String media) {
        data.setSource(media);

        ShareSocmedHandler.ShareSpecific(data, getView().getActivity(), packageName,
                "text/plain", null, null);

        sendAnalyticsToGTM(data.getType(), media);

    }


    private void sendAnalyticsToGTM(String type, String channel) {
        if (type.equals(ShareData.REFERRAL_TYPE)) {
            UnifyTracking.eventReferralAndShare(MainApplication.getAppContext(), AppEventTracking.Action.SELECT_CHANNEL, channel);
            TrackingUtils.sendMoEngageReferralShareEvent(MainApplication.getAppContext(), channel);
        } else if (type.equals(ShareData.APP_SHARE_TYPE)) {
            UnifyTracking.eventAppShareWhenReferralOff(MainApplication.getAppContext(), AppEventTracking.Action.SELECT_CHANNEL, channel);
        } else {
            UnifyTracking.eventShare(MainApplication.getAppContext(), channel);
        }
    }
}