package com.tokopedia.core.referral.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.remote_config.FirebaseRemoteConfigImpl;
import com.tokopedia.remote_config.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.design.bottomsheet.BottomSheetView;

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

            btnShare.setOnClickListener(view1 -> {
                listener.onShareClick();
                UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_SHARE_TEMAN, "");
            });

            tvHelpLink.setOnClickListener(view1 -> {
                UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_WHAT_IS_TOKOCASH, "");
                showOnBoardingTooltip(context.getString(R.string.acquisition_referral) ,context.getString(R.string.what_is_referral_tokocash) );
            });

        } else {
            WebView tnc = layout.findViewById(R.id.webview_referral_terms);
            tnc.loadDataWithBaseURL(null, getReferralTerms(), "text/html", "utf-8", null);
        }
        view.addView(layout);
        return layout;
    }

    public String getReferralTerms() {
            return remoteConfig.getString(TkpdCache.RemoteConfigKey.REFFERAL_TERMS, context.getString(R.string.referral_tnc));
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
