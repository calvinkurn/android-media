package com.tokopedia.core.talk.inboxtalk.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.GCMListenerService;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.talk.inboxtalk.fragment.InboxTalkFragment;
import com.tokopedia.core.talk.inboxtalk.intentservice.InboxTalkIntentService;
import com.tokopedia.core.talk.inboxtalk.intentservice.InboxTalkResultReceiver;
import com.tokopedia.core.talk.inboxtalk.listener.InboxTalkActivityView;
import com.tokopedia.core.talk.inboxtalk.presenter.InboxTalkActivityPresenterImpl;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InboxTalkActivity extends DrawerPresenterActivity implements
        InboxTalkActivityView,
        GCMListenerService.NotificationListener, InboxTalkResultReceiver.Receiver {

    private static final String BUNDLE_POSITION = "INBOX_TALK_POSITION";
    PagerAdapter adapter;

    @BindView(R2.id.pager)
    ViewPager mViewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;


    private Boolean ContextualStats = false;
    private ActionMode mode;
    private Boolean isLogin;
    private String[] contentArray;

    private Boolean fromNotif = false;
    private Boolean forceUnread;
    InboxTalkResultReceiver mReceiver;

    @Override
    public void onStart() {
        super.onStart();
        isLogin = SessionHandler.isV4Login(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        initResultReceiver();

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_TALK;
    }

    private void setContent() {
        if (isSellerApp()) {
            setContentSellerApp();
        } else {
            setContentBuyerApp();
        }

        for (String content : contentArray) {
            indicator.addTab(indicator.newTab().setText(content));
        }
    }

    private void setContentSellerApp() {
        contentArray = new String[]{getString(R.string.title_my_product)};
        indicator.setVisibility(View.GONE);
    }

    private void setContentBuyerApp() {
        if (checkHasNoShop()) {
            contentArray = new String[]{getString(R.string.title_menu_all)};
            indicator.setVisibility(View.GONE);
        } else {
            contentArray = new String[]{getString(R.string.title_menu_all),
                    getString(R.string.title_my_product),
                    getString(R.string.title_following)};
            indicator.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkHasNoShop() {
        return SessionHandler.getShopID(this) == null
                || SessionHandler.getShopID(this).equals("0")
                || SessionHandler.getShopID(this).equals("");
    }

    private void getExtras() {
        if (getIntent().getBooleanExtra("from_notif", false)) {
            fromNotif = true;
            new NotificationModHandler(this).cancelNotif();
        }
        forceUnread = getIntent().getBooleanExtra("unread", false);
    }

    private void initResultReceiver() {
        mReceiver = new InboxTalkResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        if (isLogin != SessionHandler.isV4Login(getBaseContext())) {
            finish();
        }
        MainApplication.setCurrentActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MainApplication.setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTalkActivityPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inbox_talk;
    }

    @Override
    protected void initView() {
        super.initView();
        drawer.setDrawerPosition(TkpdState.DrawerPosition.INBOX_TALK);
        ButterKnife.bind(this);
        setContent();
        adapter = new PagerAdapter(getFragmentManager());
        mViewPager.setOffscreenPageLimit(contentArray.length);
        mViewPager.setAdapter(adapter);

        if (getIntent().getExtras() != null && getIntent().getExtras().getInt(BUNDLE_POSITION, -1) != -1) {
            mViewPager.setCurrentItem(getIntent().getExtras().getInt(BUNDLE_POSITION));
        }
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_TALK;
    }

    @Override
    protected void setViewListener() {
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

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
    public void onRefreshCart(int status) {
        LocalCacheHandler Cache = new LocalCacheHandler(this,
                "NOTIFICATION_DATA");
        Cache.putInt("is_has_cart", status);
        Cache.applyEditor();
        invalidateOptionsMenu();
        MainApplication.resetCartStatus(false);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        InboxTalkFragment fragment = (InboxTalkFragment) adapter.getItem(mViewPager.getCurrentItem());

        if (fragment != null && fragment.getActivity() != null) {
            switch (resultCode) {
                case InboxTalkIntentService.STATUS_SUCCESS_FOLLOW:
                case InboxTalkIntentService.STATUS_SUCCESS_DELETE:
                case InboxTalkIntentService.STATUS_SUCCESS_REPORT:
                    onReceiveResultSuccess(fragment, resultData, resultCode);
                    break;
                case InboxTalkIntentService.STATUS_ERROR_FOLLOW:
                case InboxTalkIntentService.STATUS_ERROR_DELETE:
                case InboxTalkIntentService.STATUS_ERROR_REPORT:
                    onReceiveResultError(fragment, resultData, resultCode);
                    break;
            }
        } else {
            Intent intent = getIntent();
            intent.putExtra(BUNDLE_POSITION, mViewPager.getCurrentItem());
            finish();
            startActivity(intent);
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData, int resultCode) {
        ((InboxTalkFragment) fragment).onErrorAction(resultData, resultCode);
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData, int resultCode) {
        ((InboxTalkFragment) fragment).onSuccessAction(resultData, resultCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        drawer.getNotification();
    }

    public void followTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_FOLLOW);
    }

    public void deleteTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_DELETE);
    }

    public void reportTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_REPORT);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if (position < fragmentList.size()) {
                fragment = fragmentList.get(position);
            } else {
                Bundle b = new Bundle();
                if (isSellerApp()) {
                    b.putString("nav", "inbox-talk-my-product");
                } else {
                    switch (position) {
                        case 0:
                            b.putString("nav", "inbox-talk");
                            break;
                        case 1:
                            b.putString("nav", "inbox-talk-my-product");
                            break;
                        case 2:
                            b.putString("nav", "inbox-talk-following");
                            break;
                    }
                }
                b.putBoolean("unread", forceUnread);
                fragment = new InboxTalkFragment();
                fragment.setArguments(b);
                this.fragmentList.add(fragment);
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return contentArray.length;
        }
    }

    private boolean isSellerApp() {
        return getApplication().getClass().getSimpleName().equals("SellerMainApplication");
    }
}
