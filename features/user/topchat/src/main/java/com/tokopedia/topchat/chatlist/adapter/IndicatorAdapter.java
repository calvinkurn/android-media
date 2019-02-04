package com.tokopedia.topchat.chatlist.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.viewmodel.IndicatorItem;
import com.tokopedia.topchat.chatlist.viewmodel.IndicatorItem;

import java.util.List;

/**
 * @author by nisie on 2/22/18.
 */

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder> {

    public interface OnIndicatorClickListener {
        void onIndicatorClicked(int position);
    }

    private List<IndicatorItem> list;
    private Context context;

    private final OnIndicatorClickListener listener;
    private int currentActivePosition = 0;

    public static IndicatorAdapter createInstance(List<IndicatorItem> list, OnIndicatorClickListener listener) {
        return new IndicatorAdapter(list, listener);
    }

    private IndicatorAdapter(List<IndicatorItem> list, OnIndicatorClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout wrapper;
        TextView title;
        ImageView icon;
        TextView notification;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            notification = itemView.findViewById(R.id.notification);
            mainView = itemView.findViewById(R.id.main_view);
            wrapper = itemView.findViewById(R.id.icon_wrapper);

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onIndicatorClicked(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.indicator_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        IndicatorItem indicatorItem = list.get(position);
        holder.title.setText(indicatorItem.getTitle());
        ImageHandler.loadImageWithIdWithoutPlaceholder(holder.icon, indicatorItem.getIconResId());

        if (indicatorItem.getNotificationCount() > 0) {
            holder.notification.setText(String.valueOf(indicatorItem.getNotificationCount()));
            holder.notification.setVisibility(View.VISIBLE);
        } else {
            holder.notification.setVisibility(View.GONE);
        }

        if (indicatorItem.isActive()) {
            setActive(holder);
        } else {
            setInactive(holder);
        }

    }

    private void setInactive(ViewHolder holder) {
        holder.title.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        holder.title.setTextColor(MethodChecker.getColor(context, R.color.black_38));
        MethodChecker.setBackground(holder.wrapper, null);
    }

    private void setActive(ViewHolder holder) {
        holder.title.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        holder.title.setTextColor(MethodChecker.getColor(context, R.color.black_70));
        MethodChecker.setBackground(holder.wrapper, MethodChecker.getDrawable(context, R.drawable
                .shadow_background_circle));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setActiveIndicator(int position) {

        list.get(currentActivePosition).setActive(false);
        list.get(position).setActive(true);
        this.currentActivePosition = position;

        notifyItemRangeChanged(0, list.size());
    }

    public void setNotification(int indicatorPosition, int notifUnreads) {
        list.get(indicatorPosition).setNotificationCount(notifUnreads);
        notifyItemChanged(indicatorPosition);
    }
}
