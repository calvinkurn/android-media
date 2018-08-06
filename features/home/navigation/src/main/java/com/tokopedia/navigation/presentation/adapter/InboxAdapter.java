package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.navigation.R;

import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;
import com.tokopedia.navigation_common.model.NotificationsModel;


/**
 * Created by meta on 15/07/18.
 */
public class InboxAdapter extends BaseListAdapter<Inbox, InboxAdapter.InboxViewHolder> {

    public InboxAdapter(Context context) {
        super(context);
    }

    public void updateValue(NotificationsModel entity) {
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
        return R.layout.item_single_notification;
    }

    class InboxViewHolder extends BaseViewHolder<Inbox> {

        private LabelView labelView;

        InboxViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            labelView = itemView.findViewById(R.id.labelview);
        }


        @Override
        public void bind(Inbox item) {
            labelView.setImageResource(item.getIcon());
            labelView.setTitle(context.getString(item.getTitle()));
            labelView.setSubTitle(context.getString(item.getSubtitle()));
            if (item.getTotalBadge() != null
                    && !item.getTotalBadge().isEmpty()
                    && !item.getTotalBadge().equalsIgnoreCase("0")) {
                labelView.setBadgeCounter(item.getTotalBadge());
            }
            labelView.showRightArrow(false);
        }
    }
}
