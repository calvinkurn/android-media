package com.tokopedia.buyerorder.list.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics;
import com.tokopedia.buyerorder.list.common.OrderListContants;
import com.tokopedia.buyerorder.list.data.OrderCategory;
import com.tokopedia.buyerorder.list.data.OrderLabelList;
import com.tokopedia.buyerorder.list.data.ticker.TickerResponse;
import com.tokopedia.buyerorder.list.data.ticker.TickersItem;
import com.tokopedia.buyerorder.list.di.DaggerOrderListComponent;
import com.tokopedia.buyerorder.list.di.OrderListComponent;
import com.tokopedia.buyerorder.list.view.adapter.OrderTabAdapter;
import com.tokopedia.buyerorder.list.view.listener.GlobalMainTabSelectedListener;
import com.tokopedia.buyerorder.list.view.presenter.OrderListInitContract;
import com.tokopedia.buyerorder.list.view.presenter.OrderListInitPresenterImpl;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;
import com.tokopedia.unifycomponents.ticker.TickerData;
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter;
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseSimpleActivity
        implements HasComponent<OrderListComponent>, OrderListInitContract.View, OrderTabAdapter.Listener {
    private static final String ORDER_CATEGORY = OrderCategory.KEY_LABEL;
    private static final int REQUEST_CODE = 100;
    private String orderCategory = "ALL";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OrderTabAdapter adapter;
    private OrderListComponent orderListComponent;
    private OrderListInitContract.Presenter presenter;
    private TickerPagerAdapter tickerPagerAdapter;
    private Ticker tickerAnnouncement;
    private Context context;
    private static final String TICKER_URL = "https://m.tokopedia.com/myorder/buyer/ticker-info?id=";
    private static final String SPANNABLE = "[info selengkapnya]";
    public static final String KEY_TITLE = "title";
    OrderListAnalytics orderListAnalytics;

    @DeepLink({ApplinkConst.PURCHASE_CONFIRMED, ApplinkConst.PURCHASE_ORDER})
    public static Intent getConfirmedIntent(Context context, Bundle extras) {
        return getMarketPlaceIntent(context, extras);

    }

    @DeepLink(ApplinkConst.ORDER_LIST_WEBVIEW)
    public static Intent getOrderList(Context context, Bundle extras) {
        Intent intent = new Intent(context, OrderListActivity.class);
        return intent.putExtras(extras);
    }

    @DeepLink(ApplinkConst.PURCHASE_PROCESSED)
    public static Intent getProcessedIntent(Context context, Bundle extras) {
        return getMarketPlaceIntent(context, extras);
    }

    @DeepLink({ApplinkConst.PURCHASE_SHIPPED})
    public static Intent getShippedIntent(Context context, Bundle extras) {
        return getMarketPlaceIntent(context, extras);
    }

    @DeepLink({ApplinkConst.PURCHASE_DELIVERED, ApplinkConst.PURCHASE_SHIPPING_CONFIRM})
    public static Intent getDeliveredIntent(Context context, Bundle extras) {
        return getMarketPlaceIntent(context, extras);
    }

    @DeepLink(ApplinkConst.PURCHASE_HISTORY)
    public static Intent getHistoryIntent(Context context, Bundle extras) {
        return getMarketPlaceIntent(context, extras);
    }


    private static Intent getMarketPlaceIntent(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI));
        extras.putString(OrderCategory.KEY_LABEL, OrderCategory.MARKETPLACE);
        extras.putString(OrderListContants.ORDER_FILTER_ID, uri.getQueryParameter(OrderListContants.ORDER_FILTER_ID));
        Intent intent = new Intent(context, OrderListActivity.class);
        return intent.putExtras(extras);
    }

    @DeepLink({ApplinkConst.DEALS_ORDER,
            ApplinkConst.DIGITAL_ORDER,
            ApplinkConst.EVENTS_ORDER,
            ApplinkConst.GIFT_CARDS_ORDER,
            ApplinkConst.INSURANCE_ORDER,
            ApplinkConst.MODAL_TOKO_ORDER})
    public static Intent getOrderListIntent(Context context, Bundle bundle) {

        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        String link = bundle.getString(DeepLink.URI);
        String category = link.substring(link.indexOf("//") + 2, link.lastIndexOf("/")).toUpperCase();
        bundle.putString(ORDER_CATEGORY, category);
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_order_list_module;
    }

    @DeepLink(ApplinkConst.HOTEL_ORDER)
    public static Intent getHotelOrderListIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putString(ORDER_CATEGORY, OrderCategory.HOTELS);
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @DeepLink(ApplinkConst.FLIGHT_ORDER)
    public static Intent getFlightOrderListIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putString(ORDER_CATEGORY, OrderCategory.FLIGHTS);
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @DeepLink({ApplinkConst.BELANJA_ORDER, ApplinkConst.MARKETPLACE_ORDER_FILTER})
    public static Intent getMarketPlaceOrderListIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putString(ORDER_CATEGORY, OrderCategory.MARKETPLACE);
        Intent intent = new Intent(context, OrderListActivity.class);
        if (uri != null) {
            intent.setData(uri.build());
        }
        return intent.putExtras(bundle);
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, OrderListActivity.class);
    }

    protected void initVar() {
        tabLayout = findViewById(R.id.indicator);
        viewPager = findViewById(R.id.pager);
        tickerAnnouncement = findViewById(R.id.ticker_announcement);
        presenter = new OrderListInitPresenterImpl(this, new GraphqlUseCase());
    }

    @Override
    public OrderListComponent getComponent() {
        if (orderListComponent == null) initInjector();
        return orderListComponent;
    }

    private void initInjector() {
        orderListComponent = DaggerOrderListComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        Bundle bundle = getIntent().getExtras();
        context = this;
        if (bundle != null) {
            String url = bundle.getString("url");
            if (url != null && (Uri.parse(url).getQueryParameter("tab") != null))
                orderCategory = Uri.parse(url).getQueryParameter("tab");
            else if (bundle.getString(ORDER_CATEGORY) != null)
                orderCategory = bundle.getString(ORDER_CATEGORY);
            else
                orderCategory = OrderCategory.MARKETPLACE;
        }
        orderListAnalytics = new OrderListAnalytics();
        UserSession userSession = new UserSession(this);
        if (!userSession.isLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE);
        } else {
            getInitialData(orderCategory);
            presenter.getTickerInfo();
        }

    }

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }

    @Override
    public Bundle getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return new Bundle();
        } else {
            return bundle;
        }
    }

    @Override
    public void removeProgressBarView() {
    }

    @Override
    public void showErrorNetwork(String message) {
    }

    @Override
    public void renderTabs(List<OrderLabelList> orderLabelList, String orderCategory) {
        tabLayout.removeAllTabs();
        int position = 0;
        for (int i = 0; i < orderLabelList.size(); i++) {
            if (orderCategory.equals(orderLabelList.get(i).getOrderCategory())) {
                position = i;
            }
            tabLayout.addTab(tabLayout.newTab().setText(orderLabelList.get(i).getLabelBhasa()));
        }
        adapter = new OrderTabAdapter(getSupportFragmentManager(), this, orderLabelList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        viewPager.setCurrentItem(position);
        orderListAnalytics.sendPageClickEvent("order-list");
    }


    @Override
    public void updateTicker(TickerResponse tickerResponse) {
        List<TickersItem> tickersItemList;
        ArrayList<TickerData> tickerData = new ArrayList<>();
        tickersItemList = tickerResponse.getOrderTickers().getTickers();
        if (tickersItemList.size() != 0) {
            tickerAnnouncement.setVisibility(View.VISIBLE);
            if (tickersItemList.size() > 1) {
                orderListAnalytics.sendViewTickerEvent();
                for (TickersItem tickersItem : tickersItemList) {
                    String url = TICKER_URL + tickersItem.getId();
                    String ticker_des = tickersItem.getShortDesc() + " "+SPANNABLE + "{" + url + "}";
                    tickerData.add(new TickerData("", ticker_des, Ticker.TYPE_ANNOUNCEMENT, true, tickersItem));
                }

                tickerPagerAdapter = new TickerPagerAdapter(this, tickerData);
                tickerPagerAdapter.setPagerDescriptionClickEvent(new TickerPagerCallback() {
                    @Override
                    public void onPageDescriptionViewClick(@NotNull CharSequence charSequence, @Nullable Object ticker) {
                        TickersItem tickersItem = (TickersItem) ticker;
                        orderListAnalytics.sendClickTickerEvent(tickersItem.getId().toString());
                        Intent intent = RouteManager.getIntent(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, charSequence));
                        intent.putExtra(KEY_TITLE, getResources().getString(R.string.tkpdtransaction_web_title));
                        startActivity(intent);
                    }
                });
                tickerAnnouncement.setAutoSliderActive(true);
                tickerAnnouncement.setAutoSlideDelay(5000);
                tickerAnnouncement.addPagerView(tickerPagerAdapter, tickerData);
            } else {
                String url = TICKER_URL + tickersItemList.get(0).getId();
                String ticker_des = tickersItemList.get(0).getShortDesc() + SPANNABLE + "{" + url + "}";
                tickerAnnouncement.setTextDescription(ticker_des);
                tickerAnnouncement.setDescriptionClickEvent(new TickerCallback() {
                    @Override
                    public void onDescriptionViewClick(@NotNull CharSequence charSequence) {

                        Intent intent = RouteManager.getIntent(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, charSequence));
                        intent.putExtra(KEY_TITLE, getResources().getString(R.string.tkpdtransaction_web_title));
                        startActivity(intent);

                    }

                    @Override
                    public void onDismiss() {
                        orderListAnalytics.sendClickCloseTickerEvent();

                    }
                });

            }
        }
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            KeyboardHandler.hideSoftKeyboard(OrderListActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getInitialData(orderCategory);
            } else {
                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        presenter.destroyView();
        super.onDestroy();
    }

    public void getInitialData(String orderCategory) {
        presenter.getInitData(orderCategory);
    }
}
