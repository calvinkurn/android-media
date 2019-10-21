package com.tokopedia.referral.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.R;
import com.tokopedia.referral.analytics.ReferralAnalytics;
import com.tokopedia.referral.view.activity.ReferralActivity;
import com.tokopedia.referral.view.listener.FriendsWelcomeView;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.inject.Inject;

/**
 * Created by ashwanityagi on 04/12/17.
 */

public class ReferralFriendsWelcomePresenter extends BaseDaggerPresenter<FriendsWelcomeView> implements IReferralFriendsWelcomePresenter {

    private UserSession userSession;
    private RemoteConfig remoteConfig;
    private ReferralAnalytics referralAnalytics;

    @Inject
    public ReferralFriendsWelcomePresenter(UserSession userSession, RemoteConfig remoteConfig,
                                           ReferralAnalytics referralAnalytics) {
        this.userSession = userSession;
        this.remoteConfig = remoteConfig;
        this.referralAnalytics = referralAnalytics;
    }

    @Override
    public void initialize(String code) {
        if (!TextUtils.isEmpty(code)) {
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(getView().getActivity(), Constants.Values.Companion.REFERRAL);
            if (code == null || code.equalsIgnoreCase(localCacheHandler.getString(Constants.Key.Companion.REFERRAL_CODE, ""))) {
                if (userSession.isLoggedIn()) {
                    getView().getActivity().startActivity(ReferralActivity.getCallingIntent(getView().getActivity()));
                }
                getView().closeView();
            }
            PersistentCacheManager.instance.put(Constants.Cache.KEY_CACHE_PROMO_CODE, code);
        }

    }

    public String getHowItWorks() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_TEXT_WELCOME, getView().getActivity().getString(R.string.cashback_enter_tokocash));
    }

    @Override
    public String getSubHeaderFromFirebase(String owner) {
        String subHeaderMessage = remoteConfig.getString(RemoteConfigKey.REFERRAL_WELCOME_MESSAGE, getView().getActivity().getString(R.string.referral_welcome_desc));
        String user = "Topper";
        if (userSession.isLoggedIn()) user = userSession.getName();
        try {
            owner = URLDecoder.decode(owner, Constants.Values.ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return FindAndReplaceHelper.findAndReplacePlaceHolders(subHeaderMessage, Constants.Placeholder.USER, user,
                Constants.Placeholder.OWNER, owner != null ? owner : "");
    }

    @Override
    public void copyVoucherCode(String code) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getView().getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(getView().getActivity().getString(R.string.copy_coupon_code_text), code);
        clipboard.setPrimaryClip(clip);
        if (TextUtils.isEmpty(code)) {
            getView().showToastMessage(getView().getActivity().getString(R.string.no_coupon_to_copy_text));
        } else {
            getView().showToastMessage(getView().getActivity().getString(R.string.copy_coupon_code_text) + " " + code);
        }
        referralAnalytics.eventReferralAndShare(
                Constants.Action.Companion.CLICK_COPY_REFERRAL_CODE, code);
    }

    public String getHelpButtonContentTitle() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_CONTENT_TITLE, getView().getActivity().getString(R.string.acquisition_referral));
    }


    public String getHelpButtonContentSubtitle() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_CONTENT_SUBTITLE, getView().getActivity().getString(R.string.what_is_referral_tokocash));
    }

    public boolean isShowReferralHelpLink() {
        return remoteConfig.getBoolean(RemoteConfigKey.SHOW_REFERRAL_HELP_LINK, false);
    }
}