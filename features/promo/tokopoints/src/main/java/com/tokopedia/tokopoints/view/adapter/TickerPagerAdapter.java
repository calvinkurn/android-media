package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.TickerContainer;
import com.tokopedia.tokopoints.view.model.TickerMetadata;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;
import com.tokopedia.tokopoints.view.util.SimpleSpanBuilder;

import java.util.List;

public class TickerPagerAdapter extends PagerAdapter {
    private List<TickerContainer> mItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public TickerPagerAdapter(Context context, List<TickerContainer> items) {
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
        TextView item = (TextView) mInflater.inflate(R.layout.tp_layout_ticker_item, view, false);
        item.setMovementMethod(LinkMovementMethod.getInstance());
        item.setText(generateText(view.getContext(), mItems.get(position)));
        view.addView(item);

        return item;
    }

    private SpannableStringBuilder generateText(@NonNull Context context, @NonNull TickerContainer container) {
        SimpleSpanBuilder ssb = new SimpleSpanBuilder();

        if (container.getMetadata() == null || container.getMetadata().isEmpty()) {
            return ssb.build();
        }

        for (TickerMetadata each : container.getMetadata()) {
            if (URLUtil.isValidUrl(each.getLink().get("applink"))
                    || URLUtil.isValidUrl(each.getLink().get("url"))) {
                String linkContent = each.getText().get("content");
                ssb.append(linkContent, new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        String uri = each.getLink().get("applink").isEmpty() ? each.getLink().get("url") : each.getLink().get("applink");
                        if (uri.startsWith("tokopedia")) {
                            RouteManager.route(context, uri);
                        } else {
                            ((TokopointRouter) mContext.getApplicationContext()).openTokoPoint(mContext, uri);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(context.getResources().getColor(R.color.tkpd_main_green));
                    }
                });
            } else {
                ssb.appendWithSpace(each.getText().get("content"));
            }
        }

        return ssb.build();
    }
}
