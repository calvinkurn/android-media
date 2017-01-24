package com.tokopedia.core.drawer2;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.model.DrawerItem;
import com.tokopedia.core.drawer2.viewmodel.DrawerData;

import java.util.ArrayList;

/**
 * Created by nisie on 1/11/17.
 */

public abstract class DrawerHelper implements DrawerItemDataBinder.DrawerItemListener{

    public DrawerAdapter adapter;

    protected DrawerLayout drawerLayout;
    protected RecyclerView recyclerView;
    protected Toolbar toolbar;


    public DrawerHelper(Activity activity) {
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout_nav);
        recyclerView = (RecyclerView) activity.findViewById(R.id.left_drawer);
        toolbar = (Toolbar) activity.findViewById(R.id.app_bar);
    }


    public void setActionToolbar(Activity activity) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    protected ArrayList<DrawerItem> createDrawerData() {
        ArrayList data = new ArrayList();
        return data;
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public abstract void initDrawer(Activity activity);

//    public abstract ToolbarBuyerHandler.OnDrawerToggleClickListener onDrawerToggleClick();

    public abstract void setEnabled(boolean isEnabled);

    @Override
    public void onItemClicked(DrawerItem item) {
        Log.d("NISNIS", item.getLabel());
    }

    public abstract boolean isOpened();


    public abstract void setData(DrawerData drawerData);
}
