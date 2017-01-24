package com.tokopedia.core.drawer2;

import android.content.Context;

import com.tokopedia.core.drawer2.databinder.DrawerGroupDataBinder;
import com.tokopedia.core.drawer2.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.drawer2.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.databinder.DrawerSeparatorDataBinder;
import com.tokopedia.core.drawer2.model.DrawerGroup;
import com.tokopedia.core.drawer2.model.DrawerItem;
import com.tokopedia.core.drawer2.model.DrawerSeparator;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import java.util.ArrayList;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerAdapter extends DataBindAdapter implements DrawerGroupDataBinder.DrawerGroupListener {

    private static final int VIEW_HEADER = 100;
    private static final int VIEW_GROUP = 101;
    private static final int VIEW_ITEM = 102;
    private static final int VIEW_SEPARATOR = 103;

    private DrawerHeaderDataBinder drawerHeaderDataBinder;
    private DrawerItemDataBinder drawerItemDataBinder;
    private DrawerGroupDataBinder drawerGroupDataBinder;
    private DrawerSeparatorDataBinder drawerSeparatorDataBinder;


    private ArrayList<DrawerItem> data;

    public DrawerAdapter(Context context,
                         DrawerItemDataBinder.DrawerItemListener itemListener) {
        super();
        this.data = new ArrayList<>();
        drawerGroupDataBinder = new DrawerGroupDataBinder(this, this, this.data);
        drawerItemDataBinder = new DrawerItemDataBinder(this, context, itemListener, this.data);
        drawerSeparatorDataBinder = new DrawerSeparatorDataBinder(this);
    }


    public static DrawerAdapter createAdapter(Context context,
                                              DrawerItemDataBinder.DrawerItemListener itemListener
    ) {
        return new DrawerAdapter(context, itemListener);
    }

    @Override
    public int getItemCount() {
        return drawerHeaderDataBinder.getItemCount() +
                data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && drawerHeaderDataBinder.getItemCount() > 0) {
            return VIEW_HEADER;
        } else if (isDrawerSeparator(position)) {
            return VIEW_SEPARATOR;
        } else if (isDrawerGroup(position)) {
            return VIEW_GROUP;
        } else
            return VIEW_ITEM;
    }

    private boolean isDrawerSeparator(int position) {
        return data.get(position - drawerHeaderDataBinder.getItemCount()) instanceof DrawerSeparator;
    }

    private boolean isDrawerGroup(int position) {
        return data.get(position - drawerHeaderDataBinder.getItemCount()) instanceof DrawerGroup;
    }

    @Override
    public DataBinder getDataBinder(int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                return drawerHeaderDataBinder;
            case VIEW_GROUP:
                return drawerGroupDataBinder;
            case VIEW_SEPARATOR:
                return drawerSeparatorDataBinder;
            default:
                return drawerItemDataBinder;
        }
    }

    @Override
    public int getBinderPosition(int position) {
        switch (getItemViewType(position)) {
            case VIEW_HEADER:
                return 0;
            default:
                return position - drawerHeaderDataBinder.getItemCount();
        }
    }

    @Override
    public void onGroupClicked(DrawerGroup group, int position) {

        if (group.isExpanded())
            data.removeAll(group.getList());
        else {
            data.addAll(position + 1, group.getList());
        }

        for (DrawerItem item : group.getList()) {
            item.setExpanded(!group.isExpanded());
        }
        group.setExpanded(!group.isExpanded());


        notifyDataSetChanged();
    }

    public ArrayList<DrawerItem> getData() {
        return data;
    }

    public void setData(ArrayList<DrawerItem> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setHeader(DrawerHeaderDataBinder header) {
        this.drawerHeaderDataBinder = header;
    }

    public DrawerHeaderDataBinder getHeader() {
        return drawerHeaderDataBinder;
    }

}
