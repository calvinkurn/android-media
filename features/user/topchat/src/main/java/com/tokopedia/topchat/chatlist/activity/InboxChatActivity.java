package com.tokopedia.topchat.chatlist.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.listener.ChatNotifInterface;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.TopChatRouter;
import com.tokopedia.topchat.common.util.SpaceItemDecoration;
import com.tokopedia.topchat.chatlist.adapter.IndicatorAdapter;
import com.tokopedia.topchat.common.analytics.TopChatAnalytics;
import com.tokopedia.topchat.chatlist.fragment.InboxChatFragment;
import com.tokopedia.topchat.chatlist.viewmodel.IndicatorItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InboxChatActivity extends DrawerPresenterActivity
        implements InboxMessageConstant, NotificationReceivedListener, HasComponent,
        ChatNotifInterface, IndicatorAdapter.OnIndicatorClickListener {

    private static final int POSITION_TOP_CHAT = 0;
    private static final int POSITION_GROUP_CHAT = 1;

    private static final String ACTIVE_INDICATOR_POSITION = "active";
    IndicatorAdapter indicatorAdapter;
    RecyclerView indicator;
    View indicatorLayout;

    @Inject
    SessionHandler sessionHandler;

    @DeepLink(Constants.Applinks.TOPCHAT_IDLESS)
    public static Intent getCallingIntentTopchatWithoutId(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }

        Intent destination;

        destination = new Intent(context, InboxChatActivity.class)
                .setData(uri.build())
                .putExtras(extras);

        return destination;
    }

    @DeepLink(ApplinkConst.GROUPCHAT_LIST)
    public static TaskStackBuilder getCallingTaskStack(Context context) {
        Intent homeIntent = ((TkpdInboxRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent channelListIntent = InboxChatActivity.getChannelCallingIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        if (((TopChatRouter) context.getApplicationContext()).isEnabledGroupChat()) {
            taskStackBuilder.addNextIntent(channelListIntent);
        }
        return taskStackBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
        MainApplication.setCurrentActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainApplication.setCurrentActivity(null);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CHAT;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inbox_chat;
    }

    @Override
    protected void initView() {
        super.initView();
        indicatorLayout = findViewById(R.id.indicator_layout);
        indicator = findViewById(R.id.indicator);
        indicatorAdapter = IndicatorAdapter.createInstance(getIndicatorList(), this);
        indicator.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .HORIZONTAL, false));
        indicator.addItemDecoration(new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.step_size_nob)));
        indicator.setAdapter(indicatorAdapter);

        if (isIndicatorVisible()) {
            indicatorLayout.setVisibility(View.VISIBLE);
        } else {
            indicatorLayout.setVisibility(View.GONE);
        }

        if (getIntent().getExtras() != null
                && getIntent().getExtras().getInt(ACTIVE_INDICATOR_POSITION, -1) != -1) {
            indicatorAdapter.setActiveIndicator(getIntent().getExtras().getInt
                    (ACTIVE_INDICATOR_POSITION, 0));
            initGroupChatFragment();
        } else {
            initTopChatFragment();
        }

    }

    private boolean isEnabledGroupChat() {
        return getApplicationContext() instanceof TkpdInboxRouter
                && ((TkpdInboxRouter) getApplicationContext()).isEnabledGroupChat();
    }

    private boolean isIndicatorVisible() {
        return getApplicationContext() instanceof TkpdInboxRouter
                && ((TkpdInboxRouter) getApplicationContext()).isIndicatorVisible();
    }

    private List<IndicatorItem> getIndicatorList() {
        List<IndicatorItem> list = new ArrayList<>();
        if (!GlobalConfig.isSellerApp()) {
            list.add(new IndicatorItem(getString(R.string.title_personal), R.drawable
                    .ic_indicator_topchat, true));
            list.add(new IndicatorItem(getString(R.string.title_community), R.drawable
                    .ic_indicator_channel, false));
        }
        return list;
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_MESSAGE;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            finish();
        } else if (isTaskRoot()) {
            startActivity(HomeRouter.getHomeActivity(this));
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public void onGetNotif(Bundle data) {
        super.onGetNotif(data);
    }

    @Override
    public void onSuccessGetTopChatNotification(int notifUnreads) {

        if (notifUnreads > 0) {
            TextView titleTextView = (TextView) toolbar.findViewById(R.id.actionbar_title);
            titleTextView.setText(String.format(getString(R.string.chat_title), notifUnreads));
        } else {
            TextView titleTextView = (TextView) toolbar.findViewById(R.id.actionbar_title);
            titleTextView.setText(getString(R.string.chat_title_without_notif));
        }

        indicatorAdapter.setNotification(POSITION_TOP_CHAT, notifUnreads);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxChatActivity.class);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void onIndicatorClicked(int position) {
        indicatorAdapter.setActiveIndicator(position);
        switch (position) {
            case POSITION_TOP_CHAT:
                initTopChatFragment();
                break;
            case POSITION_GROUP_CHAT:
                AbstractionRouter abstractionRouter = (AbstractionRouter) getActivity().getApplicationContext();
                abstractionRouter.getAnalyticTracker().sendEventTracking(
                        TopChatAnalytics.eventClickInboxChannel().getEvent());
                initGroupChatFragment();
                break;
            default:
        }
    }

    private void initGroupChatFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        TkpdInboxRouter inboxRouter = (TkpdInboxRouter) getApplicationContext();

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (inboxRouter.getChannelFragmentTag());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = inboxRouter.getChannelFragment(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    private void initTopChatFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxChatFragment.class.getSimpleName());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = InboxChatFragment.createInstance(InboxMessageConstant.MESSAGE_ALL);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public static Intent getChannelCallingIntent(Context context) {
        Intent intent = new Intent(context, InboxChatActivity.class);
        intent.putExtra(ACTIVE_INDICATOR_POSITION, POSITION_GROUP_CHAT);
        return intent;
    }

    public void hideIndicators() {
        indicatorLayout.setVisibility(View.GONE);
    }

    public void showIndicators() {
        if (!GlobalConfig.isSellerApp() && isIndicatorVisible()) {
            indicatorLayout.setVisibility(View.VISIBLE);
        }
    }

    public void updateNotifDrawerData(){
        updateDrawerData();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(Menu.NONE, R.id.action_organize, 0, "");
//        MenuItem menuItem = menu.findItem(R.id.action_reset); // OR THIS
//        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        menuItem.setIcon(getDetailMenuItem());
//        return true;
//    }
//
//    private Drawable getDetailMenuItem() {
//        TextDrawable drawable = new TextDrawable(this);
//        drawable.setText(getResources().getString(R.string.option_organize));
//        drawable.setTextColor(Color.parseColor("#42b549"));
//        return drawable;
//    }
}
