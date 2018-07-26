package com.tokopedia.navigation.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tokopedia.navigation.R;

import com.tokopedia.navigation.data.entity.InboxEntity;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.Notification;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;

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
        notifyItemInserted(items.size() - 1);
    }

    public void add(Inbox item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void addAll(List<Inbox> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void updateValue(NotificationEntity.Notification entity) {
        if (items != null && items.size() > 0) {
            items.get(InboxFragment.CHAT_MENU)
                    .setTotalBadge(entity.getChat().getUnreads());

            items.get(InboxFragment.DISCUSSION_MENU)
                    .setTotalBadge(entity.getInbox().getTalk());

            items.get(InboxFragment.REVIEW_MENU)
                    .setTotalBadge(entity.getInbox().getReview());

            items.get(InboxFragment.HELP_MENU)
                    .setTotalBadge(entity.getInbox().getTicket());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InboxViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        Inbox item = items.get(position);
        holder.icon.setImageResource(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
        if (item.getTotalBadge() != null
                && !item.getTotalBadge().isEmpty()
                && !item.getTotalBadge().equalsIgnoreCase("0")) {
            holder.badge.setVisibility(View.VISIBLE);
            holder.badge.setText(item.getTotalBadge());
        } else {
            holder.badge.setVisibility(View.GONE);
        }
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
        private TextView title, subtitle, badge;

        public InboxViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            badge = itemView.findViewById(R.id.badge);
        }
    }
}
