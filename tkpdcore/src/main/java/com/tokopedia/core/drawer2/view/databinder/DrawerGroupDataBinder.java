package com.tokopedia.core.drawer2.view.databinder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by nisie on 1/11/17.
 */

public class DrawerGroupDataBinder extends DataBinder<DrawerGroupDataBinder.ViewHolder> {

    public interface DrawerGroupListener {
        void onGroupClicked(DrawerGroup group, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.label)
        TextView label;

        @BindView(R2.id.icon)
        ImageView icon;

        @BindView(R2.id.toggle_notif)
        TextView notificationAlert;

        @BindView(R2.id.notif)
        TextView notification;

        @BindView(R2.id.drawer_item)
        RelativeLayout layout;

        @BindView(R2.id.arrow)
        ImageView arrow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<DrawerItem> data;
    private DrawerGroupListener listener;

    public DrawerGroupDataBinder(DataBindAdapter dataBindAdapter, DrawerGroupListener listener, ArrayList<DrawerItem> data) {
        super(dataBindAdapter);
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_group, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, final int position) {
        if (data.get(position) instanceof DrawerGroup) {
            final DrawerGroup group = (DrawerGroup) data.get(position);

            setArrowPosition(holder, group);
            setNotification(holder, group);
            holder.label.setText(group.label);
            setGroupIcon(holder, group);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGroupClicked(group,position);
                }
            });
        }
    }

    private void setGroupIcon(ViewHolder holder, DrawerGroup group) {
        if (group.iconId != 0) {
            holder.icon.setImageResource(group.iconId);
        }
    }

    private void setNotification(ViewHolder holder, DrawerGroup group) {
        holder.notification.setVisibility(View.GONE);
        if (group.notif > 0) {
            holder.notificationAlert.setVisibility(View.VISIBLE);
        } else {
            holder.notificationAlert.setVisibility(View.GONE);
        }

    }

    private void setArrowPosition(ViewHolder holder, DrawerGroup group) {
        if (group.isExpanded) {
            holder.arrow.setImageResource(R.drawable.arrow_up);
        } else {
            holder.arrow.setImageResource(R.drawable.arrow_drop_down);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<DrawerGroup> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

}
