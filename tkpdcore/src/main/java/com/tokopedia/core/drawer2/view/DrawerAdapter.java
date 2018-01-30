package com.tokopedia.core.drawer2.view;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.view.databinder.DrawerGroupDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerSeparatorDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerSeparator;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;

/**
 * Created by nisie on 1/11/17.
 */

public class DrawerAdapter extends DataBindAdapter implements DrawerGroupDataBinder.DrawerGroupListener {

    private static final int VIEW_HEADER = 100;
    private static final int VIEW_GROUP = 101;
    private static final int VIEW_ITEM = 102;
    private static final int VIEW_SEPARATOR = 103;
    public static final String IS_INBOX_OPENED = "IS_INBOX_OPENED";
    public static final String IS_SHOP_OPENED = "IS_SHOP_OPENED";
    public static final String IS_PEOPLE_OPENED = "IS_PEOPLE_OPENED";
    public static final String IS_RESO_OPENED = "IS_RESO_OPENED";

    public static final String IS_PRODUCT_OPENED = "IS_PRODUCT_OPENED";
    public static final String IS_PRODUCT_DIGITAL_OPENED = "IS_PRODUCT_OPENED";
    public static final String IS_GM_OPENED = "IS_GM_OPENED";

    private DataBinder drawerHeaderDataBinder;
    private DrawerItemDataBinder drawerItemDataBinder;
    private DrawerGroupDataBinder drawerGroupDataBinder;
    private DrawerSeparatorDataBinder drawerSeparatorDataBinder;
    private LocalCacheHandler drawerCache;

    private ArrayList<DrawerItem> data;
    private int selectedItem;

    public DrawerAdapter(Context context,
                         DrawerItemDataBinder.DrawerItemListener itemListener, LocalCacheHandler drawerCache) {
        super();
        this.data = new ArrayList<>();
        this.drawerCache = drawerCache;
        drawerGroupDataBinder = new DrawerGroupDataBinder(this, this, this.data);
        drawerItemDataBinder = new DrawerItemDataBinder(this, context, itemListener, this.data);
        drawerSeparatorDataBinder = new DrawerSeparatorDataBinder(this);
    }


    public static DrawerAdapter createAdapter(Context context,
                                              DrawerItemDataBinder.DrawerItemListener itemListener,
                                              LocalCacheHandler drawerCache) {
        return new DrawerAdapter(context, itemListener, drawerCache);
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

        setExpandCache(group, group.isExpanded);

        notifyDataSetChanged();
    }

    private void setExpandCache(DrawerGroup group, boolean isExpand) {
        switch (group.getId()) {
            case TkpdState.DrawerPosition.INBOX:
                drawerCache.putBoolean(IS_INBOX_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.PEOPLE:
                drawerCache.putBoolean(IS_PEOPLE_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.SHOP:
                drawerCache.putBoolean(IS_SHOP_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.SELLER_PRODUCT_EXTEND:
                drawerCache.putBoolean(IS_PRODUCT_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE:
                drawerCache.putBoolean(IS_GM_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.RESOLUTION_CENTER:
                drawerCache.putBoolean(IS_RESO_OPENED, isExpand);
                break;
            default:
                break;
        }
        drawerCache.applyEditor();
    }

    public ArrayList<DrawerItem> getData() {
        return data;
    }

    public void setData(ArrayList<DrawerItem> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setHeader(DataBinder header) {
        this.drawerHeaderDataBinder = header;
    }

    public DataBinder getHeader() {
        return drawerHeaderDataBinder;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        drawerItemDataBinder.setSelectedItem(selectedItem);
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return selectedItem;
    }
}