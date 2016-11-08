package com.tokopedia.core.purchase.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import com.tokopedia.core.GCMListenerService;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.purchase.adapter.PurchaseTabAdapter;
import com.tokopedia.core.purchase.fragment.TxListFragment;
import com.tokopedia.core.purchase.fragment.TxSummaryFragment;
import com.tokopedia.core.purchase.utils.FilterUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import butterknife.Bind;


/**
 * @author by anggaprasetiyo on 8/26/16.
 */
public class PurchaseActivity extends DrawerPresenterActivity implements
        TxSummaryFragment.OnCenterMenuClickListener, GCMListenerService.NotificationListener,
        PurchaseTabAdapter.Listener, TxListFragment.StateFilterListener {
    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    public static final String EXTRA_STATE_TX_FILTER = "EXTRA_STATE_TX_FILTER";
    public static final int TAB_TX_SUMMARY = 0;
    public static final int TAB_TX_CONFIRMATION = 1;
    public static final int TAB_TX_VERIFICATION = 2;
    public static final int TAB_TX_STATUS = 3;
    public static final int TAB_TX_DELIVER = 4;
    public static final int TAB_TX_ALL = 5;

    @Bind(R2.id.pager)
    ViewPager viewPager;
    @Bind(R2.id.indicator)
    TabLayout indicator;

    public String[] tabContents;

    private int drawerPosition;
    private String stateTxFilterID;

    public static Intent createIntentTxSummary(Context context) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_SUMMARY);
        return intent;
    }

    public static Intent createIntentTxCanceled(Context context) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_ALL);
        intent.putExtra(EXTRA_STATE_TX_FILTER, FilterUtils.TRANSACTION_CANCELED_FILTER_ID);
        return intent;
    }

    public static Intent createIntentTxAll(Context context) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_ALL);
        intent.putExtra(EXTRA_STATE_TX_FILTER, FilterUtils.ALL_STATUS_FILTER_ID);
        return intent;
    }

    public static Intent createIntentConfirmShipping(Context context) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_DELIVER);
        return intent;
    }

    public static Intent createIntentTxVerification(Context context) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_VERIFICATION);
        return intent;
    }

    public static Intent createIntentTxStatus(Context context) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_STATUS);
        return intent;
    }

    public static Intent createIntentConfirmPayment(Context context) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(EXTRA_STATE_TAB_POSITION, TAB_TX_CONFIRMATION);
        return intent;
    }

    @Override
    protected int setDrawerPosition() {
        return drawerPosition;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        drawerPosition = extras.getInt(EXTRA_STATE_TAB_POSITION, 0);
        stateTxFilterID = extras.getString(EXTRA_STATE_TX_FILTER, FilterUtils.ALL_STATUS_FILTER_ID);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase;
    }

    @Override
    protected void setViewListener() {
        for (String tabContent : tabContents)
            indicator.addTab(indicator.newTab().setText(tabContent));
        PurchaseTabAdapter adapter = new PurchaseTabAdapter(getFragmentManager(), this);
        viewPager.setOffscreenPageLimit(tabContents.length);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        viewPager.setCurrentItem(drawerPosition);
    }

    @Override
    protected void initVar() {
        tabContents = new String[]{getString(R.string.title_dashboard_purchase),
                getString(R.string.title_payment_confirmation),
                getString(R.string.title_payment_verification),
                getString(R.string.title_order_status),
                getString(R.string.title_receive_confirmation),
                getString(R.string.title_transaction_list)};
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        if (!SessionHandler.isV4Login(getBaseContext())) {
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void setActionVar() {
        String screenName = getString(R.string.transaction_people_page);
        ScreenTracking.screenLoca(screenName);
    }

    private void setDrawerPosition(int position) {
        switch (position) {
            case 0:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_TRANSACTION);
                break;
            case 1:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_CONFIRM_PAYMENT);
                break;
            case 2:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_CONFIRM_PAYMENT);
                break;
            case 3:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS);
                break;
            case 4:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING);
                break;
            case 5:
                switch (stateTxFilterID) {
                    case FilterUtils.TRANSACTION_CANCELED_FILTER_ID:
                        drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED);
                        break;
                    default:
                        drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST);
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnMenuClick(int position, String stateTxFilter) {
        this.stateTxFilterID = stateTxFilter;
        this.viewPager.setCurrentItem(position);
    }

    @Override
    public String getFilterCaseAllTransaction() {
        return stateTxFilterID;
    }

    @Override
    public String getStateTxFilterID() {
        return stateTxFilterID;
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {
        private static final int DRAWER_POSITION_PAYMENT_CONFIRM = 1;
        private static final int DRAWER_POSITION_BUY = 0;

        private static final int VIEW_PAGER_POSITION_BUY = 0;
        private static final int VIEW_PAGER_POSITION_CHANGE_PAYMENT = 2;

        public OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            hideKeyboard();
            if (position == VIEW_PAGER_POSITION_BUY) {
                setDrawerPosition(DRAWER_POSITION_BUY);
            } else if (position == VIEW_PAGER_POSITION_CHANGE_PAYMENT) {
                setDrawerPosition(DRAWER_POSITION_PAYMENT_CONFIRM);
            } else {
                setDrawerPosition(position);
            }
        }

        private void hideKeyboard() {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    }
}
