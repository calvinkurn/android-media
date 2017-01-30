package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.tkpd.R;
import com.tokopedia.core.home.BannerWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 7/18/16.
 */
public class TickerAnnouncementAdapter extends RecyclerView.Adapter<TickerAnnouncementAdapter.ViewHolder> {

    private Ticker.Tickers[] tickers;
    Context context;

    public static final int TICKER_WARNING = 1;
    public static final int TICKER_ANNOUNCEMENT = 2;

    public static TickerAnnouncementAdapter createInstance(Context context) {
        return new TickerAnnouncementAdapter(context);
    }

    TickerAnnouncementAdapter(Context context) {
        this.context = context;
        this.tickers = new Ticker.Tickers[]{};
    }

    public void addItem(Ticker.Tickers[] tickers) {
        this.tickers = tickers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.announcement_ticker_container)
        RelativeLayout announcementContainer;

        @BindView(R.id.ticker_title)
        TextView title;

        @BindView(R.id.ticker_message)
        TextView message;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TICKER_WARNING: return new ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ticker_warning, parent, false));
            default:
            case TICKER_ANNOUNCEMENT: return new ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ticker_announcement, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (tickers[position].getTitle() != null && tickers[position].getTitle().length() == 0) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(tickers[position].getTitle());
        }
        holder.message.setText(tickers[position].getMessage());
        holder.message.setMovementMethod(new SelectableSpannedMovementMethod());

        Spannable sp = (Spannable)holder.message.getText();
        URLSpan[] urls=sp.getSpans(0, holder.message.getText().length(), URLSpan.class);
        SpannableStringBuilder style=new SpannableStringBuilder(holder.message.getText());
        style.clearSpans();
        for(final URLSpan url : urls){
            style.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(context, BannerWebView.class);
                    intent.putExtra("url", url.getURL());
                    context.startActivity(intent);
                }
            }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.message.setText(style);
    }

    @Override
    public int getItemCount() {
        return tickers.length;
    }

    @Override
    public int getItemViewType(int position) {
        return tickers[position].getTickerType().isEmpty()? TICKER_ANNOUNCEMENT:
                Integer.parseInt(tickers[position].getTickerType());
    }
}
