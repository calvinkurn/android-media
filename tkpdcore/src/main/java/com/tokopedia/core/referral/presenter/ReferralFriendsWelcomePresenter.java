package com.tokopedia.core.referral.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.referral.listner.FriendsWelcomeView;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import java.net.URLDecoder;

/**
 * Created by ashwanityagi on 04/12/17.
 */

public class ReferralFriendsWelcomePresenter implements IReferralFriendsWelcomePresenter {

    private FriendsWelcomeView view;
    private String owner = "";
    private final String CODE_KEY = "code";
    private final String OWNER_KEY = "owner";

    public ReferralFriendsWelcomePresenter(FriendsWelcomeView view) {
        this.view = view;

    }

    @Override
    public void initialize() {
        if (view.getActivity().getIntent() != null && view.getActivity().getIntent().getExtras() != null) {
            String code = view.getActivity().getIntent().getExtras().getString(CODE_KEY);
            owner = view.getActivity().getIntent().getExtras().getString(OWNER_KEY);

            LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), TkpdCache.REFERRAL);
            if (code == null || code.equalsIgnoreCase(localCacheHandler.getString(TkpdCache.Key.REFERRAL_CODE, ""))) {
                view.closeView();
            }

            view.renderReferralCode(code);
        }
    }

    @Override
    public void copyVoucherCode(String voucherCode) {
        ClipboardManager clipboard = (ClipboardManager)
                view.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                view.getActivity().getString(R.string.copied_to_clipboard), voucherCode
        );
        clipboard.setPrimaryClip(clip);
        if(TextUtils.isEmpty(voucherCode)){
            view.showToastMessage(view.getActivity().getString(R.string.no_coupon_to_copy_text));
        }else{
            view.showToastMessage(view.getActivity().getString(R.string.copied_to_clipboard) + " " + voucherCode);
        }

        UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_COPY_REFERRAL_CODE, voucherCode);

    }

    @Override
    public String getReferralWelcomeMsg() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(view.getActivity());
        String welcomeMessage = remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_SHARE_WELCOME_MESSAGE);
        String username = SessionHandler.getLoginName(view.getActivity());
        username = username == null ? "" : " " + username;
        try {
            owner = URLDecoder.decode(owner, "UTF-8");// here is double encoding characters that's why i am decoding it twice.
            owner = URLDecoder.decode(owner, "UTF-8");

            //  welcomeMessage = String.format("%s , %s",username,owner); it throws Exception
            welcomeMessage = welcomeMessage.replaceFirst("%s", username);
            welcomeMessage = welcomeMessage.replaceFirst("%s", owner);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return welcomeMessage;

    }

    public String getHowItWorks() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(view.getActivity());
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFERRAL_HOWITWORKS, view.getActivity().getString(R.string.title_app_referral_howitworks));
    }

}