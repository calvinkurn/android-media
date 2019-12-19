package com.tokopedia.topads.dashboard.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.text.style.WebViewURLSpan;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.TopAdsWebViewActivity;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class TickerTopadsAdapter extends RecyclerView.Adapter<TickerTopadsAdapter.TickerTopadsViewHolder> {

    public static final int MAX_SHOW_ITEM = 1;
    List<String> listMessageTicker;

    public TickerTopadsAdapter() {
        this.listMessageTicker = new ArrayList<>();
    }

    @NonNull
    @Override
    public TickerTopadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TickerTopadsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticker_topads, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TickerTopadsViewHolder holder, int position) {
        if(listMessageTicker != null) {
            holder.bind(listMessageTicker.get(position));
        }
    }

    public void setListMessageTicker(List<String> listMessageTicker) {
        this.listMessageTicker = listMessageTicker;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(listMessageTicker.size() > 1){
            return MAX_SHOW_ITEM;
        }
        return listMessageTicker.size();
    }

    class TickerTopadsViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;

        public TickerTopadsViewHolder(View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.text_message_ticker);
        }

        public void bind(String message) {

            try {
                textViewMessage.setClickable(true);
                textViewMessage.setMovementMethod (LinkMovementMethod.getInstance());
                Spannable spannable = (Spannable) MethodChecker.fromHtml(URLDecoder.decode(message, "UTF-8"));
                for (URLSpan u: spannable.getSpans(0, spannable.length(), URLSpan.class)) {
                    int start = spannable.getSpanStart(u);
                    int end = spannable.getSpanEnd(u);
                    spannable.removeSpan(u);
                    WebViewURLSpan webViewSpan = new WebViewURLSpan(u.getURL());
                    webViewSpan.setListener(new WebViewURLSpan.OnClickListener() {
                        @Override
                        public void onClick(@NotNull String url) {
                            itemView.getContext().startActivity(TopAdsWebViewActivity.createIntent(itemView.getContext(), url));
                        }

                        @Override
                        public boolean showUnderline() {
                            return false;
                        }
                    });

                    spannable.setSpan(webViewSpan, start, end, 0);
                }
                textViewMessage.setText(spannable);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
