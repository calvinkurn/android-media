package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.util.CommonConstant;

public class CouponCatalogInfoPagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private String mInfo;
    private String mTnC;

    public CouponCatalogInfoPagerAdapter(Context context, String info, String tnC) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mInfo = info;
        this.mTnC = tnC;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {

            View view = mLayoutInflater.inflate(R.layout.tp_layout_info, container, false);
            WebView browser = view.findViewById(R.id.text_content);
            browser.getSettings().setJavaScriptEnabled(true);

            if (position == 0) {
                browser.loadData(getFormattedHtml(mTnC), "text/html", "UTF-8");
            } else {
                browser.loadData(getFormattedHtml(mInfo), "text/html", "UTF-8");
            }
            view.setTag(position);
            container.addView(view);

            return view;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getCount() {
        return CommonConstant.HOMEPAGE_TAB_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    private String getFormattedHtml(String content) {
        return "<html><head><style>li{ font-size: 10pt; color: 8A000000; }</style></head><body>" + content + "</body></html>";
    }
}
