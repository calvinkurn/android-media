package com.tokopedia.digital.tokocash.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.ItemHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/24/17.
 */

public class HistoryTokoCashAdapter extends RecyclerView.Adapter {

    private List<ItemHistory> itemHistoryList;

    private Context context;

    private ItemHistoryListener listener;

    public HistoryTokoCashAdapter(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList = itemHistoryList;
    }

    public void setListener(ItemHistoryListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_history_tokocash, parent, false);
        return new ItemViewHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolderHistory) holder).bindView(itemHistoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemHistoryList.size();
    }

    public void addItemHistoryList(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList.clear();
        this.itemHistoryList.addAll(itemHistoryList);
        notifyDataSetChanged();
    }

    public void addItemHistoryListLoadMore(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList.addAll(itemHistoryList);
        notifyDataSetChanged();
    }

    class ItemViewHolderHistory extends RecyclerView.ViewHolder {

        @BindView(R2.id.layout_item_history)
        RelativeLayout layoutItemHistory;
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

        public ItemViewHolderHistory(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final ItemHistory itemHistory) {
            if (itemHistory.getUrlImage() != null) {
                Glide.with(context)
                        .load(itemHistory.getUrlImage())
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_loading_toped))
                        .into(iconItem);
            }
            descItem.setText(!TextUtils.isEmpty(itemHistory.getNotes()) ? itemHistory.getNotes() :
                    itemHistory.getDescription());
            titleItem.setText(itemHistory.getTitle());
            priceItem.setText(itemHistory.getAmountChanges());
            priceItem.setTextColor(ContextCompat.getColor(context,
                    itemHistory.getAmountChangesSymbol().equals("+") ? R.color.green_500 : R.color.red_500));
            dateItem.setText(itemHistory.getTransactionInfoDate());
            layoutItemHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickItemHistory(itemHistory);
                }
            });
        }
    }

    public interface ItemHistoryListener {
        void onClickItemHistory(ItemHistory itemHistory);
    }
}
