package com.tokopedia.core.referral.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
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
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TokoCashTypeDef;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

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
    public ReferralPresenter(GetReferralDataUseCase getReferralDataUseCase, TokoCashUseCase tokoCashUseCase , SessionHandler sessionHandler) {
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
    public void shareApp() {
        formatSharingContents();
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.APP_SHARE_TYPE)
                .setId(getView().getReferralCodeFromTextView())
                .setName(activity.getString(R.string.app_share_title))
                .setTextContent(contents)
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .build();
        activity.startActivity(ShareActivity.createIntent(activity, shareData));
        TrackingUtils.sendMoEngageReferralScreenOpen(activity.getString(R.string.referral_share_screen_name));


    }

    private void formatSharingContents() {
        if (!isAppShowReferralButtonActivated()) {
            contents = getAppShareDescription();
        } else if (TextUtils.isEmpty(contents)) {
            contents = getAppShareDefaultMessage();
        }
        contents = contents + " Cek - ";
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
                if (referralCodeEntity.getErorMessage() == null) {
                    LocalCacheHandler localCacheHandler = new LocalCacheHandler(activity, TkpdCache.REFERRAL);
                    localCacheHandler.putString(TkpdCache.Key.REFERRAL_CODE, referralCodeEntity.getPromoContent().getCode());
                    localCacheHandler.applyEditor();
                    contents = referralCodeEntity.getPromoContent().getContent();
                    getView().renderVoucherCode(referralCodeEntity.getPromoContent().getCode());
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
        tokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<TokoCashModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("ManagePaymentOptionsPresenter :: inside tokocash subscriber error");
            }

            @Override
            public void onNext(TokoCashModel tokoCashModel) {
                if (tokoCashModel != null
                        && tokoCashModel.isSuccess()
                        && tokoCashModel.getTokoCashData() != null
                        && tokoCashModel.getTokoCashData().getAction() != null) {
                    if (tokoCashModel.getTokoCashData().getLink() == TokoCashTypeDef.TOKOCASH_ACTIVE) {
                        getReferralVoucherCode();
                    } else {

                        WalletRouterUtil.navigateWallet(
                                getView().getActivity().getApplication(),
                                getView().getActivity(),
                                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                                tokoCashModel.getTokoCashData().getAction().getmAppLinks() == null ? "" : tokoCashModel.getTokoCashData().getAction().getmAppLinks(),
                                tokoCashModel.getTokoCashData().getAction().getRedirectUrl() == null ? "" : tokoCashModel.getTokoCashData().getAction().getRedirectUrl(),
                                new Bundle()
                        );
                    }
                }
            }
        });
    }
}