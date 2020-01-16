package com.tokopedia.core.drawer2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.analytics.AnalyticsEventTrackingHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core2.R;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.referral.view.activity.ReferralActivity;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;

/**
 * Created by nisie on 1/11/17.
 */

public abstract class DrawerHelper implements DrawerItemDataBinder.DrawerItemListener {
    public static final String DRAWER_CACHE = "DRAWER_CACHE";
    public static final int REQUEST_LOGIN = 345;
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

    @Override
    public void onItemClicked(DrawerItem item) {
        Intent intent;
        switch (item.getId()) {
            case TkpdState.DrawerPosition.LOGIN:
                intent = ((TkpdCoreRouter) context.getApplication()).getLoginIntent(context);
                context.startActivityForResult(intent, REQUEST_LOGIN);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, "Login", "Login");
                break;
            case TkpdState.DrawerPosition.REGISTER:
                intent = ((TkpdCoreRouter) context.getApplication()).getRegisterIntent(context);
                context.startActivityForResult(intent, REQUEST_LOGIN);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, "Register",AppEventTracking.EventLabel.REGISTER);
                break;
            case TkpdState.DrawerPosition.INBOX_MESSAGE:
                if (context.getApplication() instanceof TkpdCoreRouter) {
                    intent = RouteManager.getIntent(context, ApplinkConst.TOPCHAT_IDLESS);
                    context.startActivity(intent);

                    sendGTMNavigationEvent(AppEventTracking.EventLabel.MESSAGE);
                    TrackApp.getInstance().getGTM().sendGeneralEvent("clickNavigationDrawer",
                            "left navigation",
                            "click on groupchat",
                            "");
                    AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, intent.getComponent().getClassName(), AppEventTracking.EventLabel.INBOX, AppEventTracking.EventLabel.MESSAGE);

                }
                break;
            case TkpdState.DrawerPosition.INBOX_TALK:
                intent = ((TkpdCoreRouter) context.getApplication()).getInboxTalkCallingIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISCUSSION);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, intent.getComponent().getClassName(), AppEventTracking.EventLabel.INBOX, AppEventTracking.EventLabel.PRODUCT_DISCUSSION);

                break;
            case TkpdState.DrawerPosition.INBOX_REVIEW:
                if (context.getApplication() instanceof TkpdCoreRouter) {
                    intent = ((TkpdCoreRouter) context.getApplication())
                            .getInboxReputationIntent(context);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.REVIEW);
                    AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, intent.getComponent().getClassName(), AppEventTracking.EventLabel.INBOX,AppEventTracking.EventLabel.REVIEW);

                }
                break;
            case TkpdState.DrawerPosition.INBOX_TICKET:
                intent = InboxRouter.getInboxTicketActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.HELP);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, intent.getComponent().getClassName(), AppEventTracking.EventLabel.INBOX, AppEventTracking.EventLabel.HELP);

                break;
            case TkpdState.DrawerPosition.DEVELOPER_OPTIONS:
                RouteManager.route(context, ApplinkConst.DEVELOPER_OPTIONS);
                break;
            case TkpdState.DrawerPosition.SETTINGS:
                context.startActivity(new Intent(context, ManageGeneral.class));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SETTING);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, ManageGeneral.class.getCanonicalName(), AppEventTracking.EventLabel.SETTING);

                break;
            case TkpdState.DrawerPosition.APPSHARE:
                context.startActivity(new Intent(context, ReferralActivity.class));
                sendReferralGTMNavigationEvent(item);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, ReferralActivity.class.getCanonicalName(), AppEventTracking.EventLabel.REFERRAL);

                break;

            case TkpdState.DrawerPosition.CONTACT_US:
                intent = RouteManager.getIntent(context, ApplinkConst.CONTACT_US);
                context.startActivity(intent);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, intent.getComponent().getClassName(), "Contact_Us");
                break;
            case TkpdState.DrawerPosition.HELP:
                intent = InboxRouter.getContactUsActivityIntent(context);
                context.startActivity(intent);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, intent.getComponent().getClassName(), AppEventTracking.EventLabel.HELP);

                break;
            case TkpdState.DrawerPosition.LOGOUT:
                SessionHandler session = new SessionHandler(context);
                session.Logout(context);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SIGN_OUT);
                AnalyticsEventTrackingHelper.hamburgerOptionClicked(context, "Home", "Logout");

                break;
            default:
                Log.d(DrawerHelper.class.getSimpleName(), item.getLabel());
        }
    }

    protected void sendGTMNavigationEvent(String label) {
        eventDrawerClick(label);
    }

    public void eventDrawerClick(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                label);
    }

    protected static void startIntent(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    public abstract boolean isOpened();

    public DrawerAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
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

    private void sendReferralGTMNavigationEvent(DrawerItem item) {
        if (context.getString(R.string.drawer_title_appshare).equalsIgnoreCase(item.getLabel())) {
            sendGTMNavigationEvent(AppEventTracking.EventLabel.APPSHARE);
        } else {
            sendGTMNavigationEvent(AppEventTracking.EventLabel.REFERRAL);
        }
    }
}
