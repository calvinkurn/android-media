package com.tokopedia.core.referral.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.manage.general.ManageWebViewActivity;
import com.tokopedia.core.var.TkpdUrl;

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

    public ReferralGuidePagerAdapter(Activity context, ReferralGuidePagerListener listener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
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
            TextView tvHelpReceive = layout.findViewById(R.id.tv_help_receive);
            tvHelpReceive.setText(Html.fromHtml(context.getString(R.string.referral_help_receive)));

            btnShare.setOnClickListener(view1 -> {
                listener.onShareClick();
            });

            tvHelpLink.setOnClickListener(view1 -> {
                UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_HOW_IT_WORKS, "");
                context.startActivity(ManageWebViewActivity.getCallingIntent(context, TkpdUrl.REFERRAL_URL, ((AppCompatActivity) context).getSupportActionBar().getTitle().toString()));
            });

        } else {
            WebView tnc = layout.findViewById(R.id.webview_referral_terms);
            tnc.loadDataWithBaseURL(null, context.getResources().getString(R.string.referral_tnc), "text/html", "utf-8", null);
        }
        view.addView(layout);
        return layout;
    }

    public interface ReferralGuidePagerListener {
        void onShareClick();
    }
}
