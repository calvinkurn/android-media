package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 7/18/16.
 */
public class TickerAdapter extends RecyclerView.Adapter<TickerAdapter.ViewHolder> {

    private ArrayList<Ticker.Tickers> tickers;
    private TickerAdapter.OnTickerClosed onTickerClosed;
    Context context;

    public static final String PLAYSTORE_STRING = "play.google.com/store/apps";
    public static final String URL = "url";

    public static TickerAdapter createInstance(Context context, TickerAdapter.OnTickerClosed onTickerClosed) {
        return new TickerAdapter(context, onTickerClosed);
    }

    TickerAdapter(Context context, TickerAdapter.OnTickerClosed onTickerClosed) {
        this.context = context;
        this.tickers = new ArrayList<>();
        this.onTickerClosed = onTickerClosed;
    }

    public void addItem(ArrayList<Ticker.Tickers> tickers) {
        this.tickers = tickers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.announcement_ticker_container)
        RelativeLayout announcementContainer;

        @BindView(R.id.ticker_message)
        TextView message;

        @BindView(R.id.btn_close)
        ImageView btnClose;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ticker, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.message.setText(tickers.get(position).getMessage());
        holder.message.setMovementMethod(new SelectableSpannedMovementMethod());

        Spannable sp = (Spannable)holder.message.getText();
        URLSpan[] urls=sp.getSpans(0, holder.message.getText().length(), URLSpan.class);
        SpannableStringBuilder style=new SpannableStringBuilder(holder.message.getText());
        style.clearSpans();
        for(final URLSpan url : urls){
            style.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (url.getURL().contains(PLAYSTORE_STRING)) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.getURL())));
                    } else {
                        Intent intent = new Intent(context, BannerWebView.class);
                        intent.putExtra(URL, url.getURL());
                        context.startActivity(intent);
                    }
                }
            }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.message.setText(style);

        Drawable background = holder.announcementContainer.getBackground();
        try {
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(Color.parseColor(tickers.get(position).getColor().replaceAll("\\s","")));
        } catch (Exception e) {

        }

        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTickerClosed.onItemClicked();
                tickers.clear();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickers.size();
    }

    public interface OnTickerClosed {
        void onItemClicked();
    }

}
