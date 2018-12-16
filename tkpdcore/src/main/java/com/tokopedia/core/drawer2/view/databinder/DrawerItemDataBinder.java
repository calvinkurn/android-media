package com.tokopedia.core.drawer2.view.databinder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerItemDataBinder extends DataBinder<DrawerItemDataBinder.ViewHolder> {

    private static final String MAX_PLACEHOLDER = "999+";
    private int selectedItem;

    public interface DrawerItemListener {
        void onItemClicked(DrawerItem item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.label)
        TextView label;

        @BindView(R2.id.icon)
        ImageView icon;

        @BindView(R2.id.notif)
        TextView notification;

        @BindView(R2.id.new_drawer_menu)
        TextView newDrawerHighlight;

        @BindView(R2.id.drawer_item)
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DrawerItemDataBinder(DataBindAdapter dataBindAdapter,
                                Context context,
                                DrawerItemListener listener,
                                ArrayList<DrawerItem> data) {
        super(dataBindAdapter);
        this.context = context;
        this.listener = listener;
        this.data = data;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_item, parent, false);
        return new DrawerItemDataBinder.ViewHolder(itemLayoutView);
    }

    ArrayList<DrawerItem> data;
    Context context;
    DrawerItemListener listener;
    int drawerPosition = 0;

    @Override
    public void bindViewHolder(DrawerItemDataBinder.ViewHolder holder, int position) {
        final DrawerItem item = data.get(position);

        holder.label.setText(item.getLabel());
        setNotif(item.getNotif(), holder);
        setSelectedBackground(item, holder);
        holder.icon.setImageResource(item.getIconId());

        if (item.getId() == selectedItem) {
            holder.label.setTypeface(null, Typeface.BOLD);
        } else {
            holder.label.setTypeface(null, Typeface.NORMAL);
        }

        if (item.isNew()) {
            holder.newDrawerHighlight.setVisibility(View.VISIBLE);
        } else {
            holder.newDrawerHighlight.setVisibility(View.GONE);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(item);
            }
        });

    }

    private void setSelectedBackground(DrawerItem item, ViewHolder holder) {
        if (item.getId() == drawerPosition) {
            holder.label.setTypeface(null, Typeface.BOLD);
        } else {
            holder.label.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setNotif(int notif, ViewHolder holder) {
        holder.notification.setVisibility(View.VISIBLE);
        if (notif > 0) {
            holder.notification.setText(String.valueOf(notif));
        } else if (notif > 999) {
            holder.notification.setText(MAX_PLACEHOLDER);
        } else {
            holder.notification.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return selectedItem;
    }
}
