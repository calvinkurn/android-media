package com.tokopedia.topchat.chatroom.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.constant.TkpdState;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.activity.InboxChatActivity;
import com.tokopedia.topchat.chatroom.view.listener.ChatNotifInterface;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.TopChatRouter;

/**
 * Created by Nisie on 5/19/16.
 */
public class ChatRoomActivity extends BasePresenterActivity
        implements InboxMessageConstant, NotificationReceivedListener, HasComponent, ChatNotifInterface {


    private static final String TAG = "INBOX_MESSAGE_DETAIL_FRAGMENT";
    public static final String PARAM_SENDER_ROLE = "PARAM_SENDER_ROLE";


    public static final String PARAM_OWNER_FULLNAME = "owner_fullname";
    public static final String PARAM_CUSTOM_SUBJECT = "custom_subject";
    public static final String PARAM_CUSTOM_MESSAGE = "custom_message";
    public static final String PARAM_SHOP_ID = "to_shop_id";
    public static final String PARAM_USER_ID = "to_user_id";
    public static final String PARAM_SOURCE = "source";
    public static final String PARAM_ROLE = "role";
    public static final String ROLE_USER = "Pengguna";
    public static final String ROLE_SELLER = "Penjual";
    final static String SELLER = "shop";
    final static String USER = "user";
    public static final String IS_HAS_ATTACH_BUTTON = "has_attachment";
    public static final String PARAM_AVATAR = "avatar";

    public static final String PARAM_WEBSOCKET = "create_websocket";
    public static final String APPLINKS = "applinks";
    public static final String MESSAGE_ID = "message_id";
    private BroadcastReceiver notifReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setCurrentActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.setCurrentActivity(this);

        if (notifReceiver == null) {
            notifReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String fromPushNotif = intent.getExtras().getString(APPLINKS, "");
                    String fromRoom = "";
                    if (!TextUtils.isEmpty(getIntent().getExtras().getString(MESSAGE_ID, ""))) {
                        fromRoom = ApplinkConst.TOPCHAT.concat(getIntent().getExtras().getString
                                (MESSAGE_ID, ""));
                    }
                    if (!fromRoom.equals(fromPushNotif)) {
                        PushNotification.notify(context, intent.getExtras());
                    }
                }
            };
        }

        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(notifReceiver, new IntentFilter
                    (TkpdState.TOPCHAT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainApplication.setCurrentActivity(null);

        if (notifReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(notifReceiver);
        }
    }

    @Override
    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.header_chat, null);
        toolbar.addView(mCustomView);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(R.color.white)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        setTitle("");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setContentInsetEndWithActions(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        Drawable upArrow = MethodChecker.getDrawable(this, R.drawable.ic_action_back);
        if (upArrow != null) {
            upArrow.setColorFilter(
                    MethodChecker.getColor(this, R.color.grey_700),
                    PorterDuff.Mode.SRC_ATOP
            );
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        Intent detailsIntent;
        extras.putBoolean(PARAM_WEBSOCKET, true);
        detailsIntent = new Intent(context, ChatRoomActivity.class).putExtras(extras);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        if (TextUtils.equals(extras.getString(TkpdInboxRouter.CREATE_TASK_STACK), "false")) {
            taskStackBuilder.addNextIntent(detailsIntent);
            return taskStackBuilder;
        }

        String urlQueryValueTrue = "true";
        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }

        Intent parentIntent;


        if (TextUtils.equals(extras.getString(TkpdInboxRouter.IS_CHAT_BOT), urlQueryValueTrue)
                && context.getApplicationContext() instanceof TopChatRouter) {
            parentIntent = ((TopChatRouter) context.getApplicationContext()).getHelpPageActivity(
                    context,
                    "", false);
        } else {
            parentIntent = new Intent(context, InboxChatActivity.class);
        }


        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CHAT_DETAIL;
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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.container, ChatRoomFragment.createInstance(getIntent().getExtras()),
//                        ChatRoomFragment.TAG)
//                .commit();
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
    protected boolean isLightToolbarThemes() {
        return true;
    }


    @Override
    public void onGetNotif() {

    }

    @Override
    public void onRefreshCart(int status) {

    }

    @Override
    public void onGetNotif(Bundle data) {
//        ChatRoomFragment chatRoomFragment = (ChatRoomFragment) getSupportFragmentManager().findFragmentByTag(TAG);
//        if (chatRoomFragment != null)
//            chatRoomFragment.restackList(data);
    }

    @Override
    public void onBackPressed() {
//        final ChatRoomFragment fragment = (ChatRoomFragment) getSupportFragmentManager().findFragmentByTag(ChatRoomFragment.TAG);
//
//        if (fragment != null) {
//            fragment.onBackPressed();
//        } else {
//            super.onBackPressed();
//        }
    }

    public static Intent getCallingIntent(Context context, String nav, String messageId,
                                          int position, String senderName, String senderTag,
                                          String senderId, String role, int mode, String keyword, String image) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(InboxMessageConstant.PARAM_NAV, nav);
        bundle.putParcelable(InboxMessageConstant.PARAM_MESSAGE, null);
        bundle.putString(InboxMessageConstant.PARAM_MESSAGE_ID, messageId);
        bundle.putInt(InboxMessageConstant.PARAM_POSITION, position);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_NAME, senderName);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_IMAGE, image);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_TAG, senderTag);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_ID, senderId);
        bundle.putString(PARAM_SENDER_ROLE, role);
        bundle.putInt(InboxMessageConstant.PARAM_MODE, mode);
        bundle.putString(InboxMessageConstant.PARAM_KEYWORD, keyword);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }


    public static Intent getAskSellerIntent(Context context, String toShopId,
                                            String shopName, String source, String avatar) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(InboxMessageConstant.PARAM_SENDER_ID, toShopId);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_NAME, shopName);
        bundle.putString(PARAM_SOURCE, source);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_TAG, ROLE_SELLER);
        bundle.putString(PARAM_SENDER_ROLE, SELLER);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_IMAGE, avatar);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                            String customSubject, String customMessage, String
                                                    source, String avatar) {
        Intent intent = getAskSellerIntent(context, toShopId, shopName, source,
                avatar);
        Bundle bundle = intent.getExtras();
        bundle.putString(PARAM_CUSTOM_SUBJECT, customSubject);
        bundle.putString(PARAM_CUSTOM_MESSAGE, customMessage);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskUserIntent(Context context, String userId,
                                          String userName, String source,
                                          String avatar) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_NAME, userName);
        bundle.putString(PARAM_SOURCE, source);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_TAG, ROLE_USER);
        bundle.putString(PARAM_SENDER_ROLE, USER);
        bundle.putString(InboxMessageConstant.PARAM_SENDER_IMAGE, avatar);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskBuyerIntent(Context context, String toUserId, String
            customerName, String customSubject, String customMessage, String source,
                                           String avatar) {
        Intent intent = getAskUserIntent(context, toUserId, customerName, source, avatar);
        Bundle bundle = intent.getExtras();
        bundle.putString(PARAM_CUSTOM_SUBJECT, customSubject);
        bundle.putString(PARAM_CUSTOM_MESSAGE, customMessage);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getChatBotIntent(Context context, String messageId) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(InboxMessageConstant.PARAM_MESSAGE_ID, messageId);
        bundle.putBoolean(TkpdInboxRouter.IS_CHAT_BOT, true);
        intent.putExtras(bundle);
        return intent;
    }

    public void destroy() {
        super.onBackPressed();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }
}
