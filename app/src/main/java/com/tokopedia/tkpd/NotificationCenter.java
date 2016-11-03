package com.tokopedia.tkpd;

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
import com.tokopedia.tkpd.GCMListenerService.NotificationListener;
import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.app.MultiPaneActivity;
import com.tokopedia.tkpd.gcm.NotificationModHandler;
import com.tokopedia.tkpd.inboxmessage.fragment.InboxMessageFragment;
import com.tokopedia.tkpd.inboxreputation.fragment.InboxReputationFragment;
import com.tokopedia.tkpd.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.tkpd.listener.GlobalMainTabSelectedListener;
import com.tokopedia.tkpd.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.tkpd.purchase.fragment.TxListFragment;
import com.tokopedia.tkpd.purchase.utils.FilterUtils;
import com.tokopedia.tkpd.rescenter.inbox.fragment.InboxResCenterFragment;
import com.tokopedia.tkpd.session.Login;
import com.tokopedia.tkpd.session.presenter.Session;
import com.tokopedia.tkpd.talk.inboxtalk.fragment.InboxTalkFragment;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.TkpdCache;
import com.tokopedia.tkpd.var.TkpdState;

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
                    fragment = InboxMessageFragment.createInstance("inbox-message");
                    bundle.putBoolean("from_notif", true);
                    bundle.putString("nav", "inbox-message");
                    fragment.setArguments(bundle);
                    FragmentList.add(fragment);
                    CONTENT.add(getString(R.string.title_message));
                    break;
                case 102:
                    fragment = new InboxTalkFragment();
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
                    FragmentList.add(new InboxTicketFragment());
                    CONTENT.add(getString(R.string.title_activity_inbox_ticket));
                    break;
                case 401:
//                    fragment = FragmentShopNewOrderV2.createInstance();
                    fragment = FragmentSellingNewOrder.createInstance();
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
                        FragmentList.add(TxListFragment.instanceFromNotification
                                (FilterUtils.PAYMENT_VERIFICATION_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_verified));
                    }
                    break;
                case 302:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_processed))) {
                        FragmentList.add(TxListFragment.instanceFromNotification
                                (FilterUtils.PROCESSING_TRANSACTION_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_processed));
                    }
                    break;
                case 303:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_processed))) {
                        FragmentList.add(TxListFragment.instanceFromNotification
                                (FilterUtils.ONGOING_DELIVERY_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_processed));
                    }
                    break;
                case 304:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_reject))) {
                        FragmentList.add(TxListFragment.instanceFromNotification
                                (FilterUtils.TRANSACTION_CANCELED_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_reject));
                    }
                    break;
                case 305:
                    if (!isAlreadyExist(getString(R.string.title_notifcenter_purchase_delivered))) {
                        FragmentList.add(TxListFragment.instanceFromNotification
                                (FilterUtils.TRANSACTION_DELIVERED_FILTER_ID));
                        CONTENT.add(getString(R.string.title_notifcenter_purchase_delivered));
                    }
                    break;
                case 306:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxResCenterFragment.createInstance(this, TkpdState.InboxResCenter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 115:
                    if (!isAlreadyExist(getString(R.string.title_my_dispute))) {
                        FragmentList.add(InboxResCenterFragment.createInstance(this, TkpdState.InboxResCenter.RESO_MINE));
                        CONTENT.add(getString(R.string.title_my_dispute));
                    }
                    break;
                case 125:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxResCenterFragment.createInstance(this, TkpdState.InboxResCenter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 135:
                    if (!isAlreadyExist(getString(R.string.title_my_dispute))) {
                        FragmentList.add(InboxResCenterFragment.createInstance(this, TkpdState.InboxResCenter.RESO_MINE));
                        CONTENT.add(getString(R.string.title_my_dispute));
                    }
                    break;
                case 145:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxResCenterFragment.createInstance(this, TkpdState.InboxResCenter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 155:
                    if (!isAlreadyExist(getString(R.string.title_buyer_dispute))) {
                        FragmentList.add(InboxResCenterFragment.createInstance(this, TkpdState.InboxResCenter.RESO_BUYER));
                        CONTENT.add(getString(R.string.title_buyer_dispute));
                    }
                    break;
                case 165:
                    if (!isAlreadyExist(getString(R.string.title_my_dispute))) {
                        FragmentList.add(InboxResCenterFragment.createInstance(this, TkpdState.InboxResCenter.RESO_MINE));
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
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
//	    if (drawer.mDrawerToggle.onOptionsItemSelected(item)) {
//	      return true;
//	    }else{
        switch (item.getItemId()) {
            case R.id.action_search:
                return onSearchOptionSelected();
            case R.id.action_cart:
                if (!SessionHandler.isV4Login(getBaseContext())) {
                    Intent intent = new Intent(this, Login.class);
                    intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getBaseContext(), Cart.class));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    // Handle your other action bar items...
//	}


    @Override
    public void onGetNotif() {
        if (MainApplication.getNotificationStatus()) {
            drawer.getNotification();
        }
        if (MainApplication.getDrawerStatus()) {
            drawer.updateData();
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
            drawer.getNotification();
        }
        if (MainApplication.getDrawerStatus()) {
            drawer.updateData();
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

}
