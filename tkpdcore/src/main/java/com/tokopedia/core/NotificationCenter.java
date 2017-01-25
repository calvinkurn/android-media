package com.tokopedia.core;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.MultiPaneActivity;
import com.tokopedia.core.gcm.FCMMessagingService.NotificationListener;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFragment;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

public class NotificationCenter extends MultiPaneActivity implements NotificationListener {

    private PagerAdapter adapter;
    private ViewPager mViewPager;

    private Boolean isLogin;
    private ArrayList<String> CONTENT = new ArrayList<String>();
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    private List<Fragment> detailList = new ArrayList<>();
    private ArrayList<Integer> NotificationCode;
    private TabLayout indicator;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_NOTIFICATION;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        isLogin = SessionHandler.isV4Login(this);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_people_transaction_new);
        initDetailPager();
        int pos = 0;
        if (getIntent().getExtras() != null) pos = getIntent().getExtras().getInt("tab");

        LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.GCM_NOTIFICATION);
        NotificationCode = cache.getArrayListInteger(TkpdCache.Key.NOTIFICATION_CODE);

        for (int i = 0; i < NotificationCode.size(); i++) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            switch (NotificationCode.get(i)) {
                case 101:
                    fragment = InboxRouter.instanceInboxMessageFromNotification(this);
                    bundle.putBoolean("from_notif", true);
                    bundle.putString("nav", "inbox-message");
                    fragment.setArguments(bundle);
                    FragmentList.add(fragment);
                    CONTENT.add(getString(R.string.title_message));
                    break;
                case 102:
                    fragment = InboxRouter.instanceInboxTalkFromNotification(this);
                    bundle.putBoolean("from_notif", true);
                    bundle.putString("nav", "inbox-talk");
                    fragment.setArguments(bundle);
                    FragmentList.add(fragment);
                    CONTENT.add(getString(R.string.title_talk));
                    break;
                case 103:
                    if (!isAlreadyExist(getString(R.string.title_review))) {
                        fragment = InboxReputationFragment.createInstance("inbox-reputation");
                        bundle.putBoolean("from_notif", true);
                        bundle.putString("nav", "inbox-review");
                        fragment.setArguments(bundle);
                        FragmentList.add(fragment);
                        CONTENT.add(getString(R.string.title_review));
                    }
                    break;
                case 113:
                    if (!isAlreadyExist(getString(R.string.title_review))) {
                        fragment = InboxReputationFragment.createInstance("inbox-reputation");
                        bundle.putBoolean("from_notif", true);
                        bundle.putString("nav", "inbox-review");
                        fragment.setArguments(bundle);
                        FragmentList.add(fragment);
                        CONTENT.add(getString(R.string.title_review));
                    }
                    break;
                case 123:
                    if (!isAlreadyExist(getString(R.string.title_review))) {
                        fragment = InboxReputationFragment.createInstance("inbox-reputation");
                        bundle.putBoolean("from_notif", true);
                        bundle.putString("nav", "inbox-review");
                        fragment.setArguments(bundle);
                        FragmentList.add(fragment);
                        CONTENT.add(getString(R.string.title_review));
                    }
                    break;
                case 104:
                    FragmentList.add(InboxRouter.instanceInboxTicketFragmentFromNotification(this));
                    CONTENT.add(getString(R.string.title_activity_inbox_ticket));
                    break;
                case 401:
//                    fragment = FragmentShopNewOrderV2.createInstance();
                    fragment = SellerRouter.getFragmentSellingNewOrder(this);
                    bundle.putBoolean("from_notif", true);
                    fragment.setArguments(bundle);
                    FragmentList.add(fragment);
                    CONTENT.add(getString(R.string.title_tab_new_order));
                    break;
                case 202:
                    if (!isAlreadyExist(getString(R.string.title_review))) {
                        FragmentList.add(InboxReputationFragment.createInstance("inbox-reputation"));
                        CONTENT.add(getString(R.string.title_review));
                    }
                    break;
                case 212:
                    if (!isAlreadyExist(getString(R.string.title_review))) {
                        FragmentList.add(InboxReputationFragment.createInstance("inbox-reputation"));
                        CONTENT.add(getString(R.string.title_review));
                    }
                    break;
                case 301:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_verified))) {
                        FragmentList.add(TransactionPurchaseRouter.instanceTxListFromNotification(this, TransactionPurchaseRouter.PAYMENT_VERIFICATION_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_verified));
                    }
                    break;
                case 302:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_processed))) {
                        FragmentList.add(TransactionPurchaseRouter.instanceTxListFromNotification(this, TransactionPurchaseRouter.PROCESSING_TRANSACTION_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_processed));
                    }
                    break;
                case 303:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_processed))) {
                        FragmentList.add(TransactionPurchaseRouter.instanceTxListFromNotification(this, TransactionPurchaseRouter.ONGOING_DELIVERY_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_processed));
                    }
                    break;
                case 304:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_reject))) {
                        FragmentList.add(TransactionPurchaseRouter.instanceTxListFromNotification(this, TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_reject));
                    }
                    break;
                case 305:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_delivered))) {
                        FragmentList.add(TransactionPurchaseRouter.instanceTxListFromNotification(this, TransactionPurchaseRouter.TRANSACTION_DELIVERED_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_delivered));
                    }
                    break;
                case 306:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxRouter.instanceInboxResCenterFromNotification(this, InboxRouter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 115:
                    if (!isAlreadyExist(getString(R.string.title_my_dispute))) {
                        FragmentList.add(InboxRouter.instanceInboxResCenterFromNotification(this, InboxRouter.RESO_MINE));
                        CONTENT.add(getString(R.string.title_my_dispute));
                    }
                    break;
                case 125:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxRouter.instanceInboxResCenterFromNotification(this, InboxRouter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 135:
                    if (!isAlreadyExist(getString(R.string.title_my_dispute))) {
                        FragmentList.add(InboxRouter.instanceInboxResCenterFromNotification(this, InboxRouter.RESO_MINE));
                        CONTENT.add(getString(R.string.title_my_dispute));
                    }
                    break;
                case 145:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxRouter.instanceInboxResCenterFromNotification(this, InboxRouter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 155:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxRouter.instanceInboxResCenterFromNotification(this, InboxRouter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 165:
                    if (!isAlreadyExist(getString(R.string.title_my_dispute))) {
                        FragmentList.add(InboxRouter.instanceInboxResCenterFromNotification(this, InboxRouter.RESO_MINE));
                        CONTENT.add(getString(R.string.title_my_dispute));
                    }
                    break;
            }
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //notif = new NotificationVariable(this);
        //notif.CreateNotification();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        adapter = new PagerAdapter(getFragmentManager());
        for (String aCONTENT : CONTENT) indicator.addTab(indicator.newTab().setText(aCONTENT));
        mViewPager.setOffscreenPageLimit(FragmentList.size());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(pos);
        new NotificationModHandler(this).cancelNotif();
        //final ActionBar actionBar = getActionBar();

		/*actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				hideKey();
			}
			

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				    hideKey();
				
				 mViewPager.setCurrentItem(tab.getPosition());
				 if (ContextualStats) {
						mode.finish();
						ContextualStats = false;
					}
					//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					//imm.hideSoftInputFromWindow(tab.getCustomView().getWindowToken(), 0);

			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				hideKey();
			}
		 
		 };

		/*actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_payment_confirmation)).setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_order_status)).setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_receive_confirmation)).setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_transaction_list)).setTabListener(tabListener));*/

    }

    public void hideKey() {
        View target = getCurrentFocus();
        if (target != null) {
            InputMethodManager imm = (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentList.get(position);
        }

        @Override
        public int getCount() {
            return CONTENT.size();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        LocalCacheHandler Cache = new LocalCacheHandler(getBaseContext(), TkpdCache.NOTIFICATION_DATA);
        int CartCache = Cache.getInt(TkpdCache.Key.IS_HAS_CART);

        if (CartCache > 0) {
            menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart_active);
        } else {
            menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search)
            return onSearchOptionSelected();
        else if (item.getItemId() == R.id.action_search) {

            if (!SessionHandler.isV4Login(getBaseContext())) {
                Intent intent = SessionRouter.getLoginActivityIntent(this);
                intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                startActivity(intent);
            } else {
                startActivity(TransactionCartRouter.createInstanceCartActivity(this));
            }
            return true;
        } else
            return super.onOptionsItemSelected(item);

    }

    @Override
    public void onGetNotif() {
        if (MainApplication.getNotificationStatus()) {
//            drawer.getNotification();
        }
        if (MainApplication.getDrawerStatus()) {
//            drawer.updateData();
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        if (isLogin != SessionHandler.isV4Login(getBaseContext())) {
            finish();
        }
        MainApplication.setCurrentActivity(this);
        if (MainApplication.getNotificationStatus()) {
//            drawer.getNotification();
        }
        if (MainApplication.getDrawerStatus()) {
//            drawer.updateData();
        }
        super.onResume();
    }

    public Boolean isAlreadyExist(String name) {
        for (int i = 0; i < CONTENT.size(); i++) {
            if (name.equals(CONTENT.get(i))) return true;
        }
        return false;
    }

    @Override
    public void onRefreshCart(int status) {
        LocalCacheHandler Cache = new LocalCacheHandler(this, TkpdCache.NOTIFICATION_DATA);
        Cache.putInt(TkpdCache.Key.IS_HAS_CART, status);
        Cache.applyEditor();
        invalidateOptionsMenu();
        MainApplication.resetCartStatus(false);
    }


    @Override
    protected List<Fragment> getFragmentList() {
        return detailList;
    }


    @Override
    public void onDetailPageRequest(List<Fragment> fragmentList, int page) {
        detailList = new ArrayList<>();
        detailList = fragmentList;
        createDetailView(page);
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
}
