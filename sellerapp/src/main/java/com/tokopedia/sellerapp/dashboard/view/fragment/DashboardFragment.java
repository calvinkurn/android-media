package com.tokopedia.sellerapp.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.design.ticker.TickerView;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.shopscore.view.activity.ShopScoreDetailActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.di.DaggerSellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.di.SellerDashboardComponent;
import com.tokopedia.sellerapp.dashboard.presenter.SellerDashboardPresenter;
import com.tokopedia.sellerapp.dashboard.view.listener.SellerDashboardView;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;
import com.tokopedia.sellerapp.home.view.widget.ShopScoreWidget;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by nathan on 9/6/17.
 */

public class DashboardFragment extends BaseDaggerFragment implements SellerDashboardView {

    private ViewGroup vgHeaderLabelLayout;
    private SwipeToRefresh swipeRefreshLayout;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Inject
    public SellerDashboardPresenter sellerDashboardPresenter;

    private TickerView tickerView;
    private LoadingStateView headerShopInfoLoadingStateView;
    private ImageView shopIconImageView;
    private TextView shopNameTextView;
    private ImageView gmIconImageView;
    private TextView gmStatusTextView;

    private ShopScoreWidget shopScoreWidget;

    private TextView reputationPointTextView;
    private ShopReputationView shopReputationView;
    private TextView transactionSuccessTextView;

