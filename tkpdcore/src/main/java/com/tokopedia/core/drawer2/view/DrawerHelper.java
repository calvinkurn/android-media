package com.tokopedia.core.drawer2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.presenter.SessionView;
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

    public abstract void setEnabled(boolean isEnabled);

    @Override
    public void onItemClicked(DrawerItem item) {
        Intent intent;
        switch (item.getId()) {
            case TkpdState.DrawerPosition.INDEX_HOME:
                intent = HomeRouter.getHomeActivity(context);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.LOGIN:
            case TkpdState.DrawerPosition.REGISTER:
                intent = SessionRouter.getLoginActivityIntent(context);
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, item.getId());
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                context.startActivity(intent);
//                context.finish();
                break;
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
            case TkpdState.DrawerPosition.SETTINGS:
                context.startActivity(new Intent(context, ManageGeneral.class));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SETTING);
                break;
            case TkpdState.DrawerPosition.CONTACT_US:
                intent = InboxRouter.getContactUsActivityIntent(context);
                if (TrackingUtils.getBoolean(AppEventTracking.GTM.CREATE_TICKET)) {
                    intent.putExtra("link", "https://tokopedia.com/contact-us-android");
                }
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.LOGOUT:
                SessionHandler session = new SessionHandler(context);
                session.Logout(context);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SIGN_OUT);
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

    public DrawerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(DrawerAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract void setFooterData(DrawerProfile profile);

    public abstract void setSelectedPosition(int id);

    public abstract void setExpand();
}
