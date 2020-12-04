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

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.buyerorder.R;
import com.tokopedia.buyerorder.common.util.BuyerConsts;
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

import static com.tokopedia.buyerorder.common.util.BuyerConsts.APPLINK_INTERNAL_ORDER;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.HOST_BELANJA;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.HOST_BUYER;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.HOST_DIGITAL;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.HOST_FLIGHT;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.HOST_HOTEL;
import static com.tokopedia.buyerorder.common.util.BuyerConsts.HOST_ORDER_LIST;

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

    @Override
    protected int getToolbarResourceID() {
        return R.id.buyer_order_list_toolbar;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_order_list_module;
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

    private static Intent getMarketPlaceIntent(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putString(OrderCategory.KEY_LABEL, OrderCategory.MARKETPLACE);
        bundle.putString(OrderListContants.ORDER_FILTER_ID, uri.getQueryParameter(OrderListContants.ORDER_FILTER_ID));
        Intent intent = new Intent(context, OrderListActivity.class);
        return intent.putExtras(bundle);
    }

    public static Intent getOrderListIntent(Context context, String uriStr, Uri uri) {
        Bundle bundle = new Bundle();
        String category = uriStr.substring(uriStr.indexOf("//") + 2, uriStr.lastIndexOf("/")).toUpperCase();
        bundle.putString(ORDER_CATEGORY, category);
        return new Intent(context, OrderListActivity.class)
                .setData(uri)
                .putExtras(bundle);
    }

    public static Intent getHotelOrderListIntent(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_CATEGORY, OrderCategory.HOTELS);
        return new Intent(context, OrderListActivity.class)
                .setData(uri)
                .putExtras(bundle);
    }

    public static Intent getFlightOrderListIntent(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_CATEGORY, OrderCategory.FLIGHTS);
        return new Intent(context, OrderListActivity.class)
                .setData(uri)
                .putExtras(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().getData() != null) {
            String uriStr = String.valueOf(getIntent().getData());

            if (uriStr.contains(HOST_BUYER) || uriStr.contains(HOST_BELANJA)
                    || uriStr.equalsIgnoreCase(APPLINK_INTERNAL_ORDER) || uriStr.contains(HOST_ORDER_LIST)) {
                orderCategory = OrderCategory.MARKETPLACE;
                if (getIntent().getExtras() != null) {
                    getIntent().getExtras().putString(OrderListContants.ORDER_FILTER_ID, getIntent().getData().getQueryParameter(OrderListContants.ORDER_FILTER_ID));
                    getIntent().getExtras().putString(OrderCategory.KEY_LABEL, OrderCategory.MARKETPLACE);
                }
            } else {
                if (uriStr.contains(BuyerConsts.HOST_DEALS) || uriStr.contains(BuyerConsts.HOST_EVENTS)
                        || uriStr.contains(BuyerConsts.HOST_GIFTCARDS) || uriStr.contains(BuyerConsts.HOST_INSURANCE)
                        || uriStr.contains(BuyerConsts.HOST_MODALTOKO)) {
                    orderCategory = uriStr.substring(uriStr.indexOf("//") + 2, uriStr.lastIndexOf("/")).toUpperCase();

                }  else if (uriStr.contains(HOST_DIGITAL)) {
                    orderCategory = OrderCategory.DIGITAL;

                } else if (uriStr.contains(HOST_HOTEL)) {
                    orderCategory = OrderCategory.HOTELS;

                } else if (uriStr.contains(HOST_FLIGHT)) {
                    orderCategory = OrderCategory.FLIGHTS;
                }
            }
            if (getIntent().getExtras() != null) {
                getIntent().getExtras().putString(ORDER_CATEGORY, orderCategory);
            }
        }

        super.onCreate(savedInstanceState);
        initVar();

        context = this;
        orderListAnalytics = new OrderListAnalytics();
        UserSession userSession = new UserSession(this);
        if (!userSession.isLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE);
        } else {
            getInitialData(orderCategory);
            presenter.getTickerInfo(this);
        }

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
    public Context getActivity() {
        return this;
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
        presenter.getInitData(GraphqlHelper.loadRawString(getResources(),
                R.raw.initorderlist), orderCategory);
    }
}
