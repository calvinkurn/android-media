package com.tokopedia.core.referral.presenter;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.referral.ReferralActivity;
import com.tokopedia.core.referral.listener.FriendsWelcomeView;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.remoteconfig.RemoteConfigKey;

/**
 * Created by ashwanityagi on 04/12/17.
 */

public class ReferralFriendsWelcomePresenter implements IReferralFriendsWelcomePresenter {

    private FriendsWelcomeView view;
    private final String CODE_KEY = "code";
    private SessionHandler sessionHandler;
    private RemoteConfig remoteConfig;

    public ReferralFriendsWelcomePresenter(FriendsWelcomeView view) {
        this.view = view;
        sessionHandler = new SessionHandler(view.getActivity());
    }

    @Override
    public void initialize() {
        remoteConfig = new FirebaseRemoteConfigImpl(view.getActivity());
        if (view.getActivity().getIntent() != null && view.getActivity().getIntent().getExtras() != null) {
            String code = view.getActivity().getIntent().getExtras().getString(CODE_KEY);
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), TkpdCache.REFERRAL);
            if (code == null || code.equalsIgnoreCase(localCacheHandler.getString(TkpdCache.Key.REFERRAL_CODE, ""))) {
                if (sessionHandler.isV4Login()) {
                    view.getActivity().startActivity(ReferralActivity.getCallingIntent(view.getActivity()));
                }
                view.closeView();
            }
            BranchSdkUtils.REFERRAL_ADVOCATE_PROMO_CODE = code;
        }

    }

    public String getHowItWorks() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_TEXT_WELCOME, view.getActivity().getString(R.string.cashback_enter_tokocash));
    }

    @Override
    public String getSubHeaderFromFirebase() {

        return remoteConfig.getString(RemoteConfigKey.REFERRAL_WELCOME_MESSAGE, view.getActivity().getString(R.string.referral_welcome_desc));
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