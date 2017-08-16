package com.tokopedia.design.ticker;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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

class TickerViewAdapter extends PagerAdapter {

    private final ArrayList<String> listMessage;
    private final ArrayList<Integer> listBackGroundColor;
    private final ArrayList<Integer> listTextColor;
    private final Context context;
    private final TickerView.OnPartialTextClickListener listener;

    public TickerViewAdapter(Context context,
                             ArrayList<Integer> listTextColor,
                             ArrayList<Integer> listBackGroundColor,
                             ArrayList<String> listMessage,
                             TickerView.OnPartialTextClickListener listener) {
        this.context = context;
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

        tickerMessage.setText(listMessage.get(position));
        tickerBackground.setBackgroundColor(
                ContextCompat.getColor(context, listBackGroundColor.get(position))
        );
        tickerMessage.setTextColor(
                ContextCompat.getColor(context, listTextColor.get(position))
        );

        tickerMessage.setMovementMethod(new SelectableSpannedMovementMethod());
        Spannable sp = (Spannable) tickerMessage.getText();
        URLSpan[] urls=sp.getSpans(0, tickerMessage.getText().length(), URLSpan.class);
        SpannableStringBuilder style = new SpannableStringBuilder(tickerMessage.getText());
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

}
