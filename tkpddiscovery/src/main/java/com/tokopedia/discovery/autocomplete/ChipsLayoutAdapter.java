package com.tokopedia.discovery.autocomplete;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

class ChipsLayoutAdapter extends RecyclerView.Adapter<ChipsLayoutAdapter.DefaultChipsViewHolder> {

//    @LayoutRes
//    private static final int LAYOUT_CHIPS_DELETE_ABLE = R.layout.layout_autocomplete_chips_delete_able;

    @LayoutRes
    private static final int LAYOUT_DEFAULT_CHIPS = R.layout.layout_autocomplete_chips_delete_able;

    private final ItemClickListener clickListener;
    private SearchData element;

    public ChipsLayoutAdapter(ItemClickListener mClickListener) {
        this.clickListener = mClickListener;
        this.element = new SearchData();
    }

    @Override
    public DefaultChipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(LAYOUT_DEFAULT_CHIPS, parent, false);
        return new DefaultChipsViewHolder(itemLayoutView, clickListener);
    }

    @Override
    public void onBindViewHolder(DefaultChipsViewHolder holder, int position) {
        SearchItem searchItem = element.getItems().get(position);
        searchItem.setEventAction(element.getId());
        holder.bind(searchItem);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return LAYOUT_DEFAULT_CHIPS;
//        if (TextUtils.equals(
//                element.getId(),
//                "recent_search"
//        )) {
//            return LAYOUT_CHIPS_DELETE_ABLE;
//        } else {
//            return LAYOUT_DEFAULT_CHIPS;
//        }
//    }

    @Override
    public int getItemCount() {
        return element.getItems().size();
    }

    public void setModel(SearchData element) {
        this.element = element;
    }

    public static class DefaultChipsViewHolder extends RecyclerView.ViewHolder {

        private final ItemClickListener clickListener;
        TextView textView;

        public DefaultChipsViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            textView = itemView.findViewById(R.id.autocomplete_chips_item);
        }

        public void bind(final SearchItem item) {
            textView.setText(item.getKeyword());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(item);
                }
            });
        }
    }
}
