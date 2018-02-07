package com.tokopedia.digital.tokocash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.ItemHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        @BindView(R2.id.icon_item_history)
        ImageView iconItem;
        @BindView(R2.id.title_item_history)
        TextView titleItem;
        @BindView(R2.id.price_item_history)
        TextView priceItem;
        @BindView(R2.id.desc_item_history)
        TextView descItem;
        @BindView(R2.id.date_item_history)
        TextView dateItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addItem(List<ItemHistory> historyList) {
        this.historyList.clear();
        this.historyList.addAll(historyList);
        notifyDataSetChanged();
    }
}