    private LabelView newOrderLabelView;
    private LabelView deliveryConfirmationLabelView;
    private LabelView deliveryStatusLabelView;
    private LabelView opportunityLabelView;
    private LabelView messageLabelView;
    private LabelView discussionLabelView;
    private LabelView reviewLabelView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tickerView = (TickerView) view.findViewById(R.id.ticker_view);
        headerShopInfoLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_header);
        vgHeaderLabelLayout = (ViewGroup) view.findViewById(R.id.label_layout_header);
        shopIconImageView = (ImageView) view.findViewById(R.id.image_view_shop_icon);
        shopNameTextView = (TextView) view.findViewById(R.id.text_view_shop_name);
        gmIconImageView = (ImageView) view.findViewById(R.id.image_view_gm_icon);
        gmStatusTextView = (TextView) view.findViewById(R.id.text_view_gm_status);
        reputationPointTextView = (TextView) view.findViewById(R.id.text_view_reputation_point);
        shopReputationView = (ShopReputationView) view.findViewById(R.id.shop_reputation_view);
        transactionSuccessTextView = (TextView) view.findViewById(R.id.text_view_transaction_success);

        newOrderLabelView = (LabelView) view.findViewById(R.id.label_view_new_order);
        deliveryConfirmationLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_confirmation);
        deliveryStatusLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_status);
        opportunityLabelView = (LabelView) view.findViewById(R.id.label_view_opportunity);
        messageLabelView = (LabelView) view.findViewById(R.id.label_view_message);
        discussionLabelView = (LabelView) view.findViewById(R.id.label_view_discussion);
        reviewLabelView = (LabelView) view.findViewById(R.id.label_view_review);
        shopScoreWidget = (ShopScoreWidget) view.findViewById(R.id.shop_score_widget);
        newOrderLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deliveryConfirmationLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deliveryStatusLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        opportunityLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        messageLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        discussionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        reviewLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shopScoreWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopScoreDetailActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sellerDashboardPresenter.refreshShopInfo();
            }
        });

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        SellerDashboardComponent sellerDashboardComponent =
                DaggerSellerDashboardComponent.builder().appComponent(
                        MainApplication.getInstance().getApplicationComponent()).build();
        sellerDashboardComponent.inject(this);

        sellerDashboardPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_LOADING);
        sellerDashboardPresenter.getShopInfoWithScore();
        sellerDashboardPresenter.getTicker();
        sellerDashboardPresenter.getNotification();
    }

    @Override
    public void onErrorShopInfoAndScore(Throwable t) {
        //TODO snackbar error
    }

    @Override
    public void onSuccessGetShopInfoAndScore(ShopModel shopModel, ShopScoreViewModel shopScoreViewModel) {
        swipeRefreshLayout.setRefreshing(false);

        // TODO update view
        Info shopModelInfo = shopModel.info;
        headerShopInfoLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        shopNameTextView.setText(shopModelInfo.getShopName());
        if (shopModelInfo.isShopIsGoldBadge()) {
            gmIconImageView.setVisibility(View.VISIBLE);
        } else {
            gmIconImageView.setVisibility(View.GONE);
        }
        shopReputationView.setValue(0, 0, 0);

        updateViewShopOpen(shopModel);

        shopScoreWidget.renderView(shopScoreViewModel);
    }

    private void updateViewShopOpen(ShopModel shopModel){
        View vShopClose = vgHeaderLabelLayout.findViewById(R.id.vg_shop_close);
        if (shopModel.isOpen != ShopModel.IS_CLOSED) {
            if (vShopClose!= null) {
                vgHeaderLabelLayout.removeView(vShopClose);
            }
        } else {
            if (vShopClose== null) {
                vShopClose = LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_dashboard_shop_close,
                                vgHeaderLabelLayout, false);
                vgHeaderLabelLayout.addView(vShopClose);
            }
            TextView tvCloseTitle = (TextView) vShopClose.findViewById(R.id.tv_closed_title);
            String shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_DD_MM_YYYY,
                    DateFormatUtils.FORMAT_DD_MMMM_YYYY,
                    shopModel.closedInfo.until);
            if (!TextUtils.isEmpty(shopCloseUntilString)) {
                tvCloseTitle.setText(getString(R.string.dashboard_your_shop_is_closed_until_xx, shopModel.closedInfo.until));
                tvCloseTitle.setVisibility(View.VISIBLE);
            } else {
                tvCloseTitle.setVisibility(View.GONE);
            }

            TextView tvCloseDesc = (TextView) vShopClose.findViewById(R.id.tv_closed_description);
            String note = shopModel.closedInfo.note;
            if (!TextUtils.isEmpty(note)) {
                tvCloseDesc.setText(note);
                tvCloseDesc.setVisibility(View.VISIBLE);
            } else {
                tvCloseDesc.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onErrorGetTickers(Throwable throwable) {
        tickerView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetTickers(Ticker.Tickers[] tickers) {
        tickerView.setVisibility(View.VISIBLE);
        ArrayList<String> messages = new ArrayList<>();
        for (Ticker.Tickers ticker : tickers) {
            messages.add(ticker.getBasicMessage());
        }
        tickerView.setListMessage(messages);
        tickerView.setHighLightColor(ContextCompat.getColor(getContext(), R.color.tkpd_yellow_status));
        tickerView.setOnPartialTextClickListener(new TickerView.OnPartialTextClickListener() {
            @Override
            public void onClick(View view, String messageClick) {
                Intent intent = new Intent(getActivity(), BannerWebView.class);
                intent.putExtra("url", messageClick);
                startActivity(intent);
            }
        });
        tickerView.buildView();
    }

    @Override
    public void onErrorGetNotifiction(String message) {
        // TODO on Error get notification
    }

    @Override
    public void onSuccessGetNotification(DrawerNotification drawerNotification) {
        int newOrderCount = drawerNotification.getSellingNewOrder();
        int shippingConfirmation = drawerNotification.getSellingShippingConfirmation();
        int shippingStatus = drawerNotification.getSellingShippingStatus();

        int inboxCount = drawerNotification.getInboxMessage();
        int discussCount = drawerNotification.getInboxTalk();
        int reviewCount = drawerNotification.getInboxReview();

        newOrderLabelView.setContent(String.valueOf(newOrderCount));
        deliveryConfirmationLabelView.setContent(String.valueOf(shippingConfirmation));
        deliveryStatusLabelView.setContent(String.valueOf(shippingStatus));

        messageLabelView.setContent(String.valueOf(inboxCount));
        discussionLabelView.setContent(String.valueOf(discussCount));
        reviewLabelView.setContent(String.valueOf(reviewCount));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sellerDashboardPresenter.detachView();
    }
}
