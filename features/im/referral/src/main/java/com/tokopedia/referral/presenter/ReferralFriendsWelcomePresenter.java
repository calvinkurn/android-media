package com.tokopedia.referral.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.R;
import com.tokopedia.referral.ReferralActivity;
import com.tokopedia.referral.interfaces.ReferralRouter;
import com.tokopedia.referral.listener.FriendsWelcomeView;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;

/**
 * Created by ashwanityagi on 04/12/17.
 */

public class ReferralFriendsWelcomePresenter implements IReferralFriendsWelcomePresenter {

    private FriendsWelcomeView view;
    private final String CODE_KEY = "code";
    private UserSession userSession;
    private RemoteConfig remoteConfig;

    public ReferralFriendsWelcomePresenter(FriendsWelcomeView view) {
        this.view = view;
        userSession = new UserSession(view.getActivity());
    }

    @Override
    public void initialize() {
        remoteConfig = new FirebaseRemoteConfigImpl(view.getActivity());
        if (view.getActivity().getIntent() != null && view.getActivity().getIntent().getExtras() != null) {
            String code = view.getActivity().getIntent().getExtras().getString(CODE_KEY);
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), Constants.Values.Companion.REFERRAL);
            if (code == null || code.equalsIgnoreCase(localCacheHandler.getString(Constants.Key.Companion.REFERRAL_CODE, ""))) {
                if (userSession.isLoggedIn()) {
                    view.getActivity().startActivity(ReferralActivity.getCallingIntent(view.getActivity()));
                }
                view.closeView();
            }
            ((ReferralRouter)view.getActivity().getApplicationContext()).setBranchReferralCode(code);
        }

    }

    public String getHowItWorks() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_TEXT_WELCOME, view.getActivity().getString(R.string.cashback_enter_tokocash));
    }

    @Override
    public String getSubHeaderFromFirebase() {

        return remoteConfig.getString(RemoteConfigKey.REFERRAL_WELCOME_MESSAGE, view.getActivity().getString(R.string.referral_welcome_desc));
    }

    @Override
    public void copyVoucherCode(String code) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) view.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(view.getActivity().getString(R.string.copy_coupon_code_text), code);
        clipboard.setPrimaryClip(clip);
        if (TextUtils.isEmpty(code)) {
            view.showToastMessage(view.getActivity().getString(R.string.no_coupon_to_copy_text));
        } else {
            view.showToastMessage(view.getActivity().getString(R.string.copy_coupon_code_text) + " " + code);
        }
        ((ReferralRouter)view.getActivity().getApplicationContext()).eventReferralAndShare(view.getActivity(),
                Constants.Action.Companion.CLICK_COPY_REFERRAL_CODE, code);
    }

    public String getHelpButtonContentTitle() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_CONTENT_TITLE, view.getActivity().getString(R.string.acquisition_referral));
    }


    public String getHelpButtonContentSubtitle() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_CONTENT_SUBTITLE, view.getActivity().getString(R.string.what_is_referral_tokocash));
    }

    public boolean isShowReferralHelpLink() {
        return remoteConfig.getBoolean(RemoteConfigKey.SHOW_REFERRAL_HELP_LINK, false);
    }
}