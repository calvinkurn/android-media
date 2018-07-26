package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.DrawerNotification;

/**
 * Created by meta on 03/07/18.
 */
public class NotificationChildAdapter extends BaseListAdapter<DrawerNotification.ChildDrawerNotification, BaseViewHolder> {

    public NotificationChildAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_common_title;
    }

    @NonNull
    @Override
    public BaseViewHolder<DrawerNotification.ChildDrawerNotification> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(getView(parent, viewType), onItemClickListener);
    }

    public class NotificationHolder extends BaseViewHolder<DrawerNotification.ChildDrawerNotification> {

        private TextView subtitle;
        private TextView badge;
        private ImageView arrow;

        public NotificationHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            subtitle = itemView.findViewById(R.id.title);
            badge = itemView.findViewById(R.id.badge);
            arrow = itemView.findViewById(R.id.arrow);
        }

        @Override
        public void bind(DrawerNotification.ChildDrawerNotification item) {
            subtitle.setText(item.getTitle());

            if (item.getBadge() != null && item.getBadge() > 0) {
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
