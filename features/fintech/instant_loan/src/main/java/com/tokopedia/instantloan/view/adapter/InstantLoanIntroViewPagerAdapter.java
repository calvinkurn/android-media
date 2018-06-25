package com.tokopedia.instantloan.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.view.activity.InstantLoanActivity;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;

//import static com.tokopedia.core.network.constants.TkpdBaseURL.InstantLoan.WEB_LINK_TNC;

/**
 * View pager adapter
 */
public class InstantLoanIntroViewPagerAdapter extends PagerAdapter {
    private static final String WEB_LINK_TNC = "https://www.tokopedia.com/bantuan/syarat-dan-ketentuan-pinjaman-dana-tunai/";
    private static final String WEB_LINK_LEARN_MORE = "https://www.tokopedia.com/pinjaman-online/profil-kredit/";
    private LayoutInflater mLayoutInflater;
    private int[] mLayouts;
    private InstantLoanPresenter mPresenter;
    private InstantLoanActivity mActivity;

    public InstantLoanIntroViewPagerAdapter(InstantLoanActivity activity, int[] layouts, InstantLoanPresenter presenter) {
        this.mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayouts = layouts;
        this.mPresenter = presenter;
        this.mActivity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(mLayouts[position], container, false);

        if (position == mLayouts.length - 2) {

            TextView textView = view.findViewById(R.id.text_label_processing_time);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    mActivity.openWebView(WEB_LINK_LEARN_MORE);
//                    Toast.makeText(mActivity, "Click",Toast.LENGTH_SHORT).show();
                }
            };

            SpannableString spannableString = new SpannableString(textView.getText().toString());

            spannableString.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.tkpd_main_green)),
                    mActivity.getResources().getString(R.string.text_intro_slide_2).length(),
                    textView.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableString.setSpan(clickableSpan,
                    mActivity.getResources().getString(R.string.text_intro_slide_2).length(),
                    textView.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(spannableString);

            textView.setMovementMethod(LinkMovementMethod.getInstance());


        } else if (position == mLayouts.length - 1) {
            //TODO @lavekush make spanable textview for TnC
            TextView textTnC = view.findViewById(R.id.text_tnc);
            String string = mActivity.getString(R.string.label_tnc, mActivity.getResources().getColor(R.color.tkpd_main_green));
            textTnC.setText(Html.fromHtml(string));

            view.findViewById(R.id.button_connect_device).setOnClickListener(v -> mPresenter.startDataCollection());

            textTnC.setOnClickListener(v -> mActivity.openWebView(WEB_LINK_TNC));
        }
        view.setTag(position);
        container.addView(view);

        return view;
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