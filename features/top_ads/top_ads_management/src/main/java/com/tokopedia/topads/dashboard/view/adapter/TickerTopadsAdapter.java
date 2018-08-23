package com.tokopedia.topads.dashboard.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topads.R;

import java.util.ArrayList;
import java.util.List;

public class TickerTopadsAdapter extends RecyclerView.Adapter<TickerTopadsAdapter.TickerTopadsViewHolder> {

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
        return listMessageTicker.size();
    }

    class TickerTopadsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMessage;

        public TickerTopadsViewHolder(View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.text_message_ticker);
        }

        public void bind(String message) {
            textViewMessage.setText(MethodChecker.fromHtml(message));
        }
    }
}
