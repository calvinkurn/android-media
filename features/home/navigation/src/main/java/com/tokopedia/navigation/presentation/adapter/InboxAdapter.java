package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tokopedia.navigation.R;

import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;


/**
 * Created by meta on 15/07/18.
 */
public class InboxAdapter extends BaseListAdapter<Inbox, InboxAdapter.InboxViewHolder> {

    public InboxAdapter(Context context) {
        super(context);
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
        return new InboxViewHolder(getView(parent, viewType), onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_common_subtitle;
    }

    class InboxViewHolder extends BaseViewHolder<Inbox> {

        private ImageView icon;
        private TextView title, subtitle, badge;

        public InboxViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            badge = itemView.findViewById(R.id.badge);
        }


        @Override
        public void bind(Inbox item) {
            icon.setImageResource(item.getIcon());
            title.setText(item.getTitle());
            subtitle.setText(item.getSubtitle());
            if (item.getTotalBadge() != null
                    && !item.getTotalBadge().isEmpty()
                    && !item.getTotalBadge().equalsIgnoreCase("0")) {
                badge.setVisibility(View.VISIBLE);
                badge.setText(item.getTotalBadge());
            } else {
                badge.setVisibility(View.GONE);
            }
        }
    }
}
