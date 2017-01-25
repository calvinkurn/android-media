package com.tokopedia.core.drawer2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.drawer2.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.model.DrawerItem;
import com.tokopedia.core.drawer2.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;

/**
 * Created by nisie on 1/11/17.
 */

public abstract class DrawerHelper implements DrawerItemDataBinder.DrawerItemListener {

    public DrawerAdapter adapter;

    protected DrawerLayout drawerLayout;
    protected RecyclerView recyclerView;
    protected Toolbar toolbar;

    protected Activity context;

    public DrawerHelper(Activity activity) {
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout_nav);
        recyclerView = (RecyclerView) activity.findViewById(R.id.left_drawer);
        toolbar = (Toolbar) activity.findViewById(R.id.app_bar);
        this.context = activity;
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
        Intent intent;
        switch (item.getPosition()) {
            case TkpdState.DrawerPosition.INBOX_MESSAGE:
                intent = InboxRouter.getInboxMessageActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.MESSAGE);
                break;
            case TkpdState.DrawerPosition.INBOX_TALK:
                intent = InboxRouter.getInboxTalkActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISCUSSION);
                break;
            case TkpdState.DrawerPosition.INBOX_REVIEW:
                startIntent(context, InboxReputationActivity.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.REVIEW);
                break;
            case TkpdState.DrawerPosition.INBOX_TICKET:
                intent = InboxRouter.getInboxTicketActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.HELP);
                break;
            case TkpdState.DrawerPosition.RESOLUTION_CENTER:
                intent = InboxRouter.getInboxResCenterActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.RESOLUTION_CENTER);
                break;
            case TkpdState.DrawerPosition.DEVELOPER_OPTIONS:
                startIntent(context, DeveloperOptions.class);
                break;
            default:
                Log.d("NISNIS", item.getLabel());
        }
    }

    protected void sendGTMNavigationEvent(String label) {
        UnifyTracking.eventDrawerClick(label);
    }

    protected static void startIntent(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    public abstract boolean isOpened();

    public abstract void setNotification(DrawerNotification drawerNotification);

    public DrawerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(DrawerAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract void setFooterData(DrawerProfile profile);
}
