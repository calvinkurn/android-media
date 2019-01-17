package com.tokopedia.referral.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.R;
import com.tokopedia.referral.view.activity.ReferralActivity;
import com.tokopedia.referral.ReferralRouter;
import com.tokopedia.referral.view.listener.FriendsWelcomeView;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * Created by ashwanityagi on 04/12/17.
 */

public class ReferralFriendsWelcomePresenter extends BaseDaggerPresenter<FriendsWelcomeView> implements IReferralFriendsWelcomePresenter {

    private final String CODE_KEY = "code";
    private UserSession userSession;
    private RemoteConfig remoteConfig;

    @Inject
    public ReferralFriendsWelcomePresenter() {
    }

    @Override
    public void initialize() {
        userSession = new UserSession(getView().getActivity());
        remoteConfig = new FirebaseRemoteConfigImpl(getView().getActivity());
        if (getView().getActivity().getIntent() != null && getView().getActivity().getIntent().getExtras() != null) {
            String code = getView().getActivity().getIntent().getExtras().getString(CODE_KEY);
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(getView().getActivity(), Constants.Values.Companion.REFERRAL);
            if (code == null || code.equalsIgnoreCase(localCacheHandler.getString(Constants.Key.Companion.REFERRAL_CODE, ""))) {
                if (userSession.isLoggedIn()) {
                    getView().getActivity().startActivity(ReferralActivity.getCallingIntent(getView().getActivity()));
                }
                getView().closeView();
            }
            ((ReferralRouter)getView().getActivity().getApplicationContext()).setBranchReferralCode(code);
        }

    }

    public String getHowItWorks() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_TEXT_WELCOME, getView().getActivity().getString(R.string.cashback_enter_tokocash));
    }

    @Override
    public String getSubHeaderFromFirebase() {

        return remoteConfig.getString(RemoteConfigKey.REFERRAL_WELCOME_MESSAGE, getView().getActivity().getString(R.string.referral_welcome_desc));
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
        ((ReferralRouter)getView().getActivity().getApplicationContext()).eventReferralAndShare(getView().getActivity(),
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