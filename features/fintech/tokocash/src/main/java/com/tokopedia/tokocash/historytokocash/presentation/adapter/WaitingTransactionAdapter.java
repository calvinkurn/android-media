package com.tokopedia.tokocash.historytokocash.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;

import java.util.List;

/**
 * Created by nabillasabbaha on 11/28/17.
 */

public class WaitingTransactionAdapter extends RecyclerView.Adapter {

    private List<ItemHistory> historyList;
    private Context context;

    public WaitingTransactionAdapter(List<ItemHistory> historyList) {
        this.historyList = historyList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_history_tokocash, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ItemHistory itemHistory = historyList.get(position);

        ImageHandler.loadImageThumbs(context, itemViewHolder.iconItem, itemHistory.getUrlImage());
        itemViewHolder.descItem.setText(!TextUtils.isEmpty(itemHistory.getNotes()) ? itemHistory.getNotes() :
                itemHistory.getDescription());
        itemViewHolder.titleItem.setText(itemHistory.getTitle());
        itemViewHolder.priceItem.setText("+ " + itemHistory.getAmountPending());
        itemViewHolder.dateItem.setText(itemHistory.getTransactionInfoId() + " - " +
                itemHistory.getTransactionInfoDate());
    }

    @Override
    public int getItemCount() {
        return this.historyList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconItem;
        private TextView titleItem;
        private TextView priceItem;
        private TextView descItem;
        private TextView dateItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iconItem = itemView.findViewById(R.id.icon_item_history);
            titleItem = itemView.findViewById(R.id.title_item_history);
            priceItem = itemView.findViewById(R.id.price_item_history);
            descItem = itemView.findViewById(R.id.desc_item_history);
            dateItem = itemView.findViewById(R.id.date_item_history);
        }
    }

    public void addItem(List<ItemHistory> historyList) {
        this.historyList.clear();
        this.historyList.addAll(historyList);
        notifyDataSetChanged();
    }
}
