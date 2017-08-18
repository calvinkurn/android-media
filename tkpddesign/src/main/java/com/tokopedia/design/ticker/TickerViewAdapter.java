package com.tokopedia.design.ticker;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.R;

import java.util.ArrayList;

/**
 * Created by hangnadi on 8/16/17.
 */

@SuppressWarnings("WeakerAccess")
class TickerViewAdapter extends PagerAdapter {

    private ArrayList<String> listMessage;
    private ArrayList<Integer> listBackGroundColor;
    private ArrayList<Integer> listTextColor;
    private TickerView.OnPartialTextClickListener listener;

    public TickerViewAdapter(ArrayList<Integer> listTextColor,
                             ArrayList<Integer> listBackGroundColor,
                             ArrayList<String> listMessage,
                             TickerView.OnPartialTextClickListener listener) {
        this.listTextColor = listTextColor;
        this.listBackGroundColor = listBackGroundColor;
        this.listMessage = listMessage;
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_ticker, container, false);
        View tickerBackground = view.findViewById(R.id.ticker_background);
        TextView tickerMessage = (TextView) view.findViewById(R.id.ticker_message);

        tickerBackground.setBackgroundColor(listBackGroundColor.get(position));
        tickerMessage.setTextColor(listTextColor.get(position));

        tickerMessage.setMovementMethod(new SelectableSpannedMovementMethod());

        Spannable sp = (Spannable) fromHtml(listMessage.get(position));
        URLSpan[] urls = sp.getSpans(0, fromHtml(listMessage.get(position)).length(), URLSpan.class);
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

    public void setListBackGroundColor(ArrayList<Integer> listBackGroundColor) {
        this.listBackGroundColor = listBackGroundColor;
    }

    public void setListTextColor(ArrayList<Integer> listTextColor) {
        this.listTextColor = listTextColor;
    }

    public void setListener(TickerView.OnPartialTextClickListener listener) {
        this.listener = listener;
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
}
