package com.tokopedia.contactus.inboxticket.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.contactus.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.contactus.inboxticket.presenter.InboxTicketPresenter;
import com.tokopedia.contactus.inboxticket.presenter.InboxTicketPresenterImpl;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by Nisie on 4/21/16.
 */
public class InboxTicketActivity extends DrawerPresenterActivity<InboxTicketPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_TICKET;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTicketPresenterImpl();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inbox_ticket_2;
    }

    @Override
    protected void initView() {
        super.initView();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        Fragment fragment;
        if (getFragmentManager().findFragmentByTag(InboxTicketFragment.class.getSimpleName()) == null) {
            fragment = InboxTicketFragment.createInstance();
        } else {
            fragment = getFragmentManager().findFragmentByTag(InboxTicketFragment.class.getSimpleName());
        }

        fragmentTransaction.replace(R.id.container, fragment, InboxTicketFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_TICKET;
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

    private static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxTicketActivity.class);
    }
}
