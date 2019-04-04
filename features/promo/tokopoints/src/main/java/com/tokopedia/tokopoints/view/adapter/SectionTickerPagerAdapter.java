package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.view.model.TickerContainer;
import com.tokopedia.tokopoints.view.model.TickerMetadata;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.SimpleSpanBuilder;

import java.util.List;

public class SectionTickerPagerAdapter extends PagerAdapter {
    private List<TickerContainer> mItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public SectionTickerPagerAdapter(Context context, List<TickerContainer> items) {
        this.mContext = context;
        this.mItems = items;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        TextView item = (TextView) LayoutInflater.from(view.getContext()).inflate(R.layout.tp_layout_section_ticker_new, view, false);
        item.setMovementMethod(LinkMovementMethod.getInstance());
        item.setText(generateText(view.getContext(), mItems.get(position)));
        view.addView(item, 0);
        return item;
    }

    private SpannableStringBuilder generateText(@NonNull Context context, @NonNull TickerContainer container) {
        SimpleSpanBuilder ssb = new SimpleSpanBuilder();

        if (container.getMetadata() == null || container.getMetadata().isEmpty()) {
            return ssb.build();
        }

        for (TickerMetadata each : container.getMetadata()) {
            if (URLUtil.isValidUrl(each.getLink().get(CommonConstant.TickerMapKeys.APP_LINK))
                    || URLUtil.isValidUrl(each.getLink().get(CommonConstant.TickerMapKeys.URL))) {
                String linkContent = each.getText().get(CommonConstant.TickerMapKeys.CONTENT);
                ssb.append(linkContent, new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        String uri = each.getLink().get(CommonConstant.TickerMapKeys.APP_LINK).isEmpty() ? each.getLink().get(CommonConstant.TickerMapKeys.URL)
                                : each.getLink().get(CommonConstant.TickerMapKeys.APP_LINK);
                        if (uri.startsWith(CommonConstant.TickerMapKeys.TOKOPEDIA)) {
                            RouteManager.route(context, uri);
                        } else {
                            ((TokopointRouter) mContext.getApplicationContext()).openTokoPoint(mContext, uri);
                        }

                        AnalyticsTrackerUtil.sendEvent(mContext,
                                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                                "click " + linkContent + " on ticker",
                                "");
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(context.getResources().getColor(R.color.tkpd_main_green));
                    }
                });
            } else {
                ssb.appendWithSpace(each.getText().get(CommonConstant.TickerMapKeys.CONTENT));
            }
        }

        return ssb.build();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
