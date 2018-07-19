package com.tokopedia.tokopoints.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
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
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.TickerContainer;
import com.tokopedia.tokopoints.view.model.TickerMetadata;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;

import java.util.List;

public class TickerPagerAdapter extends PagerAdapter {
    private List<TickerContainer> mItems;
    private LayoutInflater mInflater;

    public TickerPagerAdapter(Context context, List<TickerContainer> items) {
        this.mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
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
        item.setText(generateText(view.getContext(), mItems.get(position)));
        view.addView(item);

        return item;
    }

    private CharSequence generateText(@NonNull Context context, @NonNull TickerContainer container) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (container.getMetadata() == null || container.getMetadata().isEmpty()) {
            return builder.subSequence(0, builder.length());
        }

        for (TickerMetadata each : container.getMetadata()) {
            if (URLUtil.isValidUrl(each.getLink().get("applink"))) {
                String linkContent = each.getText().get("content");
                SpannableString spannableString = new SpannableString(linkContent);
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        RouteManager.route(context, each.getLink().get("applink"));
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(context.getResources().getColor(R.color.tkpd_main_green));
                    }
                }, builder.length(), builder.length() + linkContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
            } else {
                builder.append(each.getText().get("content"));
            }
        }

        return builder.subSequence(0, builder.length());
    }
}
