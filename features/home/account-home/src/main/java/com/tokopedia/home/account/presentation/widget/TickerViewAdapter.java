package com.tokopedia.home.account.presentation.widget;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.content.Context;
import android.os.Build;
import androidx.viewpager.widget.PagerAdapter;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.home.account.R;

import java.util.ArrayList;

/**
 * Created by hangnadi on 8/16/17.
 */

@SuppressWarnings("WeakerAccess")
class TickerViewAdapter extends PagerAdapter {

    private ArrayList<String> listMessage;
    private int backgroundColor;
    private ArrayList<Integer> listTextColor;
    private int defaultLinkColor;
    private float textSize;
    private TickerView.OnPartialTextClickListener listener;
    private boolean isUnderlinedLink = true;

    private View tickerBackground;
    private TextView tickerMessage;
    private Context context;

    public TickerViewAdapter(ArrayList<Integer> listTextColor,
                             int backgroundColor,
                             int defaultLinkColor,
                             float textSize,
                             ArrayList<String> listMessage,
                             TickerView.OnPartialTextClickListener listener) {
        this.listTextColor = listTextColor;
        this.backgroundColor = backgroundColor;
        this.defaultLinkColor = defaultLinkColor;
        this.textSize = textSize;
        this.listMessage = listMessage;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_ticker, container, false);
        context = view.getContext();

        tickerBackground = view.findViewById(R.id.ticker_background);
        tickerMessage = (TextView) view.findViewById(R.id.ticker_message);
        tickerMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        tickerBackground.setBackgroundColor(backgroundColor);
        tickerMessage.setTextColor(listTextColor.get(position));
        tickerMessage.setLinkTextColor(defaultLinkColor);
        tickerMessage.setMovementMethod(new SelectableSpannedMovementMethod());

        Spannable sp = (Spannable) fromHtml(listMessage.get(position));
        URLSpan[] urls = sp.getSpans(0, sp.length(), URLSpan.class);
        SpannableStringBuilder style = new SpannableStringBuilder(fromHtml(listMessage.get(position)));
        style.clearSpans();

        for(URLSpan url : urls){
            final String messageClickAble = url.getURL();
            style.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onClick(widget, messageClickAble);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isUnderlinedLink);
                }
            }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tickerMessage.setText(style);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(layoutParams);
        view.invalidate();
        view.requestLayout();

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return listMessage.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null && object instanceof View) container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void setListMessage(ArrayList<String> listMessage) {
        this.listMessage = listMessage;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setListTextColor(ArrayList<Integer> listTextColor) {
        this.listTextColor = listTextColor;
    }

    public void setDefaultLinkColor(int defaultLinkColor) {
        this.defaultLinkColor = defaultLinkColor;
    }

    public void setListener(TickerView.OnPartialTextClickListener listener) {
        this.listener = listener;
    }

    public void setPadding(int top, int right, int bottom, int left) {
        tickerBackground.setPadding(left, top, right, bottom);
    }

    public void setTextAppearance(int appearance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tickerMessage.setTextAppearance(appearance);
        } else {
            tickerMessage.setTextAppearance(context, appearance);
        }
    }

    @SuppressWarnings("deprecation")
    private Spanned fromHtml(String text) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }

    public void setIsUnderlinedLink(boolean isUnderlinedLink) {
        this.isUnderlinedLink = isUnderlinedLink;
    }
}
