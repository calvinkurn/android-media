package com.tokopedia.core.drawer2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.referral.ReferralActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;

/**
 * Created by nisie on 1/11/17.
 */

public abstract class DrawerHelper implements DrawerItemDataBinder.DrawerItemListener {
    public static final String DRAWER_CACHE = "DRAWER_CACHE";
    protected LocalCacheHandler drawerCache;

    public DrawerAdapter adapter;

    protected DrawerLayout drawerLayout;
    protected RecyclerView recyclerView;
    protected Toolbar toolbar;

    protected Activity context;
    protected int selectedPosition = -1;

    public DrawerHelper(Activity activity) {
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout_nav);
        recyclerView = (RecyclerView) activity.findViewById(R.id.left_drawer);
        toolbar = (Toolbar) activity.findViewById(R.id.app_bar);
        this.context = activity;
    }

    public abstract ArrayList<DrawerItem> createDrawerData();

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public abstract void initDrawer(Activity activity);

//    public abstract ToolbarBuyerHandler.OnDrawerToggleClickListener onDrawerToggleClick();

    @Override
    public void onItemClicked(DrawerItem item) {
        Intent intent;
        switch (item.getId()) {
            case TkpdState.DrawerPosition.LOGIN:
                intent = ((TkpdCoreRouter) context.getApplication()).getLoginIntent(context);
                Intent intentHome = ((TkpdCoreRouter) context.getApplication()).getHomeIntent
                        (context);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivities(new Intent[]
                        {
                                intentHome,
                                intent
                        });
                context.finish();
                break;
            case TkpdState.DrawerPosition.REGISTER:
                intent = ((TkpdCoreRouter) context.getApplication()).getRegisterIntent(context);
                intentHome = ((TkpdCoreRouter) context.getApplication()).getHomeIntent
                        (context);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivities(new Intent[]
                        {
                                intentHome,
                                intent
                        });
                context.finish();
                break;

            case TkpdState.DrawerPosition.INBOX_TALK:
                intent = InboxRouter.getInboxTalkActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISCUSSION);
                break;
            case TkpdState.DrawerPosition.INBOX_REVIEW:
                if (context.getApplication() instanceof TkpdCoreRouter) {
                    context.startActivity(((TkpdCoreRouter) context.getApplication())
                            .getInboxReputationIntent(context));
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.REVIEW);
                }
                break;
            case TkpdState.DrawerPosition.INBOX_TICKET:
                intent = InboxRouter.getInboxTicketActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.HELP);
                break;
            case TkpdState.DrawerPosition.DEVELOPER_OPTIONS:
                startIntent(context, DeveloperOptions.class);
                break;
            case TkpdState.DrawerPosition.SETTINGS:
                context.startActivity(new Intent(context, ManageGeneral.class));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SETTING);
                break;
            case TkpdState.DrawerPosition.APPSHARE:
                context.startActivity(new Intent(context, ReferralActivity.class));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.APPSHARE);
                break;
            case TkpdState.DrawerPosition.CONTACT_US:
                intent = InboxRouter.getContactUsActivityIntent(context);
                intent.putExtra(InboxRouter.PARAM_URL,
                        URLGenerator.generateURLContactUs(TkpdBaseURL.BASE_CONTACT_US, context));
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.HELP:
                intent = InboxRouter.getContactUsActivityIntent(context);
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.LOGOUT:
                SessionHandler session = new SessionHandler(context);
                session.Logout(context);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SIGN_OUT);
                break;
            default:
                Log.d(DrawerHelper.class.getSimpleName(), item.getLabel());
        }
    }

    protected void sendGTMNavigationEvent(String label) {
        UnifyTracking.eventDrawerClick(label);
    }

    protected static void startIntent(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    public abstract boolean isOpened();

    public DrawerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(DrawerAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract void setFooterData(DrawerProfile profile);

    public void setSelectedPosition(int id) {
        this.selectedPosition = id;
    }

    public abstract void setExpand();

    public void setEnabled(boolean isEnabled) {
        if (isEnabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    protected void checkExpand(String key, int idPosition) {
        if (drawerCache.getBoolean(key, false)) {
            DrawerGroup group = findGroup(idPosition);
            if (group != null)
                adapter.getData().addAll(group.getPosition() + 1, group.getList());
        }
    }

    protected DrawerGroup findGroup(int id) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (adapter.getData().get(i) instanceof DrawerGroup
                    && adapter.getData().get(i).getId() == id) {
                adapter.getData().get(i).setPosition(i);
                return (DrawerGroup) adapter.getData().get(i);
            }
        }
        return null;
    }
}
