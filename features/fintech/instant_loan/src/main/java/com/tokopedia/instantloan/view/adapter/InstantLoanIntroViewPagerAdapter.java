package com.tokopedia.instantloan.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics;
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants;
import com.tokopedia.instantloan.view.activity.InstantLoanActivity;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;

import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_LEARN_MORE;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_TNC;

/**
 * View pager adapter
 */
public class InstantLoanIntroViewPagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private int[] mLayouts;
    private InstantLoanPresenter mPresenter;
    private InstantLoanActivity mActivity;
    private final String link = "syarat dan ketentuan";
    InstantLoanAnalytics instantLoanAnalytics;

    public InstantLoanIntroViewPagerAdapter(InstantLoanActivity activity, int[] layouts, InstantLoanPresenter presenter) {
        this.mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayouts = layouts;
        this.mPresenter = presenter;
        this.mActivity = activity;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(mLayouts[position], container, false);

        instantLoanAnalytics = new InstantLoanAnalytics(container.getContext());

        if (position == mLayouts.length - 2) {

            TextView textView = view.findViewById(R.id.text_label_processing_time);

            ClickableSpan clickableSpan = new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mActivity.getResources().getColor(R.color.tkpd_green_header));
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View view) {
                    sendLoanPopupClickEvent(InstantLoanEventConstants.EventLabel.PL_POPUP_LEARN_MORE);
                    mActivity.openWebView(WEB_LINK_LEARN_MORE);
                }
            };

            SpannableString spannableString = new SpannableString(textView.getText().toString());

            spannableString.setSpan(clickableSpan,
                    mActivity.getResources().getString(R.string.text_intro_slide_2).length() + 1,
                    textView.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(spannableString);

            textView.setMovementMethod(LinkMovementMethod.getInstance());


        } else if (position == mLayouts.length - 1) {
            TextView textTnC = view.findViewById(R.id.text_tnc);
            int startIndexOfLink = textTnC.getText().toString().indexOf(link);

            SpannableString spannableString = new SpannableString(textTnC.getText().toString());

            ClickableSpan clickableSpan = new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mActivity.getResources().getColor(R.color.tkpd_green_header));
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View view) {
                    sendLoanPopupClickEvent(InstantLoanEventConstants.EventLabel.PL_POPUP_TNC);
                    mActivity.openWebView(WEB_LINK_TNC);
                }
            };

            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textTnC.setText(spannableString);
            textTnC.setMovementMethod(LinkMovementMethod.getInstance());
            view.findViewById(R.id.button_connect_device).setOnClickListener(v -> {
                sendLoanPopupClickEvent(InstantLoanEventConstants.EventLabel.PL_POPUP_CONNECT_DEVICE);
                mPresenter.startDataCollection();
            });

        }
        view.setTag(position);
        container.addView(view);

        return view;
    }

    private void sendLoanPopupClickEvent(String label) {
        if (instantLoanAnalytics == null) {
            return;
        }
        instantLoanAnalytics.eventLoanPopupClick(label);
    }

    @Override
    public int getCount() {
        return mLayouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}