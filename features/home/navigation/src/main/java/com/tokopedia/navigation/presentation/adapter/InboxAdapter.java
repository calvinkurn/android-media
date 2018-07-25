package com.tokopedia.navigation.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tokopedia.navigation.R;

import com.tokopedia.navigation.domain.Inbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meta on 15/07/18.
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private List<Inbox> items = new ArrayList<>();

    public InboxAdapter() {
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(Inbox item) {
        items.add(item);
    }

    public void add(Inbox item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void addAll(List<Inbox> items) {
        for (Inbox item : items) {
            add(item);
        }
    }

    public void clear() {
        if (items != null && !items.isEmpty()) {
            notifyItemRangeRemoved(0, getItemCount());
            items.clear();
        }
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InboxViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common_subtitle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        Inbox item = items.get(position);
        holder.icon.setImageResource(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class InboxViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title, subtitle;

        public InboxViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }
}
