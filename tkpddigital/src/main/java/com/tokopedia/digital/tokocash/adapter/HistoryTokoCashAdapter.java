package com.tokopedia.digital.tokocash.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.ItemHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/24/17.
 * Contains 3 View Holder : Item, Loading, & empty state for the last item
 */

public class HistoryTokoCashAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_EMPTY = 2;

    private List<ItemHistory> itemHistoryList;
    private Context context;
    private ItemHistoryListener listener;
    private boolean nextUriAvailable;

    public HistoryTokoCashAdapter(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList = itemHistoryList;
    }

    public void setListener(ItemHistoryListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_list_history_tokocash, parent, false);
            return new ItemViewHolderHistory(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_loading_view, parent, false);
            return new ItemLoadingViewHolder(view);
        } else if (viewType == VIEW_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_empty_loading_history, parent, false);
            return new EmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolderHistory) {
            renderItemView((ItemViewHolderHistory) holder, itemHistoryList.get(position));
        } else if (holder instanceof ItemLoadingViewHolder) {
            ItemLoadingViewHolder loadingViewHolder = (ItemLoadingViewHolder) holder;
            loadingViewHolder.progressBarHistory.setVisibility(View.VISIBLE);
        } else if (holder instanceof EmptyViewHolder) {
        }
    }

    private void renderItemView(ItemViewHolderHistory itemViewHolder, final ItemHistory itemHistory) {
        ImageHandler.loadImageThumbs(context, itemViewHolder.iconItem, itemHistory.getUrlImage());
        itemViewHolder.descItem.setText(!TextUtils.isEmpty(itemHistory.getNotes()) ? itemHistory.getNotes() :
                itemHistory.getDescription());
        itemViewHolder.titleItem.setText(itemHistory.getTitle());
        itemViewHolder.priceItem.setText(itemHistory.getAmountChanges());
        itemViewHolder.priceItem.setTextColor(ContextCompat.getColor(context,
                itemHistory.getAmountChangesSymbol().equals("+") ? R.color.green_500 : R.color.red_500));
        itemViewHolder.dateItem.setText(itemHistory.getTransactionInfoDate());
        itemViewHolder.layoutItemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickItemHistory(itemHistory);
            }
        });
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

    public void isNextUri(boolean nextUri) {
        this.nextUriAvailable = nextUri;
    }

    @Override
    public int getItemCount() {
        return itemHistoryList == null || itemHistoryList.size() == 0 ? 0 : itemHistoryList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            if (!nextUriAvailable)
                return VIEW_EMPTY;
            else
                return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    static class ItemViewHolderHistory extends RecyclerView.ViewHolder {

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
    }

    static class ItemLoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.progress_bar_history)
        ProgressBar progressBarHistory;

        public ItemLoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ItemHistoryListener {

        void onClickItemHistory(ItemHistory itemHistory);
    }
}