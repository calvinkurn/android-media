package com.tokopedia.core.referral.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.referral.data.ReferralCodeEntity;
import com.tokopedia.core.referral.domain.GetReferralDataUseCase;
import com.tokopedia.core.referral.listener.ReferralView;
import com.tokopedia.core.referral.model.ShareApps;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.share.ShareBottomSheet;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.ShareSocmedHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.var.TokoCashTypeDef;

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
        if (sessionHandler.isV4Login(getView().getActivity())) {
            if (isAppShowReferralButtonActivated()) {
                if (sessionHandler.isMsisdnVerified()) {
                    fetchTokoCashBalance();
                } else {
                    getView().showVerificationPhoneNumberPage();
                    TrackingUtils.sendMoEngageReferralScreenOpen(activity.getString(R.string.referral_phone_number_verify_screen_name));

                }
            }
        } else {
            getView().navigateToLoginPage();
        }
    }

    @Override
    public void shareApp(FragmentManager fragmentManager) {
        formatSharingContents();
        String type = ShareData.APP_SHARE_TYPE;
        if (isAppShowReferralButtonActivated()) {
            type = ShareData.REFERRAL_TYPE;
        }
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(type)
                .setId(getView().getReferralCodeFromTextView())
                .setName(activity.getString(R.string.app_share_title))
                .setTextContent(contents)
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .build();

        ShareBottomSheet.show(fragmentManager, shareData);
        TrackingUtils.sendMoEngageReferralScreenOpen(activity.getString(R.string.referral_share_screen_name));
    }

    private void formatSharingContents() {
        if (!isAppShowReferralButtonActivated()) {
            contents = getAppShareDescription();
        } else if (TextUtils.isEmpty(contents)) {
            contents = getAppShareDefaultMessage();
        }
        if (!contents.contains(activity.getString(R.string.cek_label))) {
            contents = contents + activity.getString(R.string.cek_label);
        }

    }

    @Override
    public void getReferralVoucherCode() {
        if (TextUtils.isEmpty(getVoucherCodeFromCache())) {
            getView().showProcessDialog();
        }

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
                    getView().renderVoucherCodeData(referralCodeEntity.getPromoContent().getCode(), referralCodeEntity.getPromoContent().getFriendCount());
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
        android.content.ClipData clip = android.content.ClipData.newPlainText(getView().getActivity().getString(R.string.copy_coupon_code_text), voucherCode);
        clipboard.setPrimaryClip(clip);
        if (TextUtils.isEmpty(voucherCode)) {
            getView().showToastMessage(getView().getActivity().getString(R.string.no_coupon_to_copy_text));
        } else {
            getView().showToastMessage(getView().getActivity().getString(R.string.copy_coupon_code_text) + " " + voucherCode);
        }
        UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_COPY_REFERRAL_CODE, voucherCode);

    }

    @Override
    public String getReferralContents() {
        if (isAppShowReferralButtonActivated()) {
            return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFFERAL_CONTENT, getView().getActivity().getString(R.string.app_share_referral_label_desc));
        } else {
            return getView().getActivity().getString(R.string.app_share_label_desc);
        }
    }

    @Override
    public String getHowItWorks() {
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFERRAL_HOWITWORKS, getView().getActivity().getString(R.string.title_app_referral_howitworks));
    }

    @Override
    public String getVoucherCodeFromCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getView().getActivity(), TkpdCache.REFERRAL);
        return localCacheHandler.getString(TkpdCache.Key.REFERRAL_CODE, "");
    }

    @Override
    public Boolean isAppShowReferralButtonActivated() {
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON);
    }

    private String getAppShareDescription() {
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_SHARE_DESCRIPTION, getView().getActivity().getString(R.string.app_share_label_desc));
    }

    private String getAppShareDefaultMessage() {
        String message = remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_SHARE_DEFAULT_MESSAGE, getView().getActivity().getString(R.string.app_share_default_msg));
        if (TextUtils.isEmpty(message)) {
            message = getView().getActivity().getString(R.string.app_share_default_msg);
        }
        message = message.replace("%s", getVoucherCodeFromCache());
        return message;
    }

    /**
     * This function fetches the tokocash balance and then check Voucher code
     */
    private void fetchTokoCashBalance() {
        tokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<TokoCashData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("ManagePaymentOptionsPresenter :: inside tokocash subscriber error");
            }

            @Override
            public void onNext(TokoCashData tokoCashData) {
                if (!isViewAttached()) {
                    return;
                }

                if (tokoCashData != null
                        && tokoCashData.getAction() != null) {
                    if (tokoCashData.getLink() == TokoCashTypeDef.TOKOCASH_ACTIVE) {
                        getReferralVoucherCode();
                    } else {

                        WalletRouterUtil.navigateWallet(
                                getView().getActivity().getApplication(),
                                getView().getActivity(),
                                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                                tokoCashData.getAction().getmAppLinks() == null ?
                                        "" : tokoCashData.getAction().getmAppLinks(),
                                tokoCashData.getAction().getRedirectUrl() == null ?
                                        "" : tokoCashData.getAction().getRedirectUrl(),
                                new Bundle()
                        );
                    }
                }
            }
        });
    }


    public ShareApps[] checkInstalledApps() {
        int index = 0;
        ShareApps shareApps;
        ShareApps[] selectedApps = new ShareApps[4];
        if (appInstalledOrNot(TkpdState.PackageName.Whatsapp)) {
            shareApps = new ShareApps(TkpdState.PackageName.Whatsapp, R.drawable.ic_btn_wa);
            selectedApps[index++] = shareApps;
        }
        if (appInstalledOrNot(TkpdState.PackageName.Line)) {
            shareApps = new ShareApps(TkpdState.PackageName.Line, R.drawable.ic_btn_line);
            selectedApps[index++] = shareApps;
        }
        if (appInstalledOrNot(TkpdState.PackageName.Instagram)) {
            shareApps = new ShareApps(TkpdState.PackageName.Instagram, R.drawable.ic_btn_instagram);
            selectedApps[index++] = shareApps;
        }
        if (appInstalledOrNot(TkpdState.PackageName.Facebook)) {
            shareApps = new ShareApps(TkpdState.PackageName.Facebook, R.drawable.ic_btn_fb);
            selectedApps[index++] = shareApps;
        }
        if (index < 4 && appInstalledOrNot(TkpdState.PackageName.Gplus)) {
            shareApps = new ShareApps(TkpdState.PackageName.Gplus, R.drawable.ic_google_share);
            selectedApps[index++] = shareApps;
        }
        if (index < 4 && appInstalledOrNot(TkpdState.PackageName.Twitter)) {
            shareApps = new ShareApps(TkpdState.PackageName.Twitter, R.drawable.ic_twitter_share);
            selectedApps[index++] = shareApps;
        }
        if (index < 4 && appInstalledOrNot(TkpdState.PackageName.Pinterest)) {
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
                            if (shareApps != null) {
                                int index = 0;
                                for (ShareApps shareApp : shareApps) {
                                    getView().renderSharableApps(shareApp, index++);
                                }
                            }
                            ShareApps shareApp = new ShareApps("", R.drawable.ic_more_share);
                            getView().renderSharableApps(shareApp, 4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getView().getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
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

    public void appShare(ShareApps shareApp,FragmentManager fragmentManager) {
        formatSharingContents();
        String type = ShareData.APP_SHARE_TYPE;
        if (isAppShowReferralButtonActivated()) {
            type = ShareData.REFERRAL_TYPE;
        }
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(type)
                .setId(getView().getReferralCodeFromTextView())
                .setName(activity.getString(R.string.app_share_title))
                .setTextContent(contents)
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .build();

        if (shareApp.getPackageNmae().equalsIgnoreCase(TkpdState.PackageName.Whatsapp)) {
            shareWhatsApp(shareData);
        } else if (shareApp.getPackageNmae().equalsIgnoreCase(TkpdState.PackageName.Line)) {
            shareLine(shareData);

        } else if (shareApp.getPackageNmae().equalsIgnoreCase(TkpdState.PackageName.Instagram)) {
            shareInstagram(shareData);

        } else if (shareApp.getPackageNmae().equalsIgnoreCase(TkpdState.PackageName.Facebook)) {
            shareFb(shareData);

        } else if (shareApp.getPackageNmae().equalsIgnoreCase(TkpdState.PackageName.Gplus)) {
            shareGPlus(shareData);

        } else if (shareApp.getPackageNmae().equalsIgnoreCase(TkpdState.PackageName.Twitter)) {
            shareTwitter(shareData);

        } else if (shareApp.getPackageNmae().equalsIgnoreCase(TkpdState.PackageName.Pinterest)) {
            sharePinterest(shareData);

        } else {
            shareApp(fragmentManager);
            if (isAppShowReferralButtonActivated()) {
                UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_SHARE_CODE, getView().getReferralCodeFromTextView());
            } else {
                UnifyTracking.eventAppShareWhenReferralOff(AppEventTracking.Action.CLICK, AppEventTracking.EventLabel.APP_SHARE_LABEL);
            }
        }
    }


    public void shareFb(final ShareData data) {

//        sendAnalyticsToGTM(data.getType(),AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
//        data.setSource(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
//        ConnectionDetector detector = new ConnectionDetector(this.activity);
//        boolean expired = facebookCache.isExpired();
//
//        if (detector.isConnectingToInternet()) {
//            if (expired) {
//                LoginManager.getInstance().logOut();
//            }
//            BranchSdkUtils.generateBranchLink(data, activity, new BranchSdkUtils.GenerateShareContents() {
//                @Override
//                public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
//                    view.showDialogShareFb(branchUrl);
//                }
//            });
//
//        } else {
//            NetworkErrorHelper.showSnackbar(this.activity);
//        }

    }


    public void shareTwitter(ShareData data) {

        sendAnalyticsToGTM(data.getType(), AppEventTracking.SOCIAL_MEDIA.TWITTER);

        data.setSource(AppEventTracking.SOCIAL_MEDIA.TWITTER);
        if (data.getImgUri() != null) {
            ShareSocmedHandler.ShareSpecificUri(data, activity, TkpdState.PackageName.Twitter,
                    TkpdState.PackageName.TYPE_IMAGE, data.getImgUri(), TkpdState.PackageName
                            .TWITTER_DEFAULT + "url=" + data.getUri() + "&text=" + data.getName());
        } else {
            ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Twitter,
                    TkpdState.PackageName.TYPE_TEXT, null, null);
        }

    }

    public void shareWhatsApp(ShareData data) {

        sendAnalyticsToGTM(data.getType(), AppEventTracking.SOCIAL_MEDIA.WHATSHAPP);

        data.setSource(AppEventTracking.SOCIAL_MEDIA.WHATSHAPP);
        ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Whatsapp,
                TkpdState.PackageName.TYPE_TEXT, null, null);

    }


    public void sharePinterest(ShareData data) {

        sendAnalyticsToGTM(data.getType(), AppEventTracking.SOCIAL_MEDIA.PINTEREST);

        data.setSource(AppEventTracking.SOCIAL_MEDIA.PINTEREST);
        if (data.getImgUri() != null) {
            ShareSocmedHandler.ShareSpecificUri(data, activity, TkpdState.PackageName.Pinterest,
                    TkpdState.PackageName.TYPE_TEXT, data.getImgUri(), null);
        } else {
            ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Pinterest,
                    TkpdState.PackageName.TYPE_TEXT, null, null);
        }

    }


    public void shareMore(ShareData data) {


    }

    public void shareLine(ShareData data) {
        sendAnalyticsToGTM(data.getType(), AppEventTracking.SOCIAL_MEDIA.LINE);
        data.setSource(AppEventTracking.SOCIAL_MEDIA.LINE);
        ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Line,
                TkpdState.PackageName.TYPE_TEXT, null, null);

    }

    public void shareInstagram(ShareData data) {

        sendAnalyticsToGTM(data.getType(), AppEventTracking.SOCIAL_MEDIA.INSTAGRAM);

        data.setSource(AppEventTracking.SOCIAL_MEDIA.INSTAGRAM);
        if (data.getImgUri() != null) {
            ShareSocmedHandler.ShareSpecificUri(data, activity, TkpdState.PackageName.Instagram,
                    TkpdState.PackageName.TYPE_IMAGE,
                    data.getImgUri(), null);
        } else {
            ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Instagram,
                    TkpdState.PackageName.TYPE_TEXT, null, null);
        }

    }

    public void shareGPlus(ShareData data) {

        data.setSource(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Gplus,
                TkpdState.PackageName.TYPE_IMAGE,
                null, null);

    }

    private void sendAnalyticsToGTM(String type, String channel) {
        if (type.equals(ShareData.REFERRAL_TYPE)) {
            UnifyTracking.eventReferralAndShare(AppEventTracking.Action.SELECT_CHANNEL, channel);
            TrackingUtils.sendMoEngageReferralShareEvent(channel);
        } else if (type.equals(ShareData.APP_SHARE_TYPE)) {
            UnifyTracking.eventAppShareWhenReferralOff(AppEventTracking.Action.SELECT_CHANNEL, channel);
        } else {
            UnifyTracking.eventShare(channel);
        }
    }
}