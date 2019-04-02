package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.DrawerNotification;
import com.tokopedia.navigation.presentation.adapter.viewholder.BaseViewHolder;

/**
 * Created by meta on 03/07/18.
 */
public class NotificationChildAdapter extends BaseListAdapter<DrawerNotification.ChildDrawerNotification, BaseViewHolder> {

    public NotificationChildAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_single_notification;
    }

    @NonNull
    @Override
    public BaseViewHolder<DrawerNotification.ChildDrawerNotification> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(getView(parent, viewType), onItemClickListener);
    }

    public class NotificationHolder extends BaseViewHolder<DrawerNotification.ChildDrawerNotification> {

        private LabelView labelView;

        public NotificationHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            labelView = itemView.findViewById(R.id.labelview);
        }

        @Override
        public void bind(DrawerNotification.ChildDrawerNotification item) {
            labelView.setTitle(item.getTitle());

            if (item.getBadge() != null && item.getBadge() > 0) {
                labelView.setBadgeCounter(item.getBadge());
            }
            labelView.showRightArrow(true);
        }
    }
}
