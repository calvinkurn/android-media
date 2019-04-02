package com.tokopedia.referral.view.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tokopedia.referral.Constants;
import com.tokopedia.referral.R;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * Created by ashwanityagi on 02/05/18.
 */

public class ReferralGuidePagerAdapter extends PagerAdapter {

    private int mLayouts[] = new int[]{R.layout.referral_how_it_works, R.layout.referral_terms};
    private LayoutInflater mInflater;
    private final int HOW_TO_USE_POSITION = 0;
    private final int TERMS_POSITION = 1;
    private Activity context;
    private ReferralGuidePagerListener listener;
    private RemoteConfig remoteConfig;

    public ReferralGuidePagerAdapter(Activity context, ReferralGuidePagerListener listener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return mLayouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View layout = mInflater.inflate(mLayouts[position], view, false);
        if (position == HOW_TO_USE_POSITION) {
            TextView btnShare = layout.findViewById(R.id.btn_app_share);
            TextView tvHelpLink = layout.findViewById(R.id.tv_referral_help_link);


            tvHelpLink.setVisibility(isShowReferralHelpLink() ? View.VISIBLE : View.GONE);
            tvHelpLink.setText(getHelpButtonText());
            btnShare.setOnClickListener(view1 -> {
                listener.onShareClick();

                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        Constants.EventLabel.Companion.CLICK_APP_SHARE_REFERRAL,
                        Constants.Category.Companion.REFERRAL,
                        Constants.Action.Companion.CLICK_SHARE_TEMAN, ""));
            });

            tvHelpLink.setOnClickListener(view1 -> {
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        Constants.EventLabel.Companion.CLICK_APP_SHARE_REFERRAL,
                        Constants.Category.Companion.REFERRAL,
                        Constants.Action.Companion.CLICK_WHAT_IS_TOKOCASH, ""));
                showOnBoardingTooltip(getHelpButtonContentTitle(), getHelpButtonContentSubtitle() );
            });

        } else {
            WebView tnc = layout.findViewById(R.id.webview_referral_terms);
            tnc.loadDataWithBaseURL(null, getReferralTerms(), "text/html", "utf-8", null);
        }
        view.addView(layout);
        return layout;
    }

    private String getHelpButtonText() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_TEXT, context.getString(R.string.apa_itu_tokocash));
    }

    private String getHelpButtonContentTitle() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_CONTENT_TITLE, context.getString(R.string.acquisition_referral));
    }

    private String getHelpButtonContentSubtitle() {
        return remoteConfig.getString(RemoteConfigKey.REFERRAL_HELP_LINK_CONTENT_SUBTITLE, context.getString(R.string.what_is_referral_tokocash));
    }

    public String getReferralTerms() {
            return remoteConfig.getString(RemoteConfigKey.REFFERAL_TERMS, context.getString(R.string.referral_tnc));
    }

    public boolean isShowReferralHelpLink() {
        return remoteConfig.getBoolean(RemoteConfigKey.SHOW_REFERRAL_HELP_LINK, false);
    }

    private void showOnBoardingTooltip(String title, String content) {
        BottomSheetView mToolTip = new BottomSheetView(context);
        mToolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(content)
                .build());

        mToolTip.show();
    }

    public interface ReferralGuidePagerListener {
        void onShareClick();
    }
}
