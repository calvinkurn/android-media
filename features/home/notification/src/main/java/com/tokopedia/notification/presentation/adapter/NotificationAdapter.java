package com.tokopedia.notification.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
public class NotificationAdapter extends BaseListAdapter<Notification, BaseViewHolder> {

    private static final int TYPE_ITEM = 2;
    private static final int TYPE_SINGLE = 3;

    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        if(viewType == TYPE_SINGLE) {
            return R.layout.item_single;
        } else if(viewType == TYPE_ITEM) {
            return R.layout.item_notification;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getTitle() == null || items.get(position).getTitle().isEmpty()) {
            return TYPE_SINGLE;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SINGLE) {
            return new NotificationSingleHolder(getView(parent, viewType), onItemClickListener);
        }
        return new NotificationHolder(getView(parent, viewType));
    }

    public class NotificationSingleHolder extends BaseViewHolder<Notification> {

        private TextView title;
        private TextView badge;
        private ImageView arrow;

        NotificationSingleHolder(View itemView, BaseListAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            title = itemView.findViewById(R.id.title);
            badge = itemView.findViewById(R.id.text_badge);
            arrow = itemView.findViewById(R.id.image_arrow);
        }

        @Override
        public void bind(Notification item) {
            Notification.ChildNotification child = item.getChilds().get(0);
            if (child == null)
                return;

            title.setText(child.getTitle());

            if (child.getBadge() > 0) {
                badge.setVisibility(View.VISIBLE);
                arrow.setVisibility(View.VISIBLE);
                badge.setText(String.format("%s", child.getBadge()));
            } else {
                badge.setVisibility(View.GONE);
                arrow.setVisibility(View.GONE);
            }
        }
    }

    public class NotificationHolder extends BaseViewHolder<Notification> {

        private RecyclerView recyclerView;
        private TextView textView;

        NotificationHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerview);
            textView = itemView.findViewById(R.id.title);
        }

        @Override
        public void bind(Notification item) {
            textView.setText(item.getTitle());

            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);

            NotificationChildAdapter adapter = new NotificationChildAdapter(context);
            adapter.addAll(item.getChilds());
            recyclerView.setAdapter(adapter);
        }
    }
}
