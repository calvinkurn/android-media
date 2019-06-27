package com.tokopedia.topchat.chatlist.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.adapter.IndicatorAdapter;
import com.tokopedia.topchat.chatlist.fragment.InboxChatFragment;
import com.tokopedia.topchat.chatlist.viewmodel.IndicatorItem;
import com.tokopedia.topchat.chatlist.view.ChatNotifInterface;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.TopChatRouter;
import com.tokopedia.topchat.common.analytics.TopChatAnalytics;
import com.tokopedia.topchat.common.util.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InboxChatActivity extends BaseSimpleActivity
        implements InboxMessageConstant, ChatNotifInterface,
        IndicatorAdapter.OnIndicatorClickListener {

    private static final int POSITION_TOP_CHAT = 0;
    private static final int POSITION_GROUP_CHAT = 1;

    private static final String ACTIVE_INDICATOR_POSITION = "active";
    IndicatorAdapter indicatorAdapter;
    RecyclerView indicator;
    View indicatorLayout;

    @Inject
    TopChatAnalytics analytics;

    @DeepLink(ApplinkConst.TOPCHAT_IDLESS)
    public static Intent getCallingIntentTopchatWithoutId(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        Intent destination;

        destination = new Intent(context, InboxChatActivity.class)
                .setData(uri.build())
                .putExtras(extras);

        return destination;
    }

    @DeepLink(ApplinkConst.GROUPCHAT_LIST)
    public static TaskStackBuilder getCallingTaskStack(Context context) {
        Intent homeIntent = ((TopChatRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent channelListIntent = InboxChatActivity.getChannelCallingIntent(context);
        channelListIntent.putExtra("title", "Play");
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(channelListIntent);
        return taskStackBuilder;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxChatActivity.class);
    }

    public static Intent getChannelCallingIntent(Context context) {
        Intent intent = new Intent(context, InboxChatActivity.class);
        intent.putExtra(ACTIVE_INDICATOR_POSITION, POSITION_GROUP_CHAT);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getScreenName() {
        return TopChatAnalytics.SCREEN_CHAT_LIST;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_inbox_chat;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        indicatorLayout = findViewById(R.id.indicator_layout);
        indicator = findViewById(R.id.indicator);
        indicatorAdapter = IndicatorAdapter.createInstance(getIndicatorList(), this);
        indicator.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .HORIZONTAL, false));
        indicator.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R
                .dimen.dp_30)));
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

    private boolean isIndicatorVisible() {
        return getApplicationContext() instanceof TopChatRouter
                && ((TopChatRouter) getApplicationContext()).isIndicatorVisible();
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
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent homeIntent = ((TopChatRouter) getApplicationContext()).getHomeIntent(this);
            startActivity(homeIntent);
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public void onGetNotif(Bundle data) {

    }

    @Override
    public void onIndicatorClicked(int position) {
        indicatorAdapter.setActiveIndicator(position);
        switch (position) {
            case POSITION_TOP_CHAT:
                initTopChatFragment();
                break;
            case POSITION_GROUP_CHAT:
                analytics.eventClickInboxChannel();
                initGroupChatFragment();
                break;
            default:
        }
    }

    private void initGroupChatFragment() {

        if (!TextUtils.isEmpty(getIntent().getExtras().getString("title"))) {
            getSupportActionBar().setTitle(getIntent().getExtras().getString("title"));
        }
        ;

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        TopChatRouter inboxRouter = (TopChatRouter) getApplicationContext();

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

    public void hideIndicators() {
        indicatorLayout.setVisibility(View.GONE);
    }

    public void showIndicators() {
        if (!GlobalConfig.isSellerApp() && isIndicatorVisible()) {
            indicatorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
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
