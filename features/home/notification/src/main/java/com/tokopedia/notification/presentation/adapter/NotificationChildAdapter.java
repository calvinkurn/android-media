package com.tokopedia.notification.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.notification.R;
import com.tokopedia.notification.domain.Notification;

/**
 * Created by meta on 03/07/18.
 */
public class NotificationChildAdapter extends BaseListAdapter<Notification.ChildNotification, BaseViewHolder> {

    public NotificationChildAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_body_notification;
    }

    @NonNull
    @Override
    public BaseViewHolder<Notification.ChildNotification> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(getView(parent, viewType), onItemClickListener);
    }

    public class NotificationHolder extends BaseViewHolder<Notification.ChildNotification> {

        private TextView subtitle;
        private TextView badge;
        private ImageView arrow;

        public NotificationHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            subtitle = itemView.findViewById(R.id.subtitle);
            badge = itemView.findViewById(R.id.text_badge);
            arrow = itemView.findViewById(R.id.image_arrow);
        }

        @Override
        public void bind(Notification.ChildNotification item) {
            subtitle.setText(item.getTitle());

            if (item.getBadge() > 0) {
                badge.setVisibility(View.VISIBLE);
                arrow.setVisibility(View.VISIBLE);
                badge.setText(String.format("%s", item.getBadge()));
            } else {
                badge.setVisibility(View.GONE);
                arrow.setVisibility(View.GONE);
            }
        }
    }
}
