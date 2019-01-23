package com.tokopedia.referral.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.R;
import com.tokopedia.referral.view.activity.ReferralActivity;
import com.tokopedia.referral.ReferralRouter;
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

    private final String CODE_KEY = "code";
    private UserSession userSession;
    private RemoteConfig remoteConfig;

    @Inject
    public ReferralFriendsWelcomePresenter(UserSession userSession, RemoteConfig remoteConfig) {
        this.userSession = userSession;
        this.remoteConfig = remoteConfig;
    }

    @Override
    public void initialize() {
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
        String subHeaderMessage = remoteConfig.getString(RemoteConfigKey.REFERRAL_WELCOME_MESSAGE, getView().getActivity().getString(R.string.referral_welcome_desc));
        String user = "";
        if(userSession.isLoggedIn()) user = userSession.getName();
        String owner = getView().getActivity().getIntent().getExtras().getString(Constants.Key.OWNER);
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